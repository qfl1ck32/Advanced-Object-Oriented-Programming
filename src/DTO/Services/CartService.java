package DTO.Services;

import DTO.Cart;
import DTO.CartProduct;
import Database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CartService implements DTOService <Cart> {

    private static CartService instance;

    private CartService() {}

    public static CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }

        return instance;
    }

    @Override
    public Cart selectByID(String ID) {
        Database database = Database.getInstance();

        String query = "SELECT HEX(product_ID) as ID, " +
                "(SELECT name " +
                "FROM products " +
                "WHERE ID = product_ID) AS name, " +
                "(SELECT price " +
                "FROM products " +
                "WHERE ID = product_ID) AS price, quantity " +
                "FROM cart " +
                "WHERE ID = UNHEX(?);";

        ResultSet answer = database.sendQuery(query, Collections.singletonList(ID));

        List <CartProduct> products = new ArrayList <> ();

        try {
            while (answer.next()) {
                CartProduct currentProduct = new CartProduct(
                    answer.getString("ID"),
                        answer.getString("name"),
                        answer.getDouble("price"),
                        answer.getInt("quantity")
                );

                products.add(currentProduct);
            }
        }

        catch (SQLException exception) {
            throw new RuntimeException("Error when selecting user's cart: " + exception);
        }

        return new Cart(products);
    }

    public void insertProduct(String ID, CartProduct product) {
        Database database = Database.getInstance();

        String checkQuery = "SELECT COUNT(product_ID) AS count " +
                "FROM cart " +
                "WHERE ID = UNHEX(?) " +
                "AND product_ID = UNHEX(?);";

        ResultSet checkExists = database.sendQuery(checkQuery, Arrays.asList(ID, product.ID()));
        boolean exists = false;

        try {
            checkExists.next();
            exists = (checkExists.getInt("count") > 0);
        }

        catch (Exception ignored) {}

        String insertQuery = "";

        if (exists) {
            insertQuery = "UPDATE cart " +
                    "SET quantity = quantity + ? " +
                    "WHERE ID = UNHEX(?) " +
                    "AND product_ID = UNHEX(?);";

            database.sendQuery(insertQuery, Arrays.asList(product.quantity(), ID, product.ID()));
        }

        else {
            insertQuery = "INSERT INTO cart " +
                    "VALUES (UNHEX(?), UNHEX(?), ?);";

            database.sendQuery(insertQuery, Arrays.asList(ID, product.ID(), product.quantity()));
        }
    }

    public void removeProduct(String ID, CartProduct product) {
        Database database = Database.getInstance();

        String query = "DELETE FROM cart " +
                "WHERE ID = UNHEX(?) " +
                "AND product_ID = UNHEX(?);";

        database.sendQuery(query, Arrays.asList(ID, product.ID()));
    }

    public void updateProductQuantity(String ID, CartProduct product, int newQuantity) {
        if (product.quantity() == newQuantity) {
            return;
        }

        Database database = Database.getInstance();

        String query = "UPDATE cart " +
                "SET quantity = ? " +
                "WHERE ID = UNHEX(?) " +
                "AND product_ID = UNHEX(?);";

        database.sendQuery(query, Arrays.asList(newQuantity, ID, product.ID()));
    }
}
