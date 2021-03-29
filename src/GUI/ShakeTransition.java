package GUI;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class ShakeTransition {
    private TranslateTransition translateTransition;

    public ShakeTransition(Duration duration, Node node) {
        translateTransition = new TranslateTransition(duration, node);

        translateTransition.setFromX(0f);
        translateTransition.setByX(10f);
        translateTransition.setCycleCount(4);
        translateTransition.setAutoReverse(true);
    }

    public void play() {
        translateTransition.playFromStart();
    }
}
