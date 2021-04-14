package Singletons;

import Records.MenuOption;

import java.util.ArrayList;
import java.util.HashMap;

import DatabaseMock.WithArrayAndMap;
import interfaces.IterableAndMappable;
import JSONParser.JSONParser;

public class Menu extends WithArrayAndMap<MenuOption> implements IterableAndMappable {
    
    private final HashMap <String, Menu> submenus;
    public static Menu menu;

    public static Menu getInstance() {
        if (menu == null) {
            menu = new Menu("src/assets/menu.json");
        }

        return menu;
    }

    {
        submenus = new HashMap <> ();
    }

    public void append(Object o) {
        MenuOption mo = (MenuOption) o;

        super.arr.add(0, mo);
        super.map.put(mo.name(), mo);
    }

    public ArrayList <MenuOption> getMenuOptions() {
        return this.arr;
    }

    private void init(String filename, Object JSON) {
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

    private Menu(String filename) {
        init(filename, null);
    }

    private Menu(Object JSON) {
        init(null, JSON);
    }
}
