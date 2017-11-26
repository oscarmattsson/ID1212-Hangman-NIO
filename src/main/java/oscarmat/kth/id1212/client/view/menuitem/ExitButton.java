package oscarmat.kth.id1212.client.view.menuitem;

import javafx.scene.image.Image;

import java.io.IOException;

public class ExitButton extends MenuItem {

    private static final String CROSS = "/assets/cross.png";

    public ExitButton(Tooltip tooltip) throws IOException {
        super(tooltip);
        Image cross = new Image(getResourceStream(CROSS));
        setImage(cross);
    }
}
