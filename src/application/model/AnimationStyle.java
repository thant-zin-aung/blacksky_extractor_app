package application.model;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;


public class AnimationStyle {
//    private double duration;
//    private boolean setToAutoReverse;
//    private int setCycleCount;
//
//    private AnimationStyle() {
//        this.duration = 1000;
//        setToAutoReverse = true;
//        setCycleCount = 1;
//    }

    private static AnimationStyle instance = new AnimationStyle();

    public static AnimationStyle getInstance() {
        return instance;
    }

    public void playScaleEffect (Node node, double duration, int setCycleCount, boolean setToAutoReverse,
                                        double setFromX, double setFromY, double setToX, double setToY) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(duration),node);
        scaleTransition.setFromX(setFromX);
        scaleTransition.setFromY(setFromY);
        scaleTransition.setToX(setToX);
        scaleTransition.setToY(setToY);
        scaleTransition.setAutoReverse(setToAutoReverse);
        scaleTransition.setCycleCount(setCycleCount);
        scaleTransition.play();
    }

    public void playFadeEffect (Node node, double duration, int setCycleCount, boolean setToAutoReverse,
                                double setFromValue, double setToValue) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration),node);
        fadeTransition.setFromValue(setFromValue);
        fadeTransition.setToValue(setToValue);
        fadeTransition.setAutoReverse(setToAutoReverse);
        fadeTransition.setCycleCount(setCycleCount);
        fadeTransition.play();
    }
}
