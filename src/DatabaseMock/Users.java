package DatabaseMock;

import JSONParser.JSONParser;
import Records.User;
import interfaces.IterableAndMappable;

public class Users extends WithArrayAndMap<User> implements IterableAndMappable {

    public void append(Object o) {
        User u = (User) o;

        super.arr.add(u);
        super.map.put(u.getUsername(), u);
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

    public User find(String username) {
        return super.map.get(username);
    }

    public Users(String filename) {
        init(filename);
    }
}