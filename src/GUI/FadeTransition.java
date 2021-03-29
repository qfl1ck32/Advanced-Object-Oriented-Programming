package GUI;


import enumerations.FadeType;
import javafx.scene.Node;
import javafx.util.Duration;

public class FadeTransition {
    private javafx.animation.FadeTransition fadeTransition;
    private Node node;

    public FadeTransition(Duration duration, Node node, FadeType type, Boolean hideOnFinish) {
        fadeTransition = new javafx.animation.FadeTransition(duration, node);

        this.node = node;

        if (type == FadeType.FADE_IN) {
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
        }

        else {
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
        }

        if (hideOnFinish)
            fadeTransition.setOnFinished(event -> node.setVisible(false));
    }

    public void play() {
        node.setVisible(true);
        fadeTransition.playFromStart();
    }
}
