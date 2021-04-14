package GUI;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class gui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        if (!ScreenManager.init()) {
            System.out.println("Couldn't load the main screen.");
            return;
        }

        primaryStage.setTitle("myFoodDelivery");
        primaryStage.setScene(ScreenManager.getMainScene());
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.show();
    }
}