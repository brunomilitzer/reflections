package repetableannotations;

import repetableannotations.annotations.Annotations.ExecuteOnSchedule;
import repetableannotations.annotations.Annotations.ScanPackages;
import repetableannotations.annotations.Annotations.ScheduledExecutorClass;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ScanPackages(values = { "loaders" })
public class Main {

    public static void main( String[] args ) throws Throwable {
        schedule();
    }

    private static void schedule() throws URISyntaxException, IOException, ClassNotFoundException {
        ScanPackages scanPackages = Main.class.getAnnotation( ScanPackages.class );
        if ( scanPackages == null || scanPackages.values().length == 0 ) {
            return;
        }

        List<Class<?>> allClasses = getAllClasses( scanPackages.values() );
        List<Method> scheduleExecutorMethods = getScheduleExecutorMethods( allClasses );

        for ( Method method : scheduleExecutorMethods ) {
            scheduleMethodExecution( method );
        }
    }

    private static void scheduleMethodExecution( Method method ) {
        ExecuteOnSchedule[] schedules = method.getAnnotationsByType( ExecuteOnSchedule.class );

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        for (ExecuteOnSchedule schedule : schedules) {
            scheduledExecutorService.scheduleAtFixedRate( () -> runWhenScheduled(method),
                    schedule.delaySeconds(),
                    schedule.periodSeconds(),
                    TimeUnit.SECONDS
            );
        }
    }

    private static void runWhenScheduled( Method method ) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        System.out.println(String.format( "Executing at %s", dateFormat.format( currentDate ) ));

        try {
            method.invoke( null );
        } catch ( InvocationTargetException | IllegalAccessException e ) {
            e.printStackTrace();
        }
    }

    private static List<Method> getScheduleExecutorMethods( List<Class<?>> allClasses ) {
        List<Method> scheduledMethods = new ArrayList<>();

        for ( Class<?> clazz : allClasses ) {
            if ( !clazz.isAnnotationPresent( ScheduledExecutorClass.class ) ) {
                continue;
            }

            for ( Method method : clazz.getDeclaredMethods() ) {
                if ( method.getAnnotationsByType( ExecuteOnSchedule.class ).length != 0 ) {
                    scheduledMethods.add( method );
                }
            }
        }

        return scheduledMethods;
    }

    private static List<Class<?>> getAllClasses( String... packageNames )
            throws URISyntaxException, IOException, ClassNotFoundException {
        List<Class<?>> allClasses = new ArrayList<>();

        for ( String packageName : packageNames ) {
            String packageRelativePath = packageName.replace( ".", "\\" );
            System.out.println(packageRelativePath);

            URI packageUri = Main.class.getResource( packageRelativePath ).toURI();

            if ( packageUri.getScheme().equals( "file" ) ) {
                Path packageFullPath = Paths.get( packageUri );
                allClasses.addAll( getAllPackagesClasses( packageFullPath, packageName ) );
            } else if ( packageUri.getScheme().equals( "jar" ) ) {
                FileSystem fileSystem = FileSystems.newFileSystem( packageUri, Collections.emptyMap() );

                Path packageFullPathInJar = fileSystem.getPath( packageRelativePath );
                allClasses.addAll( getAllPackagesClasses( packageFullPathInJar, packageName ) );

                fileSystem.close();
            }
        }

        return allClasses;
    }

    private static List<Class<?>> getAllPackagesClasses( Path packagePath, String packageName )
            throws IOException, ClassNotFoundException {

        if ( !Files.exists( packagePath ) ) {
            return Collections.emptyList();
        }

        List<Path> files = Files.list( packagePath ).filter( Files::isRegularFile ).toList();

        List<Class<?>> classes = new ArrayList<>();

        String basePath = getBasePath( Main.class );

        for ( Path filePath : files ) {
            String fileName = filePath.getFileName().toString();

            if ( fileName.endsWith( ".class" ) ) {
                String classFullName = packageName.isBlank() ? fileName.replaceFirst( "\\.class$", "" )
                        : packageName + "." + fileName.replaceFirst( "\\.class$", "" );
                String completeFullClassName = basePath + "." + classFullName;
                Class<?> clazz = Class.forName( completeFullClassName );
                classes.add( clazz );
            }
        }

        return classes;
    }

    private static String getBasePath( Class<?> clazz ) {
        String canonicalName = clazz.getCanonicalName();
        String[] canonicalNameArray = canonicalName.split( "\\." );
        return canonicalNameArray[ 0 ];
    }

}
