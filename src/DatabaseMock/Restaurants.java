package DatabaseMock;

import JSONParser.JSONParser;
import Records.Restaurant;
import interfaces.IterableAndMappable;

public class Restaurants extends WithArrayAndMap<Restaurant> implements IterableAndMappable {

    public void append(Object o) {
        Restaurant r = (Restaurant) o;

        super.itemsArray.add(r);
        super.itemsMap.put(r.ID(), r);
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
