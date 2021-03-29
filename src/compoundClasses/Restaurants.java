package compoundClasses;

import interfaces.*;
import simpleClasses.Restaurant;
import jsonparser.JSONParser;

public class Restaurants extends Entity <Restaurant> implements ArrayAndMap {

    public void append(Object o) {
        Restaurant r = (Restaurant) o;

        super.arr.add(r);
        super.idMap.put(r.getID(), r);
    }

    public void init(String filename) {
        JSONParser parser = new JSONParser(filename);
        String ID;

        while ((ID = parser.nextKey()) != null) {
            JSONParser inner = new JSONParser(parser.getJSON(ID));

            Restaurant restaurant = new Restaurant(ID, inner.getString("name"), 
            inner.getString("location"), inner.getInteger("tableCount"));
            
            this.append(restaurant);
        }
    }

    public Restaurants() {
        this.init("src/assets/restaurants.json");
    }

}
