package simpleClasses;

public class Driver {
    private String ID;
    private String name;
    private int age;

    public Driver(String ID, String name, int age) {
        this.ID = ID;
        this.name = name;
        this.age = age;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
}
