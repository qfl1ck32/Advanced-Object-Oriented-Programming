package simpleClasses;

import compoundClasses.ProductsWithQuantity;

public class Delivery {
    private String ID;
    private ProductsWithQuantity products;

    public Delivery(String ID, ProductsWithQuantity products) {
        this.ID = ID;
        this.products = products;
    }

    public String getID() {
        return this.ID;
    }

    public ProductsWithQuantity getProducts() {
        return this.products;
    }
}
