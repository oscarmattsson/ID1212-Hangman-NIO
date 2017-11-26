package oscarmat.kth.id1212.client.view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import oscarmat.kth.id1212.client.controller.GameController;
import oscarmat.kth.id1212.client.view.menuitem.ConnectionStatus;
import oscarmat.kth.id1212.client.view.menuitem.ExitButton;
import oscarmat.kth.id1212.client.view.menuitem.Tooltip;
import oscarmat.kth.id1212.client.view.scene.ConnectScene;
import oscarmat.kth.id1212.client.view.scene.GameScene;
import oscarmat.kth.id1212.client.view.scene.WelcomeScene;
import oscarmat.kth.id1212.common.Message;
import oscarmat.kth.id1212.common.Protocol;
import oscarmat.kth.id1212.sceneframework.SceneManager;
import oscarmat.kth.id1212.server.controller.StatusListener;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Manages the main window.
 */
public class App implements Initializable, StatusListener {

    // Path to stage FXML file
    private static final String FXML_STAGE = "/client/Stage.fxml";
    private final Stage stage;

    // Injected static assets
    @FXML private AnchorPane titlepane;
    @FXML private Label title;
    @FXML private VBox menubar;
    @FXML private AnchorPane tooltipPane;
    @FXML private AnchorPane root;
    @FXML private Label statusLabel;
    @FXML private Label scoreLabel;

    // Menu items
    private ExitButton exitButton;
    private ConnectionStatus connectionStatus;

    // Dynamic scenes
    private SceneManager sceneManager;
    private ConnectScene connectScene;
    private GameScene gameScene;
    private WelcomeScene welcomeScene;

    // Helper variables for window events
    private double offsetX, offsetY;

    // Helper variables for bound values
    private StringProperty aliasProperty = new SimpleStringProperty();
    private IntegerProperty scoreProperty = new SimpleIntegerProperty();
    private StringProperty statusProperty = new SimpleStringProperty();

    // Controller
    private final GameController controller;

    /**
     * Initialize the stage.
     * @param stage Primary stage for the application.
     */
    public App(Stage stage, GameController controller) {
        this.stage = stage;
        this.controller = controller;
        controller.addListener(this);
        initStage();
    }

    /**
     * Set the title of the application.
     * @param title Title to be shown.
     */
    public void setTitle(String title) {
        stage.setTitle(title);
    }

    /**
     * Start the application.
     */
    public void show() {
        stage.show();
    }

    /**
     * Initialize the stage with the base GUI.
     */
    private void initStage() {
        Parent parent = (Parent) loadScene(FXML_STAGE, this);
        Scene scene = new Scene(parent);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
    }

    /**
     * Load all dynamic FXML resources into objects.
     */
    private void loadResources() {
        try {
            sceneManager = new SceneManager();
            connectScene = new ConnectScene(controller);
            gameScene = new GameScene(controller);
            welcomeScene = new WelcomeScene(controller);

            exitButton = new ExitButton(createTooltip("exit"));
            connectionStatus = new ConnectionStatus(createTooltip("disconnect"));

        }
        catch (IOException e) {
            e.printStackTrace();
            controller.exit();
        }
    }

    /**
     * Add dynamic assets to their corresponding static parents in the main
     * stage.
     */
    private void initDynamicAssets() {
        root.getChildren().add(sceneManager);
        menubar.getChildren().add(exitButton);
        menubar.getChildren().add(connectionStatus);
    }

    /**
     * Create a tooltip object and add it to the tooltip pane.
     * @param text Text to be displayed in tooltip.
     * @return Tooltip object.
     * @throws IOException If the tooltip node could not be loaded.
     */
    private Tooltip createTooltip(String text) throws IOException {
        Tooltip tooltip = new Tooltip(text);
        tooltipPane.getChildren().add(tooltip);
        return tooltip;
    }

