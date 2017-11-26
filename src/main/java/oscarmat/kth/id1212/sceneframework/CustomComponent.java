package oscarmat.kth.id1212.sceneframework;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public abstract class CustomComponent extends AnchorPane {

    /**
     * Create a new GUI component linked to an FXML file.
     * @param path Path to FXML file.
     * @throws IOException If FXML file could not be loaded.
     */
    protected CustomComponent(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

}
