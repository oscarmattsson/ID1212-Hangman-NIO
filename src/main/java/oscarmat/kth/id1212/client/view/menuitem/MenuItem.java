package oscarmat.kth.id1212.client.view.menuitem;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.io.InputStream;

abstract class MenuItem extends ImageView {

    private static final String PATH = "/client/components/icons/MenuIcon.fxml";

    MenuItem(Tooltip tooltip) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();

        this.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event) -> {
            tooltip.setLayoutY(this.getLayoutY());
            tooltip.setVisible(true);
        });
        this.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent event) -> {
            tooltip.setVisible(false);
        });
    }

    InputStream getResourceStream(String path) {
        return getClass().getResourceAsStream(path);
    }
}
