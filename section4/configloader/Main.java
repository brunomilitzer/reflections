package section4.configloader;

import section4.data.GameConfig;
import section4.data.UserInterfaceConfig;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    private static final Path GAME_CONFIG_PATH = Path.of("section4/resources/game-properties.cfg");
    private static final Path USER_CONFIG_PATH = Path.of("section4/resources/user-interface.cfg");

    public static void main(String[] args)
            throws IOException, InvocationTargetException, InstantiationException,
            IllegalAccessException, NoSuchMethodException {
        GameConfig gameConfig = createConfigObject(GameConfig.class, GAME_CONFIG_PATH);
        UserInterfaceConfig userInterfaceConfig = createConfigObject(UserInterfaceConfig.class, USER_CONFIG_PATH);

        System.out.println(gameConfig);
        System.out.println(userInterfaceConfig);
    }

    public static <T> T createConfigObject(Class<T> clazz, Path filePath)
            throws IOException, InvocationTargetException, InstantiationException,
            IllegalAccessException, NoSuchMethodException {
        Scanner scanner = new Scanner(filePath);

        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        T configInstance = (T) constructor.newInstance();

        while (scanner.hasNextLine()) {
            String configLine = scanner.nextLine();
            String[] nameValuePair = configLine.split("=");
            String propertyName = nameValuePair[0];
            String propertyValue = nameValuePair[1];

            Field field;
            try {
                field = clazz.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                System.out.printf("Property name : %s is unsupported\n", propertyName);
                continue;
            }

            field.setAccessible(true);

            Object parseValue = parseValue(field.getType(), propertyValue);
            field.set(configInstance, parseValue);
        }

        return configInstance;
    }

    private static Object parseValue(Class<?> type, String value) {
        if (type.equals(int.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(short.class)) {
            return Short.parseShort(value);
        } else if (type.equals(long.class)) {
            return Long.parseLong(value);
        } else if (type.equals(double.class)) {
            return Double.parseDouble(value);
        } else if (type.equals(float.class)) {
            return Float.parseFloat(value);
        } else if (type.equals(String.class)) {
            return value;
        }

        throw new RuntimeException(String.format("Type : %s unsupported", type.getTypeName()));
    }
}
