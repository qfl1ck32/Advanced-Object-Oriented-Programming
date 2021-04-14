package DatabaseMock;

import JSONParser.JSONParser;
import Records.Driver;
import interfaces.IterableAndMappable;

public class Drivers extends WithArrayAndMap<Driver> implements IterableAndMappable {
    public void append(Object o) {
        Driver d = (Driver) o;
        super.arr.add(d);
        super.map.put(d.name(), d);
    }

    public void init(String filename) {
        JSONParser parser = new JSONParser(filename);
        String ID;

        while ((ID = parser.nextKey()) != null) {
            JSONParser inner = new JSONParser(parser.getJSON(ID));

            Driver driver = new Driver(ID, inner.getString("name"), inner.getInteger("age"));

            this.append(driver);
        }
    }

    public Drivers(String filename) {
        init(filename);
    }
}
