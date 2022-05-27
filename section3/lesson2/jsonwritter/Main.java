package section3.lesson2.jsonwritter;

import section3.lesson2.data.Address;
import section3.lesson2.data.Company;
import section3.lesson2.data.Person;

import java.lang.reflect.Field;

public class Main {

    public static void main(String[] args) throws IllegalAccessException {

        Company company = new Company("Udemy", "San Francisco",
                new Address("Harrison Street", (short) 600));
        Address address = new Address("Main Street", (short) 1);
        Person person = new Person("Vanessa", true, 42, 100.555f, address, company);
        String json = objectToJson(person, 0);

        System.out.println(json);

    }

    public static String objectToJson(Object instance, int indentSize) throws IllegalAccessException {
        Field [] fields = instance.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append(indent(indentSize));
        sb.append("{");
        sb.append("\n");


        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            if (field.isSynthetic()) continue;

            sb.append(indent(indentSize + 1));
            sb.append(formatStringValue(field.getName()));
            sb.append(":");

            if (field.getType().isPrimitive()) {
                sb.append(formatPrimitiveValue(field, instance));
            } else if (field.getType().equals(String.class)) {
                sb.append(formatStringValue(field.get(instance).toString()));
            } else {
                sb.append(objectToJson(field.get(instance), indentSize + 1));
            }

            if (i != fields.length - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append(indent(indentSize));
        sb.append("}");
        return sb.toString();
    }

    private static String indent(int indentSize) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indentSize; i++) {
            sb.append("\t");
        }

        return sb.toString();
    }

    private static String formatPrimitiveValue(Field field, Object parentInstance) throws IllegalAccessException {
        if (field.getType().equals(boolean.class)
        || field.getType().equals(int.class)
        || field.getType().equals(short.class)) {
            return field.get(parentInstance).toString();
        } else if (field.getType().equals(double.class) || field.getType().equals(float.class)) {
            return String.format("%.02f", field.get(parentInstance));
        }

        throw new RuntimeException(String.format("Type : %s is unsupported", field.getType().getName()));
    }

    private static String formatStringValue(String value) {
        return String.format("\"%s\"", value);
    }
}
