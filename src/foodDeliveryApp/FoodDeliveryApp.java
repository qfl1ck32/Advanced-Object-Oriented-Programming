package foodDeliveryApp;

import compoundClasses.Menu;

public class FoodDeliveryApp {

    private static String welcomeMessage;

    private Menu menu;

    FoodDeliveryApp() {
        welcomeMessage = "Welcome to Food Delivery!";

        menu = new Menu("src/assets/menu.json");
    }

    public static void main(String[] args) {
        FoodDeliveryApp food = new FoodDeliveryApp();

         System.out.println(welcomeMessage);
         System.out.println("-".repeat(welcomeMessage.length()));

         while (true) {
             food.menu.show();
             System.out.println();
         }
    }
}
