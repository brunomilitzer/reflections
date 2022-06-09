package section7;

import section7.annotations.InitializerClass;
import section7.annotations.InitializerMethod;
import section7.annotations.RetryOperation;
import section7.annotations.ScanPackages;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@ScanPackages(values = { "app", "app.configs", "app.databases", "app.http" })
public class Main {

    public static void main( String[] args ) throws Throwable {
        initialize();
    }

    public static void initialize()
            throws Throwable {

        ScanPackages scanPackages = Main.class.getAnnotation( ScanPackages.class );

        if ( scanPackages == null || scanPackages.values().length == 0 ) {
            return;
        }

        List<Class<?>> classes = getAllClasses( scanPackages.values() );

        for ( Class<?> clazz : classes ) {
            if ( !clazz.isAnnotationPresent( InitializerClass.class ) ) {
                continue;
            }

            List<Method> methods = getAllInitializingMethods( clazz );

            Object instance = clazz.getDeclaredConstructor().newInstance();

            for ( Method method : methods ) {
                callInitializingMethod( instance, method );
            }
        }
    }

    private static void callInitializingMethod( Object instance, Method method ) throws Throwable {
        RetryOperation retryOperation = method.getAnnotation( RetryOperation.class );

        int numberOfRetries = retryOperation == null ? 0 : retryOperation.numberOfRetries();
        while ( true ) {
            try {
                method.invoke( instance );
                break;
            } catch ( InvocationTargetException e ) {
                Throwable targetException = e.getTargetException();

                if ( numberOfRetries > 0 && Set.of( retryOperation.retryException() )
                        .contains( targetException.getClass() ) ) {
                    numberOfRetries--;

                    System.out.println( "Retrying ..." );
                    Thread.sleep( retryOperation.durationBetweenRetriesMs() );
                } else if ( retryOperation != null ) {
                    throw new Exception( retryOperation.failureMessage(), targetException );
                } else {
                    throw targetException;
                }
            }
        }
    }

    private static List<Method> getAllInitializingMethods( Class<?> clazz ) {
        List<Method> initializingMethods = new ArrayList<>();

        for ( Method method : clazz.getMethods() ) {
            if ( method.isAnnotationPresent( InitializerMethod.class ) ) {
                initializingMethods.add( method );
            }
        }
        return initializingMethods;
    }

    private static List<Class<?>> getAllClasses( String... packageNames )
            throws URISyntaxException, IOException, ClassNotFoundException {
        List<Class<?>> allClasses = new ArrayList<>();

        for ( String packageName : packageNames ) {
            String packageRelativePath = packageName.replace( ".", "\\" );
            //String packageResourcePath = packageName.split( "\\.", 0 )[ 1 ];

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

        for ( Path filePath : files ) {
            String fileName = filePath.getFileName().toString();

            if ( fileName.endsWith( ".class" ) ) {
                String classFullName = packageName.isBlank() ? fileName.replaceFirst( "\\.class$", "" )
                        : packageName + "." + fileName.replaceFirst( "\\.class$", "" );
                String completeFullClassName = "section7." + classFullName;
                Class<?> clazz = Class.forName( completeFullClassName );
                classes.add( clazz );
            }
        }

        return classes;
    }

}
