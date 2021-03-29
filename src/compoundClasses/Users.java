package compoundClasses;

import interfaces.*;
import simpleClasses.User;
import jsonparser.JSONParser;

public class Users extends Entity <User> implements ArrayAndMap {

    public void append(Object o) {
        User u = (User) o;

        super.arr.add(u);
        super.idMap.put(u.getUsername(), u);
    }

    public void init(String filename) {
        JSONParser parser = new JSONParser(filename);
        String ID;

        while ((ID = parser.nextKey()) != null) {
            JSONParser inner = new JSONParser(parser.getJSON(ID));

            User user = new User(inner.getString("username"), inner.getString("password"), inner.getString("address"),
                inner.getStringArray("deliveryIDS"), inner.getStringArray("cart"), inner.getBoolean("isLoggedIn"));

            this.append(user);
        }
    }

    public Users(String filename) {
        init(filename);
    }
}
