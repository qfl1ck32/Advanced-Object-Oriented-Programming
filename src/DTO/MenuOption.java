package DTO;

import Singletons.Menu;

public record MenuOption(String name, boolean requiresAuth, boolean hideOnAuth, Menu subMenu) {}