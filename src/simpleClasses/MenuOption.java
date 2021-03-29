package simpleClasses;

import compoundClasses.Menu;

public class MenuOption {
    private String name;
    private Boolean requiresAuth, hideOnAuth;
    private Menu submenu;

    public MenuOption (String name, Boolean requiresAuth, Boolean hideOnAuth, Menu submenu) {
        this.name = name;
        this.requiresAuth = requiresAuth;
        this.hideOnAuth = hideOnAuth;
        this.submenu = submenu;
    }

    public String getName() {
        return name;
    }

    public Boolean requiresAuth() {
        return requiresAuth;
    }

    public Boolean hideOnAuth() {
        return hideOnAuth;
    }

    public Menu getSubmenu() {
        return submenu;
    }

    @Override
    public String toString() {
        return
            "Name: " + this.name +
            "\nRequires auth: " + this.requiresAuth +
            "\nHide on auth: " + this.hideOnAuth + "\n";
    }
}
