package GUI;

import com.jfoenix.controls.JFXButton;

import compoundClasses.Authenticate;

import enumerations.SlideType;
import enumerations.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import enumerations.LoginResponse;

import java.io.IOException;

public class Login {
    @FXML
    private BorderPane container, messageWrapper;
    @FXML
    private JFXButton loginButton, registerButton, backButton;
    @FXML
    private TextField usernameText, passwordText;
    @FXML
    private Label message;

    public void onTextChange(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            tryLogin();
            return;
        }

        EffectManager.onTextChange(messageWrapper, message);
    }

    private void shakeOrShowMessage() {
        EffectManager.shakeOrShowMessage(messageWrapper, message);
    }

    private void tryLogin() throws IOException {
        if (usernameText.getText().length() == 0
                || passwordText.getText().length() == 0) {
            message.setText("Missing credentials.");

            shakeOrShowMessage();
            return;
        }

        LoginResponse loginResponse = Authenticate.getInstance().login(usernameText.getText(), passwordText.getText());

        switch(loginResponse) {
            case NOT_EXISTS:
                message.setText("Account not found.");
                break;
            case WRONG_PASSWORD:
                message.setText("Wrong password.");
                break;
        }

        if (loginResponse == LoginResponse.SUCCESS) {

            if (!ScreenManager.hasScreen(View.MAIN_MENU)) {
                Parent mainMenu = FXMLLoader.load(getClass().getResource("FXML/MainMenu.fxml"));
                ScreenManager.addScreen(View.MAIN_MENU, mainMenu);
            }

            ScreenManager.setScreen(View.MAIN_MENU, SlideType.HORIZONTAL);

            return;
        }

        shakeOrShowMessage();
    }

    public void loginButtonOnAction(ActionEvent event) throws IOException {
        tryLogin();
    }

    public void registerButtonOnAction(ActionEvent event) throws IOException {
        if (!ScreenManager.hasScreen(View.REGISTER)) {
            Parent register = FXMLLoader.load(getClass().getResource("FXML/Register.fxml"));

            ScreenManager.addScreen(View.REGISTER, register);
        }

        ScreenManager.setScreen(View.REGISTER, SlideType.VERTICAL);
    }

    public void backButtonOnAction(ActionEvent event) throws IOException {
        ScreenManager.goBack(SlideType.HORIZONTAL);
    }

    public void initialize() {
        messageWrapper.setVisible(false);
    }
}
