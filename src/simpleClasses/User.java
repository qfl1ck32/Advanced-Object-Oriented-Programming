package simpleClasses;

import java.util.ArrayList;

public class User {
    private String username, password, address;
    private ArrayList <String> deliveries, cart;
    boolean isLoggedIn;

    public User() {
        this.isLoggedIn = false;
    }

    public User(String username, String password, String address, ArrayList <String> deliveries, ArrayList <String> cart, 
        boolean isLoggedIn) {

        this.username = username;
        this.password = password;
        this.address = address;
        this.deliveries = deliveries;
        this.cart = cart;
        this.isLoggedIn = isLoggedIn;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getAddress() {
        return this.address;
    }

    public ArrayList <String> getDeliveries() {
        return this.deliveries;
    }

    public ArrayList <String> getCart() {
        return this.cart;
    }

    public void login() {
        this.isLoggedIn = true;
    }

    public void logout() {
        this.isLoggedIn = false;
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    @Override
    public String toString() {
        return
            "Username: " + username +
            "\nAddress: " + address + "\n";
    }
}