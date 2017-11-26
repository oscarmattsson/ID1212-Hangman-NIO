package oscarmat.kth.id1212.client.view.scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import oscarmat.kth.id1212.client.controller.GameController;
import oscarmat.kth.id1212.sceneframework.CustomComponent;

import java.io.IOException;

public class WelcomeScene extends CustomComponent {

    private static final String PATH = "/client/components/scenes/Welcome.fxml";

    @FXML private Button startButton;
    @FXML private TextField aliasField;

    private final GameController controller;

    public WelcomeScene(GameController controller) throws IOException {
        super(PATH);
        this.controller = controller;

        startButton.setOnAction(this::onStartAction);
    }

    private void onStartAction(ActionEvent event) {
        controller.setAlias(aliasField.getText());
    }
}
