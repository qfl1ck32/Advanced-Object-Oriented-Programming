package simpleClasses;

public class CurrentUser {
    private static User user = null;
    
    public static User getUser() {
        if (user == null) {
            user = new User();
        }

        return user;
    }

    public static void setUser(User u) {
        if (user == null)
            user = new User();

        user = u;
    }
}
