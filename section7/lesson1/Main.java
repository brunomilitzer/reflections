package section7.lesson1;

import section7.lesson1.annotations.InitializerClass;
import section7.lesson1.annotations.InitializerMethod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Throwable {
        initialize("app");
    }

    public static void initialize(String ... packageNames)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException,
            IllegalAccessException, URISyntaxException, IOException, ClassNotFoundException {

        List<Class<?>> classes = getAllClasses(packageNames);

        for (Class<?> clazz : classes) {
            if (!clazz.isAnnotationPresent(InitializerClass.class)) {
                continue;
            }

            List<Method> methods = getAllInitializingMethods(clazz);

            Object instance = clazz.getDeclaredConstructor().newInstance();

            for (Method method : methods) {
                method.invoke(instance);
            }
        }
    }

    private static List<Method> getAllInitializingMethods(Class<?> clazz) {
        List<Method> initializingMethods = new ArrayList<>();

        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(InitializerMethod.class)) {
                initializingMethods.add(method);
            }
        }
        return initializingMethods;
    }

    private static List<Class<?>> getAllClasses(String... packageNames)
            throws URISyntaxException, IOException, ClassNotFoundException {
        List<Class<?>> allClasses = new ArrayList<>();

        for (String packageName : packageNames) {
            String packageRelativePath = packageName.replace(".", "/");

            URI packageUri = Main.class.getResource(packageRelativePath).toURI();

            if (packageUri.getScheme().equals("file")) {
                Path packageFullPath = Paths.get(packageUri);
                allClasses.addAll(getAllPackagesClasses(packageFullPath, packageName));
            } else if (packageUri.getScheme().equals("jar")) {
                FileSystem fileSystem = FileSystems.newFileSystem(packageUri, Collections.emptyMap());

                Path packageFullPathInJar = fileSystem.getPath(packageRelativePath);
                allClasses.addAll(getAllPackagesClasses(packageFullPathInJar, packageName));

                fileSystem.close();
            }
        }

        return allClasses;
    }

    private static List<Class<?>> getAllPackagesClasses(Path packagePath, String packageName)
            throws IOException, ClassNotFoundException {

        if (!Files.exists(packagePath)) {
            return Collections.emptyList();
        }

        List<Path> files = Files.list(packagePath).filter(Files::isRegularFile).toList();

        List<Class<?>> classes = new ArrayList<>();

        for (Path filePath : files) {
            String fileName = filePath.getFileName().toString();

            if (fileName.endsWith(".class")) {
                String classFullName = fileName.replaceFirst("\\.class", "");
                Class<?> clazz = Class.forName(classFullName);
                classes.add(clazz);
            }
        }

        return classes;
    }

}
