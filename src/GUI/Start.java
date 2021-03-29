package GUI;

import enumerations.SlideType;
import enumerations.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Start implements Initializable {
    @FXML
    private JFXButton getStartedButton;
    @FXML
    private BorderPane container;

    @FXML
    private void openLogin(ActionEvent event) throws IOException {
        if (!ScreenManager.hasScreen(View.LOGIN)) {
            Parent login = FXMLLoader.load(getClass().getResource("/GUI/FXML/Login.fxml"));
            ScreenManager.addScreen(View.LOGIN, login);
        }

        ScreenManager.setScreen(View.LOGIN, SlideType.HORIZONTAL);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
