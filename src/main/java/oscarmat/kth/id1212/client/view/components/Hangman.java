package oscarmat.kth.id1212.client.view.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import oscarmat.kth.id1212.sceneframework.CustomComponent;

import java.io.IOException;

public class Hangman extends CustomComponent {

    private static final String PATH = "/client/components/game/Hangman.fxml";

    @FXML private Label progressLabel;
    @FXML private SplitPane progressSlider;

    private final SplitPane.Divider divider;

    // Clamp progress between 0 and 1.
    private DoubleProperty progressProperty = new SimpleDoubleProperty() {

        @Override
        public void set(double value) {
            if(value < 0) value = 0;
            else if(value > 1) value = 1;
            super.set(value);
        }

        @Override
        public void setValue(Number value) {
            if(value.doubleValue() < 0) value = 0;
            else if(value.doubleValue() > 1) value = 1;
            super.setValue(value);
        }
    };

    public Hangman() throws IOException {
        super(PATH);

        IntegerProperty percentProperty = new SimpleIntegerProperty();
        percentProperty.bind(progressProperty.multiply(100));
        progressLabel.textProperty().bind(percentProperty.asString().concat("%"));

        divider = progressSlider.getDividers().get(0);
        divider.setPosition(0);
        progressProperty.setValue(0);
    }

    /**
     * Updates the progress by animating the slider and updating the numeric
     * representation.
     * @param failedAttempts Amount of failed attempts so far.
     * @param maxAttempts Max amount of allowed attempts.
     */
    public void setProgress(double failedAttempts, double maxAttempts) {
        double value = failedAttempts / maxAttempts;
        animateSlider(value);
        progressProperty.set(value);
    }

    private void animateSlider(double value) {
        DoubleProperty position = divider.positionProperty();
        Timeline slide = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(position, progressProperty.get())),
                new KeyFrame(new Duration(500), new KeyValue(position, value))
        );
        slide.play();
    }
}
