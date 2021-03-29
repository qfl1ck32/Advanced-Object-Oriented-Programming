package compoundClasses;

import interfaces.*;
import jsonparser.JSONParser;
import simpleClasses.Driver;

public class Drivers extends Entity <Driver> implements ArrayAndMap {
    public void append(Object o) {
        Driver d = (Driver) o;
        super.arr.add(d);
        super.idMap.put(d.getName(), d);
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
