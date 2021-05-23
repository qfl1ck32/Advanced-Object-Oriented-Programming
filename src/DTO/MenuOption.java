package DTO;

import Menu.Menu;

public record MenuOption(String name, boolean requiresAuth, boolean hideOnAuth, Menu subMenu) {}