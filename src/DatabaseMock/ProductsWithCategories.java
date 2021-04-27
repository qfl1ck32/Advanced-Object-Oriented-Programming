package DatabaseMock;

import JSONParser.JSONParser;
import Records.Product;
import Records.ProductWithCategory;
import interfaces.IterableAndMappable;

import java.util.HashMap;

public class ProductsWithCategories extends WithArrayAndMap<ProductWithCategory> implements IterableAndMappable {
    private final HashMap<String, Product> productByID = new HashMap<>();
    private final HashMap<String, String> categoryByProductID = new HashMap<>();
    private final HashMap<Integer, ProductWithCategory> productsByIndex = new HashMap<>();
    private Integer index = 0;

    public void append(Object o) {
        ProductWithCategory p = (ProductWithCategory) o;

        for (Product pr : p.products().itemsArray) {
            productByID.put(pr.ID(), pr);
            categoryByProductID.put(pr.ID(), p.name());
        }

        productsByIndex.put(++index, p);

        super.itemsArray.add(0, p);
        super.itemsMap.put(p.name(), p);
    }

    private void init(String filename) {
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
}
