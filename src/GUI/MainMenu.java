package GUI;

import enumerations.SlideType;
import enumerations.View;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuButton;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import compoundClasses.Authenticate;

public class MainMenu {
    @FXML
    private MenuButton mainMenu;
    @FXML
    private MenuItem logout;

    public void logoutButtonOnAction(ActionEvent a) {
        Authenticate.getInstance().logout();

        ScreenManager.setScreen(View.START, SlideType.VERTICAL);
    }

    public void initialize() {

    }
}
