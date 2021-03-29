package simpleClasses;

public class Product {
    String ID, name;
    int price;

    public Product(String ID, String name, int price) {
        this.ID = ID;
        this.name = name;
        this.price = price;
    }

    public String getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    @Override
    public String toString() {
        return 
            "Name: " + this.name +
            "\nPrice: " + this.price;
    }
}
