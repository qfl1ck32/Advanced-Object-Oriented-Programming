package GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import Singletons.Authenticate;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import Singletons.CurrentUser;
import Records.MenuOption;

import java.util.Arrays;
import java.util.HashMap;

public class MainMenu {
    @FXML
    private MenuBar menuBar;
    @FXML
    private Text welcomeMessage;

    private final HashMap <String, Label> labelsByName = new HashMap <> ();
    private final HashMap <String, MenuItem> menuItemByName = new HashMap <> ();

    public void authButtonOnAction(MouseEvent a) {
        boolean shouldLogOut = CurrentUser.getUser().isLoggedIn();

        if (shouldLogOut)
            Authenticate.getInstance().logout();

        ScreenManager.goToStart();
    }

    public void showProducts(MouseEvent a) {
        ScreenManager.showProducts();
    }

    public void putMenuFunction(String name, EventHandler <MouseEvent> func) {
        if (labelsByName.containsKey(name))
            labelsByName.get(name).setOnMouseClicked(func);
    }

    public void putSubmenuFunction(String name, EventHandler <ActionEvent> func) {
        if (menuItemByName.containsKey(name))
            menuItemByName.get(name).setOnAction(func);
    }

    private void setButtonsFunctionalities() {

        for (Menu menu : menuBar.getMenus()) {
            Label menuButton = (Label) menu.getGraphic();
            String menuText = menuButton.getText();

            labelsByName.put(menuText, menuButton);

            if (menu.getItems().size() > 0) {

                for (MenuItem menuItem : menu.getItems()) {
                    String menuItemText = menuItem.getText();
                    menuItemByName.put(menuItemText, menuItem);
                }
            }
        }

        for (String option : Arrays.asList("Back", "Logout"))
            putMenuFunction(option, this :: authButtonOnAction);

        putMenuFunction("Products", this :: showProducts);

    }

    public void initialize() {
        boolean isLoggedIn = CurrentUser.getUser().isLoggedIn();

        Singletons.Menu menu = Singletons.Menu.getInstance();

        for (MenuOption option : menu.getMenuOptions()) {

            if (option.hideOnAuth() && isLoggedIn || option.requiresAuth() && !isLoggedIn)
                continue;

            String optionName = option.name();

            Menu currentMenu = new Menu();
            Label optionLabel = new Label(optionName);
            currentMenu.setGraphic(optionLabel);

            Singletons.Menu submenu = option.subMenu();

            if (submenu != null) {
                for (MenuOption submenuOption : submenu.getMenuOptions()) {
                    String submenuOptionName = submenuOption.name();
                    MenuItem submenuItem = new MenuItem(submenuOptionName);
                    currentMenu.getItems().add(submenuItem);
                }
            }

            menuBar.getMenus().add(currentMenu);
        }

        setButtonsFunctionalities();

        if (isLoggedIn)
            welcomeMessage.setText("Welcome, " + CurrentUser.getUser().getUsername() + "!");

        else
            welcomeMessage.setText("You are not logged in.");
    }
}
