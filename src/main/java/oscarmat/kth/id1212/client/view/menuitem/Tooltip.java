package oscarmat.kth.id1212.client.view.menuitem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Tooltip extends AnchorPane {

    @FXML private Label tooltip;
    @FXML private ImageView background;

    public Tooltip(String text) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/components/icons/Tooltip.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();

        Image image = new Image(getClass().getResourceAsStream("/assets/tooltip.png"));
        background.setImage(image);

        tooltip.setText(text);
        this.setVisible(false);
    }

}
