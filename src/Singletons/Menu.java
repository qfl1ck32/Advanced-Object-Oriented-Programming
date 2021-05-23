package Singletons;

import DTO.*;
import DTO.Services.CartService;
import DTO.Services.ProductsService;
import DTO.Services.TransactionService;
import DTO.Services.UserService;
import JSONParser.JSONParser;
import WithArrayAndMap.WithArrayAndMap;
import enumerations.LoginResponse;
import enumerations.SignupResponse;
import interfaces.IterableAndMappable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class Menu extends WithArrayAndMap<MenuOption> implements IterableAndMappable {

    private final HashMap <String, Menu> submenus;
    private AtomicBoolean showMenu;

    public static Menu menu;

    private int hammingDistance(String str1, String str2) {
        int len1 = str1.length(), len2 = str2.length();

        int answer = abs(len1 - len2);

        for (int i = 0; i < min(len1, len2); ++i) {
            answer += (str1.charAt(i) == str2.charAt(i) ? 0 : 1);
        }

        return answer;
    }

    private String promptBinaryAnswer(String first, String second) {
        Scanner scanner = new Scanner(System.in);
        String answer = "";

        while (!(answer.equalsIgnoreCase(first) || answer.equalsIgnoreCase(second))) {
            System.out.print("> ");
            answer = scanner.nextLine();
        }

        return answer;
    }

    {
        submenus = new HashMap <> ();
    }

    public static Menu getInstance(AtomicBoolean show) {
        if (menu == null) {
            menu = new Menu("src/assets/menu.json", show);
        }

        return menu;
    }

    public void append(Object o) {
        MenuOption mo = (MenuOption) o;

        super.itemsArray.add(mo);
        super.itemsMap.put(mo.name(), mo);
    }

    private void init(String filename, Object JSON, Boolean parentRequiresAuth, Boolean parentRequiresHideOnAuth) {
        JSONParser parser;

        if (filename != null)
            parser = new JSONParser(filename);
        else
            parser = new JSONParser(JSON);

        String name;

        while ((name = parser.nextKey()) != null) {
            JSONParser inner = new JSONParser(parser.getJSON(name));

            Menu submenu = null;

            Boolean requiresAuth, hideOnAuth;

            if (parentRequiresAuth == null) {
                requiresAuth = inner.getBoolean("requiresAuth");
                hideOnAuth = inner.getBoolean("hideOnAuth");
            }

            else {
                requiresAuth = parentRequiresAuth;
                hideOnAuth = parentRequiresHideOnAuth;
            }

            if (inner.hasKey("submenu")) {
                submenu = new Menu(inner.getJSON("submenu"), requiresAuth, hideOnAuth);

                submenus.put(name, submenu);
            }

            MenuOption menuOption = new MenuOption(name, requiresAuth, hideOnAuth, submenu);

            this.append(menuOption);
        }

        this.itemsArray.sort(Comparator.comparing(MenuOption::name));

        if (JSON != null) {
            this.append(new MenuOption("Back", false, false, null));
        }

        else if (filename != null) {
            this.append(new MenuOption("Exit", false, false, null));
        }
    }

    private Menu(String filename, AtomicBoolean show) {
        init(filename, null, null, null);
        this.showMenu = show;
    }

    private Menu(Object JSON, Boolean parentRequiresAuth, Boolean parentRequiresHideOnAuth) {
        init(null, JSON, parentRequiresAuth, parentRequiresHideOnAuth);
    }

    //

    private boolean triesAgain() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n1. Retry\n2. Back");
        System.out.print("> ");

        int choice = scanner.nextInt();

        if (choice > 2 || choice < 1)
            return triesAgain();

        return choice == 1;
    }

    public boolean show() {
        Scanner scanner = new Scanner(System.in);

        int index = 0;

        boolean goBack = false;

        HashMap<Integer, String> mp = new HashMap<>();

        for (MenuOption option : itemsArray) {
            if (CurrentUser.isLoggedIn() && option.hideOnAuth())
                continue;

            if (!CurrentUser.isLoggedIn() && option.requiresAuth())
                continue;

            System.out.println(++index + ". " + option.name());
            mp.put(index, option.name());
        }

        System.out.print("> ");
        MenuOption choice;

        while ((choice = itemsMap.get(mp.get(scanner.nextInt()))) == null
                || CurrentUser.isLoggedIn() && choice.hideOnAuth()
                || !CurrentUser.isLoggedIn() && choice.requiresAuth()) {
            System.out.println("Invalid choice. Please try again.");
            System.out.print("> ");
        }

        System.out.println();
        scanner.nextLine();

        if (submenus.get(choice.name()) != null) {
            boolean goesBack = submenus.get(choice.name()).show();

            if (goesBack) {
                return this.show();
            }
        }

        switch (choice.name().toLowerCase()) {
            case "login" -> handleLogin();
            case "logout" -> UserService.getInstance().logout();
            case "register" -> handleRegister();

            case "show personal data" -> showPersonalData();
            case "modify personal data" -> modifyPersonalData();

            case "products" -> showProducts();
            case "place order" -> placeOrder();

            case "add to cart" -> addToCart();
            case "modify cart" -> modifyCart();
            case "show cart" -> showCart();

            case "show history" -> showHistory(null);
            case "cancel transaction" -> cancelTransaction();

            case "back" -> goBack = true;

            case "exit" -> showMenu.set(false);
        }

        return goBack;
    }

    private void addToCart() {
        List <Product> products = ProductsService.getInstance().getAllProducts();

        Scanner scanner = new Scanner(System.in);

        String productName;
        int quantity;

        System.out.print("Product name: ");
        productName = scanner.nextLine();

        System.out.print("Quantity: ");
        quantity = scanner.nextInt();

        while (quantity < 0) {
            System.out.print("Quantity should be positive: ");
            quantity = scanner.nextInt();
        }

        scanner.nextLine();

        CartProduct mostSimilar = new CartProduct(null, null, 0, 0);

        int hammingDistance = Integer.MAX_VALUE;

        for (Product product: products) {
            String name = product.name();

            int currentHammingDistance = hammingDistance(name, productName);

            if (currentHammingDistance < hammingDistance) {
                hammingDistance = currentHammingDistance;

                mostSimilar = new CartProduct(product.ID(), product.name(), product.price(), quantity);
            }
        }

        if (!mostSimilar.name().equalsIgnoreCase(productName)) {
            System.out.println("Did you mean '" + mostSimilar.name() + "'? [Y/N]");

            String answer = promptBinaryAnswer("Y", "N");

            if (answer.equalsIgnoreCase("N")) {
                return;
            }
        }

        System.out.println("Total cost: " + String.valueOf(quantity) + " * " + String.valueOf(mostSimilar.price())
            + " = " + String.valueOf(quantity * mostSimilar.price()) + "\nIs that alright? [Y/N]:");

        String answer = promptBinaryAnswer("Y", "N");

        if (answer.equalsIgnoreCase("N")) {
            System.out.println("That's okay, we won't place the product in your cart.");
            return;
        }

        CartService.getInstance().insertProduct(CurrentUser.getUser().ID(), mostSimilar);

        System.out.print("Successfully added to cart.");
    }

    private void modifyCart() {
        Cart cart = CartService.getInstance().selectByID(CurrentUser.getUser().ID());

        System.out.println("Index\tProduct name\tQuantity");

        int index = 0;

        for (CartProduct product: cart.products()) {
            String toShow = (index + 1) +
                    "\t\t" +
                    product.name() +
                    "\t\t\t" +
                    product.quantity();

            System.out.println(toShow);

            ++index;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the index of the item you want to modify: ");
        int chosenIndex = -1;

        while (chosenIndex < 1 || chosenIndex > index) {
            System.out.print("> ");
            chosenIndex = scanner.nextInt();
        }

        scanner.nextLine();

        CartProduct chosenProduct = cart.products().get(chosenIndex - 1);

        System.out.println("What action do you want to take - Modify quantity / Remove [M/R]: ");

        String answer = promptBinaryAnswer("M", "R");

        if (answer.equalsIgnoreCase("R")) {
            CartService.getInstance().removeProduct(CurrentUser.getUser().ID(), chosenProduct);
            System.out.println("Successfully removed.");
            return;
        }

        System.out.println("Choose a new quantity:");
        int newQuantity = -1;

        while (newQuantity < 0) {
            System.out.print("> ");
            newQuantity = scanner.nextInt();
        }

        CartService.getInstance().updateProductQuantity(CurrentUser.getUser().ID(), chosenProduct, newQuantity);
        System.out.println("Quantity successfully updated.");
    }

    private void showCart() {
        Cart cart = CartService.getInstance().selectByID(CurrentUser.getUser().ID());

        if (cart.products().isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        double totalCost = 0.0;

        for (CartProduct product: cart.products()) {
            double productPrice = product.price();
            int productQuantity = product.quantity();
            System.out.println("Name: " + product.name());
            System.out.println("Price: " + productPrice);
            System.out.println("Quantity: " + productQuantity);
            System.out.println();

            totalCost += productPrice * productQuantity;
        }

        System.out.println("Total cost: " + totalCost + "\n");
    }

    private void handleLogin() {
        Scanner scanner = new Scanner(System.in);
        String username, password;

        System.out.print("Username: ");
        username = scanner.nextLine();

        System.out.print("Password: ");
        password = scanner.nextLine();

        LoginResponse loginResponse;

        User user = new User(null, username, null, password, null,
                null, null, null, null);

        loginResponse = UserService.getInstance().login(user);

        switch (loginResponse) {
            case SUCCESS -> System.out.println("Successfully logged in.");
            case NOT_EXISTS -> System.out.println("The given username does not exist.");
            case WRONG_PASSWORD -> System.out.println("Wrong password.");
        }

        if (loginResponse != LoginResponse.SUCCESS) {
            if (triesAgain()) {
                System.out.println();
                handleLogin();
            }
        }
    }

    private void handleRegister() {
        Scanner scanner = new Scanner(System.in);
        String greeting = "Register";
        System.out.println(greeting + "\n" + "-".repeat(greeting.length()) + "\n");

        String username, email, password;

        System.out.print("Username: ");
        username = scanner.nextLine();

        System.out.print("Email: ");
        email = scanner.nextLine();

        System.out.print("Password: ");
        password = scanner.nextLine();

        User user = new User(null, username, email, password, null, null, null, null, null);

        SignupResponse response = UserService.getInstance().signup(user);

        switch(response) {
            case USERNAME_ALREADY_USED -> System.out.println("Username is already in use.");
            case EMAIL_ALREADY_USED -> System.out.println("Email is already in use.");
            case SUCCESS -> System.out.println("You have successfully registered!");
            case EMPTY_FIELDS -> System.out.println("Unexpected empty fields.");
        }

        if (response != SignupResponse.SUCCESS) {
            if (triesAgain()) {
                System.out.println();
                handleRegister();
            }
        }
    }

    private void showPersonalData() {
        UserService.getInstance().showAddress(CurrentUser.getUser());
    }

    private void modifyPersonalData() {
        User currentUser = CurrentUser.getUser();

        Scanner scanner = new Scanner(System.in);
        String phoneNumber, country, city, street, postalCode;

        System.out.println("You'll be shown your old data, and be prompted to input a new value for" +
                " every other line. If you don't want to change anything, leave that field blank.\n");

        showPersonalData();

        System.out.print("New phone number: ");
        phoneNumber = scanner.nextLine();
        phoneNumber = phoneNumber.isEmpty() ? currentUser.phoneNumber() : phoneNumber;

        System.out.print("New country: ");
        country = scanner.nextLine();
        country = country.isEmpty() ? currentUser.country() : country;

        System.out.print("New city: ");
        city = scanner.nextLine();
        city = city.isEmpty() ? currentUser.city() : city;

        System.out.print("New street: ");
        street = scanner.nextLine();
        street = street.isEmpty() ? currentUser.street() : street;

        System.out.print("New postal code: ");
        postalCode = scanner.nextLine();
        postalCode = postalCode.isEmpty() ? currentUser.postalCode() : postalCode;

        User newUser = new User(null, null, null, null, phoneNumber, country, city,
                street, postalCode);

        UserService.getInstance().updatePersonalData(newUser);

        System.out.println("Successfully updated your info.");
    }

    private void showProducts() {
        ProductsService products = ProductsService.getInstance();
        List <CategoryWithProducts> categories = products.selectAllCategoriesWithProducts();

        int index = 1;

        for (CategoryWithProducts category : categories) {
            System.out.println(index + ". " + category.name());

            int inner_index = 1;

            for (Product product : category.products()) {
                StringBuilder showBuilder = new StringBuilder()
                        .append("\t")
                        .append(index)
                        .append(".")
                        .append(inner_index++)
                        .append(". ")
                        .append(product.name());

                String ingredients = product.ingredients();

                if (!ingredients.isEmpty()) {
                    showBuilder.append("\n\tIngredients: ")
                            .append(product.ingredients());
                }

                showBuilder.append("\n\tPrice: ")
                        .append(product.price());

                System.out.println(showBuilder.toString());
            }

            System.out.println();
            ++index;
        }
    }

    private void placeOrder() {
        if (!UserService.getInstance().canPlaceOrder(CurrentUser.getUser())) {
            System.out.println("You can't place an order, since you have one ore more missing fields in your" +
                    " 'Personal Data' tab. Please solve this and try again.");

            return;
        }

        Cart currentCart = CartService.getInstance().selectByID(CurrentUser.getUser().ID());

        if (currentCart.products().isEmpty()) {
            System.out.println("You have no products in your cart.");

            return;
        }

        showCart();
        showPersonalData();

        System.out.println("Are you sure you want to place the order? [Y/N]:");
        String answer = promptBinaryAnswer("Y", "N");

        if (answer.equalsIgnoreCase("N")) {
            return;
        }

        TransactionService.getInstance().placeOrder(CurrentUser.getUser().ID());

        System.out.println("Your order was successfully placed. Check your transaction history.\n" +
                "For further inquiries, please contact us.");
    }

    private void showHistory(List <Transaction> transactions) {

        if (transactions == null) {
            transactions = TransactionService.getInstance()
                    .getAllTransactions(CurrentUser.getUser().ID());
        }

        if (transactions.isEmpty()) {
            System.out.println("You have no transactions.");
            return;
        }

        int index = 1;

        for (Transaction transaction : transactions) {
            StringBuilder toShow = new StringBuilder()
                    .append(index)
                    .append(".\n")
                    .append("ID: ")
                    .append(transaction.ID())
                    .append("\nTimestamp: ")
                    .append(transaction.timestamp())
                    .append("\nStatus: ")
                    .append(transaction.status())
                    .append("\nItems: ");

            ++index;

            StringBuilder transactionItems = new StringBuilder();

            for (TransactionItem item : transaction.items()) {
                transactionItems
                        .append(item.quantity())
                        .append(" x ")
                        .append(item.productName());

                if (item != transaction.items().get(transaction.items().size() - 1)) {
                    transactionItems.append(", ");
                }

                else {
                    transactionItems.append(".");
                }
            }

            toShow.append(transactionItems)
                    .append("\nPrice: ")
                    .append(transaction.price());

            System.out.println(toShow);
        }
    }

    private void cancelTransaction() {
        List <Transaction> transactions = TransactionService.getInstance()
                .getAllTransactions(CurrentUser.getUser().ID())
                .stream()
                .filter(transaction -> transaction.status().equalsIgnoreCase("waiting"))
                .collect(Collectors.toList());

        if (transactions.isEmpty()) {
            System.out.println("There are no transactions that are currently pending.");
            showTransactionAlreadyProcessedMessage();

            return;
        }

        showHistory(transactions);

        System.out.println("\nDoes the transaction you wish to cancel appear here? [Y/N]: ");
        String answer = promptBinaryAnswer("Y", "N");

        if (answer.equalsIgnoreCase("N")) {
            System.out.println("In this case, your transaction was probably already processed. ");
            showTransactionAlreadyProcessedMessage();
            return;
        }

        int index = -1;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the transaction you wish to cancel:");

        while (index < 1 || index > transactions.size()) {
            System.out.print("> ");
            index = scanner.nextInt();

            scanner.nextLine();
        }

        boolean wasCanceled = TransactionService.getInstance().cancelTransaction(transactions.get(index - 1).ID());

        if (wasCanceled) {
            System.out.println("Your transaction was successfully canceled.");
        }

        else {
            System.out.println("Your transaction has been processed in the meantime, so we couldn't cancel it.");
            showTransactionAlreadyProcessedMessage();
        }
    }

    private void showTransactionAlreadyProcessedMessage() {
        System.out.println("If you still want to cancel a transaction, please contact us at our phone number.");
    }
}
