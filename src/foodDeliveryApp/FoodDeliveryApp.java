package FoodDeliveryApp;

import Menu.Menu;

import java.util.concurrent.atomic.AtomicBoolean;

public class FoodDeliveryApp {

    private final String welcomeMessage;
    private final AtomicBoolean showMenu;
    private final Menu menu;

    FoodDeliveryApp() {
        welcomeMessage = "Welcome to Food Delivery!";
        showMenu = new AtomicBoolean(true);
        menu = Menu.getInstance(showMenu);
    }

    public void greet() {
        System.out.println(welcomeMessage);
        System.out.println("-".repeat(welcomeMessage.length()));
    }

    public static void main(String[] args) {
        FoodDeliveryApp myFoodDeliveryApp = new FoodDeliveryApp();

        myFoodDeliveryApp.greet();

        while(myFoodDeliveryApp.showMenu.get()) {
            System.out.println();
            myFoodDeliveryApp.menu.show();
        }

        System.out.println("Goodbye!");
    }
}
