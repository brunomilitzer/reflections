package section4.exercise;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayFlattening {

    public static void main(String[] args) {
        Integer[] result = concat(Integer.class, 1, 2, 3, 4, 5);

        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
        }
    }

    public static  <T> T concat(Class<?> type, Object... arguments) {

        if (arguments.length == 0) {
            return null;
        }

        List<Object> elements = new ArrayList<>();

        for (Object argument : arguments) {
            if (argument.getClass().isArray()) {
                int length = Array.getLength(argument);

                for (int i = 0; i < length; i++) {
                    elements.add(Array.get(argument, i));
                }
            } else {
                elements.add(argument);
            }
        }

        Object flattenArray = Array.newInstance(type, elements.size());

        for (int i = 0; i < elements.size(); i++) {
            Array.set(flattenArray, i, elements.get(i));
        }

        return (T) flattenArray;
    }
}
