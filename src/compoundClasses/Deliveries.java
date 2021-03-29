package compoundClasses;

import interfaces.*;
import jsonparser.JSONParser;
import simpleClasses.Delivery;

public class Deliveries extends Entity <Delivery> implements ArrayAndMap {

    public void append(Object o) {
        Delivery d = (Delivery) o;

        super.arr.add(d);
        super.idMap.put(d.getID(), d);
    }

    private void init(String filename) {
        JSONParser parser = new JSONParser(filename);
        String ID;

        while ((ID = parser.nextKey()) != null) {
            JSONParser inner = new JSONParser(parser.getJSON(ID));

            ProductsWithQuantity p = new ProductsWithQuantity(inner.getJSON());
            
            Delivery delivery = new Delivery(ID, p);

            this.append(delivery);
        }
    }

    public Deliveries(String filename) {
        init(filename);
    }
}
