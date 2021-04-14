package FoodDeliveryApp;

import Singletons.Menu;

public class FoodDeliveryApp {

    private static String welcomeMessage;

    private Menu menu;

    FoodDeliveryApp() {
        welcomeMessage = "Welcome to Food Delivery!";
    }

    public void greet() {
        System.out.println(welcomeMessage);
        System.out.println("-".repeat(welcomeMessage.length()));
    }

    public static void main(String[] args) {
        FoodDeliveryApp myFoodDeliveryApp = new FoodDeliveryApp();

        myFoodDeliveryApp.greet();
    }
}
