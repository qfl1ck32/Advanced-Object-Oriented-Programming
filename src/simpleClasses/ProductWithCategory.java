package simpleClasses;

import compoundClasses.Products;

public class ProductWithCategory {
    private String ID, name;
    private Products products;

    public ProductWithCategory(String ID, String name, Products products) {
        this.ID = ID;
        this.name = name;
        this.products = products;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Products getProducts() {
        return products;
    }
}
