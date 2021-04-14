package GUI;

import com.jfoenix.controls.JFXButton;

import Singletons.Authenticate;

import enumerations.SlideType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import enumerations.LoginResponse;


public class Login {
    @FXML
    private BorderPane container, messageWrapper;
    @FXML
    private JFXButton loginButton, backButton;
    @FXML
    private TextField usernameText, passwordText;
    @FXML
    private Label message;

    public void onTextChange(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            tryLogin();
            return;
        }

        EffectManager.onTextChange(messageWrapper, message);
    }

    private void shakeOrShowMessage() {
        EffectManager.shakeOrShowMessage(messageWrapper, message);
    }

    private void tryLogin() {
        if (usernameText.getText().length() == 0
                || passwordText.getText().length() == 0) {
            message.setText("Missing credentials.");

            shakeOrShowMessage();
            return;
        }

        LoginResponse loginResponse = Authenticate.getInstance().login(usernameText.getText(), passwordText.getText());

        switch (loginResponse) {
            case NOT_EXISTS -> message.setText("Account not found.");
            case WRONG_PASSWORD -> message.setText("Wrong password.");
        }

        if (loginResponse == LoginResponse.SUCCESS) {

            ScreenManager.goToMainMenu();
            return;
        }

        shakeOrShowMessage();
    }

    public void loginButtonOnAction(ActionEvent event) {
        tryLogin();
    }

    public void backButtonOnAction(ActionEvent event) {
        ScreenManager.goBack(SlideType.HORIZONTAL);
    }

    public void initialize() {
        messageWrapper.setVisible(false);
    }
}
