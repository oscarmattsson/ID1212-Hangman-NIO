package oscarmat.kth.id1212.client.view.scene;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import oscarmat.kth.id1212.client.controller.GameController;
import oscarmat.kth.id1212.sceneframework.CustomComponent;

import java.io.IOException;

public class ConnectScene extends CustomComponent {

    private static final String PATH = "/client/components/scenes/Connect.fxml";

    @FXML private Button connectButton;
    @FXML private TextField hostField;
    @FXML private TextField portField;

    private final GameController controller;

    public ConnectScene(GameController controller) throws IOException {
        super(PATH);
        this.controller = controller;
        connectButton.setOnAction(this::connect);

        portField.textProperty().addListener((observable, oldValue, newValue) -> {
            StringProperty property = (StringProperty)observable;
            if(newValue.length() > 0) {
                try {
                    Integer.parseInt(newValue);
                    property.setValue(newValue);
                } catch (NumberFormatException e) {
                    property.setValue(oldValue);
                }
            }
        });
    }

    private void connect(ActionEvent event) {
        String host = hostField.getText();
        int port = Integer.parseInt(portField.getText());
        controller.connect(host, port);
    }
}
