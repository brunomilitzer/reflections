package section3.exercise;

import java.lang.reflect.Array;

public class ArrayReader {


    public Object getArrayElement(Object array, int index) {
        int arrayLength = Array.getLength(array);

        if (index >= 0) {
            return Array.get(array, index);
        } else {
            return Array.get(array, arrayLength + index);
        }
    }
}
