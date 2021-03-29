package simpleClasses;

public class Restaurant {
    private String ID, name, location;
    private int tablesCount;

    public Restaurant(String ID, String name, String location, int tablesCount) {
        this.ID = ID;
        this.name = name;
        this.location = location;
        this.tablesCount = tablesCount;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getTablesCount() {
        return tablesCount;
    }
}
