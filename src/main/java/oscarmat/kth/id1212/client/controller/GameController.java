package oscarmat.kth.id1212.client.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import oscarmat.kth.id1212.client.net.NetHandler;
import oscarmat.kth.id1212.client.net.NetListener;
import oscarmat.kth.id1212.common.Message;
import oscarmat.kth.id1212.common.Protocol.SessionState;
import oscarmat.kth.id1212.server.controller.StatusListener;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Acts as buffer between view and net layers and keeps track of the state
 * of communication.
 */
public class GameController implements NetListener {

    private List<StatusListener> listeners = new ArrayList<>();

    private final NetHandler net;
    private SessionState state;

    public GameController(NetHandler net) {
        this.net = net;
        state = null;
        net.addListener(this);
    }

    public void addListener(StatusListener listener) {
        net.addListener(listener);
        listeners.add(listener);
    }

    public void connect(String host, int port) {
        if(!net.isConnected()) {
            net.connect(host, port);
            setStatus("Connecting to " + host + ":" + port + "...");
        }
    }

    public void disconnect() {
        if(net.isConnected()) {
            net.disconnect();
            setStatus("Disconnecting from server...");
        }
    }

    public void setAlias(String alias) {
        net.setAlias(state, alias);
        setStatus("Setting alias...");
    }

    public void newGame() {
        net.newGame(state);
    }

    public void play(String guess) {
        net.play(state, guess);
    }

    private void setStatus(String status) {
        for(StatusListener listener : listeners) {
            listener.updateStatus(status);
        }
    }

    @Override
    public void alertConnected(InetSocketAddress serverAddress) {
        state = SessionState.INIT;
        setStatus("Connected");
    }

    @Override
    public void alertDisconnected() {
        state = null;
        setStatus("Disconnected");
    }

    @Override
    public void alertNewMessage(Message.Data message) {
        state = message.getState();
        setStatus("Ready");
    }

    public void exit() {
        net.disconnect();
        Platform.exit();
        System.exit(1);
    }
}
