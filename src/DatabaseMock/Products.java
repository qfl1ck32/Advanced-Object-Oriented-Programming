package DatabaseMock;

import JSONParser.JSONParser;
import Records.Product;
import interfaces.IterableAndMappable;

public class Products extends WithArrayAndMap<Product> implements IterableAndMappable {

    public void append(Object o) {
        Product p = (Product) o;

        super.arr.add(p);
        super.map.put(p.ID(), p);
    }

    public Products(Object JSON) {
        JSONParser parser = new JSONParser(JSON);
        String ID;

        while ((ID = parser.nextKey()) != null) {
            JSONParser inner = new JSONParser(parser.getJSON(ID));

            Product p = new Product(ID, inner.getString("name"),
                    inner.getInteger("price"));

            this.append(p);
        }
    }
}
