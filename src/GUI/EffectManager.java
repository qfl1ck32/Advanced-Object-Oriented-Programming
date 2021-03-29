package GUI;

import enumerations.FadeType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class EffectManager {
    public static void shakeOrShowMessage(BorderPane messageWrapper, Label message) {
        if (messageWrapper.isVisible()) {
            ShakeTransition transition = new ShakeTransition(Duration.millis(50), message);
            transition.play();
            return;
        }

        FadeTransition fade = new FadeTransition(Duration.millis(100), messageWrapper, FadeType.FADE_IN, false);
        fade.play();
    }

    public static void onTextChange(BorderPane messageWrapper, Label message) {
        if (!messageWrapper.isVisible())
            return;

        FadeTransition fade = new FadeTransition(Duration.millis(100), messageWrapper, FadeType.FADE_OUT, true);
        fade.play();
    }
}
