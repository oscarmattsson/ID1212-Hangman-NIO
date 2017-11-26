package oscarmat.kth.id1212.client.view.menuitem;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ConnectionStatus extends MenuItem {

    public enum State {
        Connected,
        Disconnected,
        Loading
    }

    private static final String CONNECTED = "/assets/connected.png";
    private static final String DISCONNECTED = "/assets/disconnected.png";
    private static final String LOADING = "/assets/loading.png";
    private static final String HOVER = "/assets/disconnect.png";

    private State state;

    private final Image connected;
    private final Image disconnected;
    private final Image loading;
    private final Image hover;

    public ConnectionStatus(Tooltip tooltip) throws IOException {
        super(tooltip);

        state = State.Disconnected;

        connected = new Image(getClass().getResourceAsStream(CONNECTED));
        disconnected = new Image(getClass().getResourceAsStream(DISCONNECTED));
        loading = new Image(getClass().getResourceAsStream(LOADING));
        hover = new Image(getClass().getResourceAsStream(HOVER));

        setImage(disconnected);

        this.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event) -> {
            if(state != State.Disconnected) {
                setImage(hover);
            }
        });
        this.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent event) -> {
            if(state != State.Disconnected) {
                setStateImage();
            }
        });
    }

    public void setState(State state) {
        this.state = state;
        setStateImage();
    }

    private void setStateImage() {
        switch (state) {
            case Loading:
                setImage(loading);
                break;
            case Connected:
                setImage(connected);
                break;
            case Disconnected:
                setImage(disconnected);
                break;
        }
    }
}
