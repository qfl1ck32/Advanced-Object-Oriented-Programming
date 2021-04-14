package GUI;

import enumerations.SlideType;
import enumerations.View;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScreenManager {
    private static final HashMap <View, Parent> screens = new HashMap <> ();
    private static Scene mainScene = null;
    private static final List <View> history = new ArrayList <> ();

    private static void addScreen(View view, String path) {
        if (!hasScreen(view)) {
            try {
                Parent newScreen = FXMLLoader.load(ScreenManager.class.getResource(path));
                addScreen(view, newScreen);
            }

            catch (IOException exception) {
                // TODO.
                //  mostly, there can't really be any exception, and it'd be non-sense
                //  to try to load another screen that could cause an error to try to show an error in the first place
            }
        }
    }

    public static void setMainScene(Parent root) {
        Scene scene = new Scene(root, 640, 480);

        if (mainScene == null)
            mainScene = scene;
    }

    public static Boolean hasScreen(View v) {
        return screens.containsKey(v);
    }

    public static void setFirstScreen() {
        history.add(View.START);

        StackPane p = (StackPane) mainScene.getRoot();
        addScreen(View.START, "/GUI/FXML/Start.fxml");

        p.getChildren().add(screens.get(View.START));
    }

    public static void setScreen(View v, SlideType slideType) {
        history.add(v);

        if (v == View.START) {
            slideTo(v, 0.25, slideType, true);
            history.clear();
            history.add(v);
            System.out.println(history);
        }

        else
            slideTo(v, 0.25, slideType, false);
    }

    public static Scene getMainScene() {
        return mainScene;
    }

    public static void addScreen(View v, Parent p) {
        if (!screens.containsKey(v))
            screens.put(v, p);
    }

    public static void goBack(SlideType slideType) {
        int size = history.size();

        if (size >= 2) {
            slideFrom(screens.get(history.get(history.size() - 2)), 0.3, slideType);
            history.remove(history.size() - 1);
        }

    }

    public static void slideFrom(Parent nextScreen, Double duration, SlideType type) {
        StackPane parentContainer = (StackPane) mainScene.getRoot();
        BorderPane container = (BorderPane) screens.get(history.get(history.size() - 1));
        parentContainer.getChildren().add(0, nextScreen);

        Timeline timeline = new Timeline();

        double stop;
        DoubleProperty property = null;

        if (type == SlideType.HORIZONTAL) {
            stop = mainScene.getWidth();
            property = container.translateXProperty();
        }

        else {
            stop = mainScene.getHeight();
            property = container.translateYProperty();
        }


        KeyValue kv = new KeyValue(property, stop, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(duration), kv);
        timeline.getKeyFrames().add(kf);

        timeline.setOnFinished(event1 -> {
            parentContainer.getChildren().remove(container);
        });

        timeline.play();
    }

    public static void slideTo(View v, Double duration, SlideType type, boolean shouldDeleteAll) {
        Parent nextScreen = screens.get(v);
        StackPane parentContainer = (StackPane) mainScene.getRoot();
        BorderPane container = (BorderPane) screens.get(history.get(history.size() - 2));

        parentContainer.getChildren().add(nextScreen);

        DoubleProperty property;

        if (type == SlideType.HORIZONTAL) {
            nextScreen.setTranslateX(mainScene.getWidth());
            property = nextScreen.translateXProperty();
        }

        else {
            nextScreen.setTranslateY(mainScene.getHeight());
            property = nextScreen.translateYProperty();
        }

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(property, 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(duration), kv);
        timeline.getKeyFrames().add(kf);

        timeline.setOnFinished(event1 -> {
            if (shouldDeleteAll) {
                parentContainer.getChildren().clear();
                parentContainer.getChildren().add(nextScreen); // or delete everything but last
            }
            else
                parentContainer.getChildren().remove(container);
        });

        timeline.play();
    }

    public static void goToLogin() {
        addScreen(View.LOGIN, "/GUI/FXML/Login.fxml");
        setScreen(View.LOGIN, SlideType.HORIZONTAL);
    }

    public static void goToRegister() {
        addScreen(View.REGISTER, "/GUI/FXML/Register.fxml");
        setScreen(View.REGISTER, SlideType.HORIZONTAL);
    }

    public static void goToMainMenu() {

        addScreen(View.MAIN_MENU, "/GUI/FXML/MainMenu.fxml");
        setScreen(View.MAIN_MENU, SlideType.VERTICAL);

        history.clear();
        history.add(View.MAIN_MENU);
    }

    public static void goToStart() {

        screens.clear();

        addScreen(View.START, "/GUI/FXML/Start.fxml");

        setScreen(View.START, SlideType.HORIZONTAL);

        history.clear();
        history.add(View.START);
    }

    public static void showProducts() {
        StackPane parentContainer = (StackPane) mainScene.getRoot();
        BorderPane mainMenu = (BorderPane) parentContainer.getChildren().get(0);

        BorderPane paneToModify = (BorderPane) mainMenu.getChildren().get(1);

        paneToModify.setCenter(new Text("Test!"));
    }
}
