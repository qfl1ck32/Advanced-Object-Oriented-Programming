package GUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import enumerations.SlideType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class Register {
    @FXML
    JFXTextField usernameText, passwordText, emailText;
    @FXML
    JFXButton registerButton, backButton;
    @FXML
    BorderPane messageWrapper;
    @FXML
    private Label message;

    public void onTextChange(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            tryRegister();
            return;
        }

        EffectManager.onTextChange(messageWrapper, message);
    }

    private void shakeOrShowMessage() {
        EffectManager.shakeOrShowMessage(messageWrapper, message);
    }

    private void tryRegister() {
        shakeOrShowMessage();
    }

    public void backButtonOnAction(ActionEvent event) {
        ScreenManager.goBack(SlideType.HORIZONTAL);
    }

    public void registerButtonOnAction(ActionEvent event) {
        tryRegister();
    }

    public void initialize() {
        messageWrapper.setVisible(false);
    }
}
