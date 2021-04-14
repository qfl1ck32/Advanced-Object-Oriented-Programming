package DatabaseMock;

import JSONParser.JSONParser;
import Records.Delivery;
import interfaces.IterableAndMappable;

public class Deliveries extends WithArrayAndMap<Delivery> implements IterableAndMappable {

    public void append(Object o) {
        Delivery d = (Delivery) o;

        super.arr.add(d);
        super.map.put(d.ID(), d);
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

    private Deliveries(String filename) {
        init(filename);
    }
}
