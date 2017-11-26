package oscarmat.kth.id1212.sceneframework;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class SceneManager extends StackPane {

    public SceneManager() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sceneframework/SceneManager.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

    /**
     * Load a new scene with an animated transition.
     * @param scene Scene to load.
     */
    public void loadScene(Node scene) {
        loadScene(scene, opacityProperty(), 0.0, 1.0, 1500);
    }

    private void loadScene(Node scene, final DoubleProperty property, double min, double max, double duration) {
        if(!getChildren().isEmpty()) {
            Timeline fade = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(property, max)),
                    new KeyFrame(new Duration(duration / 2), (ActionEvent event) -> {
                        getChildren().remove(0);
                        getChildren().add(0, scene);
                        Timeline fadeIn = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(property, min)),
                                new KeyFrame(new Duration(duration / 2), new KeyValue(property, max)));
                        fadeIn.play();
                    }, new KeyValue(property, min))
            );
            fade.play();
        }
        else {
            property.setValue(min);
            getChildren().add(scene);
            Timeline fadeIn = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(property, min)),
                    new KeyFrame(new Duration(duration), new KeyValue(property, max)));
            fadeIn.play();
        }
    }
}
