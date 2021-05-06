package Singletons;

import DatabaseMock.WithArrayAndMap;
import JSONParser.JSONParser;
import Records.MenuOption;
import Services.UserSignUpService;
import enumerations.LoginResponse;
import enumerations.SignupResponse;
import interfaces.IterableAndMappable;

import java.util.Comparator;
import java.util.HashMap;

public class Menu extends WithArrayAndMap<MenuOption> implements IterableAndMappable {
    
    private final HashMap <String, Menu> submenus;
    public static Menu menu;

    {
        submenus = new HashMap <> ();
    }


    public static Menu getInstance() throws Exception {
        if (menu == null) {
            menu = new Menu("src/assets/menu.json");
        }

        return menu;
    }

    public void append(Object o) {
        MenuOption mo = (MenuOption) o;

        super.itemsArray.add(0, mo);
        super.itemsMap.put(mo.name(), mo);
    }

    private void init(String filename, Object JSON) {
        JSONParser parser;

        if (filename != null)
            parser = new JSONParser(filename);
        else
            parser = new JSONParser(JSON);

        String name;

        while ((name = parser.nextKey()) != null) {
            JSONParser inner = new JSONParser(parser.getJSON(name));

            Menu submenu = null;

            if (inner.hasKey("submenu")) {
                submenu = new Menu(inner.getJSON("submenu"));
                submenus.put(name, submenu);
            }

            MenuOption menuOption = new MenuOption(name, inner.getBoolean("requiresAuth"),
                inner.getBoolean("hideOnAuth"), submenu);

            this.append(menuOption);
        }

        this.itemsArray.sort(Comparator.comparing(MenuOption::name));
    }

    private Menu(String filename) {
        init(filename, null);
    }

    private Menu(Object JSON) {
        init(null, JSON);
    }

    public boolean show() {
        int index = 0;
        boolean returnValue = true;

        HashMap<Integer, String> mp = new HashMap<>();

        for (MenuOption option : itemsArray) {
            if (CurrentUser.getUser().isLoggedIn() && option.hideOnAuth())
                continue;

            if (!CurrentUser.getUser().isLoggedIn() && option.requiresAuth())
                continue;

            System.out.println(++index + ". " + option.name());
            mp.put(index, option.name());
        }

        System.out.print("> ");
        MenuOption choice;

        while ((choice = itemsMap.get(mp.get(scanner.nextInt()))) == null
                || CurrentUser.getUser().isLoggedIn() && choice.hideOnAuth()
                || !CurrentUser.getUser().isLoggedIn() && choice.requiresAuth()) {
            System.out.println("Invalid choice. Please try again.");
            System.out.print("> ");
        }

        System.out.println();
        scanner.nextLine();

        if (submenus.get(choice.name()) != null)
            return submenus.get(choice.name()).show();

        switch (choice.name().toLowerCase()) {
            case "login" -> handleLogin();
            case "logout" -> {
                try {
                    Authenticate.getInstance().logout();
                }
                catch (Exception e) {
                    System.out.println("Couldn't log you out.");
                }
            }
            case "register" -> handleRegister();
            case "exit" -> returnValue = false;
        }

        return returnValue;
    }

    private void handleLogin() {
        String username, password;

        System.out.print("Username: ");
        username = scanner.nextLine();

        System.out.print("Password: ");
        password = scanner.nextLine();

        LoginResponse loginResponse;

        loginResponse = Authenticate.getInstance().login(username, password);

        switch (loginResponse) {
            case SUCCESS -> System.out.println("Successfully logged in.");
            case NOT_EXISTS -> System.out.println("The given username does not exist.");
            case WRONG_PASSWORD -> System.out.println("Wrong password.");
        }
    }

    private void handleRegister() {
        String username, email, password;

        System.out.print("Username: ");
        username = scanner.nextLine();

        System.out.print("Email: ");
        email = scanner.nextLine();

        System.out.print("Password: ");
        password = scanner.nextLine();

        SignupResponse response = UserSignUpService.registerUser(username, email, password);

        switch(response) {
            case USERNAME_ALREADY_USED -> System.out.println("Username is already in use.");
            case EMAIL_ALREADY_USED -> System.out.println("Email is already in use.");
            case SUCCESS -> System.out.println("You have successfully registered!");
            case EMPTY_FIELDS -> System.out.println("Unexpected  empty fields.");
        }
    }
}
