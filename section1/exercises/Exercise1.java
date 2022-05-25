package section1.exercises;

import java.util.*;

public class Exercise1 {
    private static final List<String> JDK_PACKAGE_PREFIXES =
            Arrays.asList("com.sun.", "java", "javax", "jdk", "org.w3c", "org.xml");

    public static void main(String[] args) throws ClassNotFoundException {
        Class<MyInterface> myInterfaceClass = MyInterface.class;

        System.out.println(createPopupTypeInfoFromClass(myInterfaceClass));
    }

    public static PopupTypeInfo createPopupTypeInfoFromClass(Class<?> inputClass) throws ClassNotFoundException {
        PopupTypeInfo popupTypeInfo = new PopupTypeInfo();

        /** Complete the Code **/
        Class<?> primitiveClass = int.class;
        Class<?> interfaceClass = MyInterface.class;
        Class<?> enumClass = ClassType.class;
        Class<?> myClass = MyClass.class;


        popupTypeInfo.setPrimitive(primitiveClass.isPrimitive())
                .setInterface(interfaceClass.isInterface())
                .setEnum(enumClass.isEnum())
                .setName(myClass.getName())
                .setJdk(isJdkClass(inputClass))
                .addAllInheritedClassNames(getAllInheritedClassNames(inputClass));

        return popupTypeInfo;
    }

    /*********** Helper Methods ***************/

    public static boolean isJdkClass(Class<?> inputClass) {
        return JDK_PACKAGE_PREFIXES.stream().anyMatch(packagePrefix -> inputClass.getPackage() == null
                || inputClass.getPackage().getClass().getName().startsWith(packagePrefix));
    }

    public static String[] getAllInheritedClassNames(Class<?> inputClass) {
        if (inputClass.isInterface()) {
            return Arrays.stream(inputClass.getInterfaces())
                    .map(Class::getSimpleName).toArray(String[]::new);
        } else {
            Class<?> inheritedClass = inputClass.getSuperclass();
            return  inheritedClass != null ? new String[] {inputClass.getSuperclass().getSimpleName()} : null;
        }
    }

    private static interface MyInterface {
        boolean isInterface();
    }

    private enum ClassType {
        INTERFACE,
        ENUM,
        CLASS
    }

    private static class MyClass implements MyInterface{

        @Override
        public boolean isInterface() {
            return false;
        }
    }
}