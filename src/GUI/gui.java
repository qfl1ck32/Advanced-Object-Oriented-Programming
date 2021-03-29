package GUI;

import enumerations.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class gui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent  mainStack = FXMLLoader.load(getClass().getResource("FXML/MainStack.fxml")),
                start = FXMLLoader.load(getClass().getResource("FXML/Start.fxml"));

        ScreenManager.getInstance().setMainScene(mainStack);

        ScreenManager.addScreen(View.START, start);
        ScreenManager.setFirstScreen(View.START);

        primaryStage.setTitle("myFoodDelivery");
        primaryStage.setScene(ScreenManager.getMainScene());
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.show();
    }



    public static void goBack(StackPane parentContainer) {
        parentContainer.getChildren().remove(1);
    }
}
