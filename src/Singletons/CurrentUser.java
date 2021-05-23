package Singletons;

import DTO.User;

public class CurrentUser {
    private static User user = null;
    
    public static User getUser() {
        return user;
    }

    public static boolean isLoggedIn() {
        return user != null;
    }

    public static void login(User u) {
        user = u;
    }

    public static void logout() {
        user = null;
    }
}
