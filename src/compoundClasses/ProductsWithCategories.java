package compoundClasses;

import interfaces.*;
import simpleClasses.Product;
import simpleClasses.ProductWithCategory;
import jsonparser.JSONParser;
import java.util.HashMap;

public class ProductsWithCategories extends Entity <ProductWithCategory> implements ArrayAndMap {
    private HashMap <String, Product> productByID = new HashMap <> ();
    private HashMap <String, String> categoryByProductID = new HashMap <> ();
    private HashMap <Integer, ProductWithCategory> productsByIndex = new HashMap <> ();
    private Integer index = 0;

    public void append(Object o) {
        ProductWithCategory p = (ProductWithCategory) o;

        for (Product pr : p.getProducts().arr) {
            productByID.put(pr.getID(), pr);
            categoryByProductID.put(pr.getID(), p.getName());
        }

        productsByIndex.put(++index, p);

        super.arr.add(0, p);
        super.idMap.put(p.getName(), p);
    }

    public void init(String filename) {
        JSONParser parser = new JSONParser(filename);
        String ID;

        while ((ID = parser.nextKey()) != null) {
            JSONParser inner = new JSONParser(parser.getJSON(ID));

            Products products = new Products(inner.getJSON("products"));

            ProductWithCategory p = new ProductWithCategory(ID, inner.getString("name"), products);

            this.append(p);
        }
    }

    public ProductsWithCategories(String filename) {
        init(filename);
    }

    public void show() {
        Integer index = 0;

        for (ProductWithCategory pwc : arr) {
            String text = (++index).toString() + ". " + pwc.getName();
            System.out.println(text + "\n" + "-".repeat(text.length()));

            for (Product p : pwc.getProducts().arr) {
                Integer innerIndex = 0;
                StringBuilder innerText = new StringBuilder();
                innerText.append(index + "." + ++innerIndex + "\n\t");
                innerText.append("Product name:\t" + p.getName() + "\n\t");
                innerText.append("Price:\t\t" + "$" + p.getPrice());
                System.out.println(innerText);
            }

            System.out.println();
        }
    }
}
