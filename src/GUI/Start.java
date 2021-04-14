package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Start implements Initializable {
    @FXML
    private JFXButton login, register, enterAsGuest;
    @FXML
    private BorderPane container;

    @FXML
    private void openLogin(ActionEvent event) {
        ScreenManager.goToLogin();
    }

    @FXML
    private void openRegister(ActionEvent event) {
        ScreenManager.goToRegister();
    }

    @FXML
    private void openMainMenuAsGuest(ActionEvent event) {
        ScreenManager.goToMainMenu();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
