package simpleClasses;

public class ProductWithQuantity {
    String ID;
    int quantity;

    public ProductWithQuantity(String ID, int quantity) {
        this.ID = ID;
        this.quantity = quantity;
    }

    public String getID() {
        return this.ID;
    }

    public int getQuantity() {
        return this.quantity;
    }
}