    /**
     * Load an FXML resource.
     * @param path Path to resource.
     * @param controller GameController for node.
     * @return Node loaded from path.
     */
    private Node loadScene(String path, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            loader.setController(controller);
            return loader.load();
        }
        catch (IOException e) {
            System.err.println("Could not load resource: " + path);
            e.printStackTrace();
            this.controller.exit();
            return null;
        }
    }

    /**
     * Set up event handlers for the main stage.
     */
    private void setStageEvents() {
        titlepane.setOnMousePressed(this::onTitlePress);
        titlepane.setOnMouseDragged(this::onTitleDrag);
        title.textProperty().bind(stage.titleProperty());

        exitButton.setOnMouseClicked(this::onExitClick);
    }

    private void onTitlePress(MouseEvent event) {
        offsetX = event.getSceneX();
        offsetY = event.getSceneY();
    }

    private void onTitleDrag(MouseEvent event) {
        stage.setX(event.getScreenX() - offsetX);
        stage.setY(event.getScreenY() - offsetY);
    }

    private void onExitClick(MouseEvent event) {
        controller.exit();
    }

    private void onDisconnectClick(MouseEvent event) {
        controller.disconnect();
        connectionStatus.removeEventHandler(event.getEventType(), this::onDisconnectClick);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadResources();
        initDynamicAssets();
        setStageEvents();
        sceneManager.loadScene(connectScene);

        scoreLabel.textProperty().setValue("");
        statusLabel.textProperty().bind(statusProperty);
    }

    @Override
    public void alertConnected(InetSocketAddress serverAddress) {
        connectionStatus.setOnMouseClicked(this::onDisconnectClick);
        connectionStatus.setState(ConnectionStatus.State.Connected);
        sceneManager.loadScene(welcomeScene);
    }

    @Override
    public void alertDisconnected() {
        connectionStatus.setState(ConnectionStatus.State.Disconnected);
        sceneManager.loadScene(connectScene);
    }

    @Override
    public void alertNewMessage(Message.Data message) {
        if(message.getStatus().getBoolean(Protocol.STATUS_OK)) {
            switch (message.getAction()) {
                case ALIAS:
                    handleAlias(message.getMessage());
                    break;
                case NEW_GAME:
                    handleNewGame(message.getMessage());
                    break;
                case PLAY:
                    switch (message.getState()) {
                        case PLAYING:
                            handlePlay(message.getMessage());
                            break;
                        case NO_GAME:
                            handleGameOver(message.getMessage());
                            break;
                    }
                    handlePlay(message.getMessage());
                    break;
                default:
                    System.err.println("Unknown state. Disconnecting from server.");
                    controller.disconnect();
            }
        }
        else {
            System.err.println("Message error: " +
                    message.getStatus().getString(Protocol.STATUS_MSG));
            controller.exit();
        }
    }

    private void handleAlias(JsonObject data) {
        aliasProperty.setValue(data.getString(Protocol.ALIAS));
        scoreProperty.setValue(data.getInt(Protocol.SCORE));
        JsonArray leaderboard = data.getJsonArray(Protocol.LEADERBOARD);

        aliasProperty.concat(": ").concat(scoreProperty);
        controller.newGame();
    }

    private void handleNewGame(JsonObject data) {
        JsonObject gamestate = data.getJsonObject(Protocol.GAMESTATE);
        String wordState = gamestate.getString(Protocol.GAMESTATE_WORDSTATE);
        int failedAttempts = gamestate.getInt(Protocol.GAMESTATE_FAILEDATTEMPTS);
        int maxAttempts = gamestate.getInt(Protocol.GAMESTATE_MAXATTEMPTS);

        gameScene.newGame(wordState, failedAttempts, maxAttempts);
        sceneManager.loadScene(gameScene);
    }

    private void handlePlay(JsonObject data) {
        JsonArray guesses = data.getJsonArray(Protocol.GUESSES);
        JsonObject gamestate = data.getJsonObject(Protocol.GAMESTATE);
        String wordState = gamestate.getString(Protocol.GAMESTATE_WORDSTATE);
        int failedAttempts = gamestate.getInt(Protocol.GAMESTATE_FAILEDATTEMPTS);
        int maxAttempts = gamestate.getInt(Protocol.GAMESTATE_MAXATTEMPTS);

        gameScene.update(guesses, wordState, failedAttempts, maxAttempts);
    }

    private void handleGameOver(JsonObject data) {
        JsonObject gamestate = data.getJsonObject(Protocol.GAMESTATE);
        String wordState = gamestate.getString(Protocol.GAMESTATE_WORDSTATE);
        int failedAttempts = gamestate.getInt(Protocol.GAMESTATE_FAILEDATTEMPTS);
        int maxAttempts = gamestate.getInt(Protocol.GAMESTATE_MAXATTEMPTS);
        scoreProperty.setValue(data.getInt(Protocol.SCORE));

        boolean win = data.getBoolean(Protocol.WIN);
    }

    @Override
    public void updateStatus(String status) {
        synchronized (statusProperty) {
            //statusProperty.setValue(status);
        }
    }
}
