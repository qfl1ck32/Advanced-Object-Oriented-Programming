package compoundClasses;

import simpleClasses.MenuOption;
import simpleClasses.CurrentProducts;
import simpleClasses.CurrentUser;

import java.util.HashMap;

import interfaces.ArrayAndMap;
import jsonparser.JSONParser;

public class Menu extends Entity <MenuOption> implements ArrayAndMap {
    
    private HashMap <String, Menu> submenus;

    {
        submenus = new HashMap <> ();
    }

    public void append(Object o) {
        MenuOption mo = (MenuOption) o;

        super.arr.add(0, mo);
        super.idMap.put(mo.getName(), mo);
    }

    public void init(String filename, Object JSON) {
        JSONParser parser = null;

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
    }

    public Menu(String filename) {
        init(filename, null);
    }

    public Menu(Object JSON) {
        init(null, JSON);
    }

    public void show() {
        int index = 0;

        HashMap <Integer, String> mp = new HashMap <> ();

        for (MenuOption option : arr) {
            if (CurrentUser.getUser().isLoggedIn() && option.hideOnAuth())
                continue;
            
            if (!CurrentUser.getUser().isLoggedIn() && option.requiresAuth())
                continue;

            System.out.println(++index + ". " + option.getName());
            mp.put(index, option.getName());
        }

        System.out.print("> ");
        MenuOption choice;

        while ((choice = idMap.get(mp.get(in.nextInt()))) == null || CurrentUser.getUser().isLoggedIn() && choice.hideOnAuth() || !CurrentUser.getUser().isLoggedIn() && choice.requiresAuth()) {
            System.out.println("Invalid choice. Please try again.");
            System.out.print("> ");
        }

        System.out.println();

        if (submenus.get(choice.getName()) != null) {
            submenus.get(choice.getName()).show();
            return;
        }

        switch(choice.getName().toLowerCase()) {
            case "login":

                break;

            case "logout":
                Authenticate.getInstance().logout();
                break;

            case "register":
                System.out.println("N-avem, inca");
                break;

            case "products":
                CurrentProducts.getProducts().show();
                break;
        }
    }
}
