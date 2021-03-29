package compoundClasses;

import interfaces.ArrayAndMap;
import simpleClasses.Product;
import jsonparser.JSONParser;

public class Products extends Entity <Product> implements ArrayAndMap {

    public void append(Object o) {
        Product p = (Product) o;

        super.arr.add(p);
        super.idMap.put(p.getID(), p);
    }

    public Products(Object JSON) {
        JSONParser parser = new JSONParser(JSON);
        String ID;

        while ((ID = parser.nextKey()) !=  null) {
            JSONParser inner = new JSONParser(parser.getJSON(ID));

            Product p = new Product(ID, inner.getString("name"),
                inner.getInteger("price"));

            this.append(p);
        }
    }
}
