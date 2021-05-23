package Singletons;

import DTO.User;

public class CurrentUser {
    private static final User nullUser = new User(null, null, null, null,
            null, null, null, null, null);

    private static User user = nullUser;
    
    public static User getUser() {
        return user;
    }

    public static boolean isLoggedIn() {
        return user != nullUser;
    }

    public static void login(User u) {
        user = u;
    }

    public static void logout() {
        user = nullUser;
    }
}
