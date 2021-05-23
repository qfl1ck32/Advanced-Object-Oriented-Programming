package DTO.Services;

import DTO.User;
import Database.Database;
import Singletons.CurrentUser;
import enumerations.LoginResponse;
import enumerations.SignupResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;

import Audit.Audit;

public class UserService implements DTOService <User> {

    private static UserService instance;

    private UserService() {}

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }

        return instance;
    }

    @Override
    public User selectByID(String ID) {
        Database database = Database.getInstance();

        String query = "SELECT username, email, password, phone_number, country, city, street, postal_code" +
                " FROM users" +
                " WHERE ID = UNHEX(?);";

        ResultSet answer = database.sendQuery(query, Collections.singletonList(ID));

        User user;

        try {
            answer.next();

            user = new User(ID,
                    answer.getString("username"),
                    answer.getString("email"),
                    null,
                    answer.getString("phone_number"),
                    answer.getString("country"),
                    answer.getString("city"),
                    answer.getString("street"),
                    answer.getString("postal_code"));
        }

        catch (SQLException exception) {
            throw new RuntimeException("Error when selecting user: " + exception);
        }

        return user;
    }

    public void insert(User o) {
        Database database = Database.getInstance();

        String query = "INSERT INTO users (ID, username, email, password)" +
                " VALUES (UNHEX(REPLACE(?, '-', '')), ?, ?, ?);";

        database.sendQuery(query, Arrays.asList(o.ID(), o.username(), o.email(), o.password()));
    }

    public SignupResponse signup(User o) {
        if (Stream.of(o.username(), o.email(), o.password()).anyMatch(String :: isEmpty)) {
            return SignupResponse.EMPTY_FIELDS;
        }

        Database database = Database.getInstance();

        String query = "SELECT (" +
                "SELECT COUNT(ID)" +
                " FROM users" +
                " WHERE username = ?) AS countUsernames, (" +
                "SELECT COUNT(ID)" +
                " FROM users" +
                " WHERE email = ?) AS countEmails" +
                " FROM dual;";

        ResultSet answer = database.sendQuery(query, Arrays.asList(o.username(), o.email()));

        try {
            answer.next();

            if (answer.getInt("countUsernames") > 0) {
                return SignupResponse.USERNAME_ALREADY_USED;
            }

            if (answer.getInt("countEmails") > 0) {
                return SignupResponse.EMAIL_ALREADY_USED;
            }
        }

        catch (SQLException exception) {
            System.out.println("Exception caught in signup: " + exception);
        }

        String ID = UUID.randomUUID().toString();

        insert(new User(ID, o.username(), o.email(), o.password(),
                o.phoneNumber(), o.country(), o.city(), o.street(), o.postalCode()));

        Audit.insertLog(ID, "Register");

        return SignupResponse.SUCCESS;
    }

    public LoginResponse login(User o)  {
        String query = "SELECT HEX(ID) as ID, password " +
                "FROM users " +
                "WHERE username = ?;";

        ResultSet users = Database.getInstance().sendQuery(query, Collections.singletonList(o.username()));
        String ID;

        try {
            if (!users.next()) {
                return LoginResponse.NOT_EXISTS;
            }

            if (!users.getString("password").equals(o.password()))
                return LoginResponse.WRONG_PASSWORD;

            ID = users.getString("ID");
        }

        catch (SQLException exception) {
            throw new RuntimeException("Couldn't login: " + exception);
        }

        CurrentUser.login(UserService.getInstance().selectByID(ID));

        Audit.insertLog(ID, "Log in");

        return LoginResponse.SUCCESS;
    }

    public void logout() {
        Audit.insertLog(CurrentUser.getUser().ID(), "Logout");
        CurrentUser.logout();
    }

    public void updatePersonalData(User u) {
        Database database = Database.getInstance();
        User currentUser = CurrentUser.getUser();

        String query = "UPDATE users" +
                " SET phone_number = ?, country = ?, city = ?, street = ?, postal_code = ?" +
                " WHERE ID = UNHEX(?);";

        database.sendQuery(query, Arrays.asList(u.phoneNumber(), u.country(), u.city(), u.street(), u.postalCode(),
                currentUser.ID()));

        User newUser = new User(currentUser.ID(), currentUser.username(), currentUser.email(), currentUser.password(),
                u.phoneNumber(), u.country(), u.city(), u.street(), u.postalCode());

        CurrentUser.login(newUser);

        Audit.insertLog(u.ID(), "Update personal data");
    }

    ///

    public String getString(String string) {
        if (string.isEmpty())
            return "-";

        return string;
    }

    public boolean canPlaceOrder(User u) {
        return Stream.of(u.phoneNumber(), u.country(), u.city(), u.street(), u.postalCode()).noneMatch(String::isEmpty);
    }

    public void showAddress(User u) {

        String toShow = "Phone number: " +
                getString(u.phoneNumber()) +
                "\nCountry: " +
                getString(u.country()) +
                "\nCity: " +
                getString(u.city()) +
                "\nStreet: " +
                getString(u.street()) +
                "\nPostal code: " +
                getString(u.postalCode()) +
                "\n";

        System.out.println(toShow);
    }
}
