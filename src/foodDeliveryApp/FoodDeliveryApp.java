package FoodDeliveryApp;

import Singletons.Menu;

public class FoodDeliveryApp {

    private final String welcomeMessage;
    private Menu menu = null;

    FoodDeliveryApp() {
        welcomeMessage = "Welcome to Food Delivery!";

        try {
            menu = Menu.getInstance();
        }

        catch (Exception err) {
            System.out.println("Couldn't load Menu data.");
        }
    }

    public void greet() {
        System.out.println(welcomeMessage);
        System.out.println("-".repeat(welcomeMessage.length()));
    }

    public static void main(String[] args) {
        FoodDeliveryApp myFoodDeliveryApp = new FoodDeliveryApp();

        myFoodDeliveryApp.greet();

        while(myFoodDeliveryApp.menu.show()) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}