package section3.lesson2.data;

public class Person {

    private final String name;
    private final boolean employeed;
    private final int age;
    private final float salary;
    private final Address address;
    private final Company job;

    public Person(String name, boolean employeed, int age, float salary, Address address, Company job) {
        this.name = name;
        this.employeed = employeed;
        this.age = age;
        this.salary = salary;
        this.address = address;
        this.job = job;
    }
}
