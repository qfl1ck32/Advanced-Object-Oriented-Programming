package DatabaseMock;

import JSONParser.JSONParser;
import Records.ProductWithQuantity;
import interfaces.IterableAndMappable;

public class ProductsWithQuantity extends WithArrayAndMap<ProductWithQuantity> implements IterableAndMappable {
    public void append(Object o) {
        ProductWithQuantity p = (ProductWithQuantity) o;

        super.arr.add(p);
        super.map.put(p.ID(), p);
    }

    public ProductsWithQuantity(Object JSON) {
        JSONParser parser = new JSONParser(JSON);
        String ID;

        while ((ID = parser.nextKey()) != null) {
            JSONParser inner = new JSONParser(parser.getJSON(ID));

            ProductWithQuantity p = new ProductWithQuantity(inner.getString("ID"),
                    inner.getInteger("quantity"));

            this.append(p);
        }
    }
}
