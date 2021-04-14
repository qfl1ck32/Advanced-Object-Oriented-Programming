package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class gui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent mainStack = FXMLLoader.load(getClass().getResource("FXML/MainStack.fxml"));

        ScreenManager.setMainScene(mainStack);

        ScreenManager.setFirstScreen();

        primaryStage.setTitle("myFoodDelivery");
        primaryStage.setScene(ScreenManager.getMainScene());
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.show();
    }
}