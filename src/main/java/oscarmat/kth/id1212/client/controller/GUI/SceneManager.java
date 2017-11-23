package oscarmat.kth.id1212.client.controller.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages transitions between available scenes.
 */
public class SceneManager extends StackPane {

    private Map<String, Node> scenes = new HashMap<>();

    public SceneManager() {
        super();
    }

    public void addScene(String name, Node scene) {

    }
}
