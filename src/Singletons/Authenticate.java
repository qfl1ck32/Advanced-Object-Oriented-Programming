package Singletons;

import java.util.Scanner;

import DatabaseMock.Users;
import enumerations.LoginResponse;
import Records.*;

public class Authenticate {
    private static Authenticate instance;
    private final Users users;
    private static Scanner in;

    public static Authenticate getInstance() {
        if (instance == null) {
            in = new Scanner(System.in);
            instance = new Authenticate();
        }

        return instance;
    }

    private Authenticate() {
        users = new Users("src/assets/users.json");
    }

    public LoginResponse login(String username, String password) {
        User u = users.find(username);

        if (u == null)
            return LoginResponse.NOT_EXISTS;

        if (!u.getPassword().equals(password))
            return LoginResponse.WRONG_PASSWORD;

        u.login();

        CurrentUser.setUser(u);

        return LoginResponse.SUCCESS;


    }

    public void logout() {
        CurrentUser.getUser().logout();

        CurrentUser.setUser(null);
    }
}
