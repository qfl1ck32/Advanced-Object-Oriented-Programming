package GUI;

import enumerations.SlideType;
import enumerations.View;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScreenManager {
    private static ScreenManager screenManager = null;
    private static HashMap <View, Parent> screens;
    private static Scene mainScene = null;
    private static List <View> history;

    public static ScreenManager getInstance() {
        if (screenManager == null)
            screenManager = new ScreenManager();

        return screenManager;
    }

    private ScreenManager() {
        screens = new HashMap <> ();
        history = new ArrayList <>();
    }

    public static void setMainScene(Parent root) {
        Scene scene = new Scene(root, 640, 480);

        if (mainScene == null) {
            mainScene = scene;
        }
    }

    public static Boolean hasScreen(View v) {
        return screens.containsKey(v);
    }

    public static void setFirstScreen(View v) {
        history.add(v);
        StackPane p = (StackPane) mainScene.getRoot();
        p.getChildren().add(screens.get(v));
    }

    public static void setScreen(View v, SlideType slideType) {
        history.add(v);
        slideTo(screens.get(v), 0.3, slideType);
    }

    public static Scene getMainScene() {
        return mainScene;
    }

    public static void addScreen(View v, Parent p) {
        if (!screens.containsKey(v))
            screens.put(v, p);
    }

    public static void goBack(SlideType slideType) {
        Integer size = history.size();

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

        Double stop;
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

    public static void slideTo(Parent nextScreen, Double duration, SlideType type) {
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
            parentContainer.getChildren().remove(container);
        });
        timeline.play();
    }
}
