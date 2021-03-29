package compoundClasses;

import interfaces.ArrayAndMap;
import simpleClasses.ProductWithQuantity;
import jsonparser.JSONParser;

public class ProductsWithQuantity extends Entity <ProductWithQuantity> implements ArrayAndMap {
    public void append(Object o) {
        ProductWithQuantity p = (ProductWithQuantity) o;

        super.arr.add(p);
        super.idMap.put(p.getID(), p);
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
