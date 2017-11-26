package oscarmat.kth.id1212.client.startup;

import javafx.application.Application;
import javafx.stage.Stage;
import oscarmat.kth.id1212.client.controller.GameController;
import oscarmat.kth.id1212.client.net.NetHandler;
import oscarmat.kth.id1212.client.view.App;

public class Client extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        NetHandler net = new NetHandler();
        GameController controller = new GameController(net);
        App app = new App(stage, controller);
        app.setTitle("Hangman-NIO");
        app.show();
    }

    public static void main(String[] args) {
        Client.launch(args);
    }

}
