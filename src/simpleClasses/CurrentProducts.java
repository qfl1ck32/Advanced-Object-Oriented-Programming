package simpleClasses;

import compoundClasses.ProductsWithCategories;

public class CurrentProducts {
    private static ProductsWithCategories products;
    private static String filename = "src/assets/productswithcategories.json";

    public static ProductsWithCategories getProducts() {
        if (products == null)
            products = new ProductsWithCategories(filename);

        return products;
    }
}
