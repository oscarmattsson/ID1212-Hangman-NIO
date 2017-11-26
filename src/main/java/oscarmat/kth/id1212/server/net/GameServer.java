package oscarmat.kth.id1212.server.net;

import oscarmat.kth.id1212.server.model.Leaderboard;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class GameServer implements Runnable {

    private static final int DEFAULT_PORT = 8383;
    private static final String SERVER_NAME = "SERVER";

    private static final int LINGER_TIME = 5000;

    private final int port;
    private final Logger logger;

    private final String[] wordList;
    private final Leaderboard leaderboard;

    private boolean running = true;
    private Selector selector;
    private ServerSocketChannel channel;

    /**
     * Create a game server using the default port and the assigned stream
     * for logging errors and messages from the server.
     * @param out Output stream for server logs.
     * @param wordList Word list used for games.
     * @param leaderboard Leaderboard instance across games.
     */
    public GameServer(OutputStream out, String[] wordList, Leaderboard leaderboard) {
        this(out, DEFAULT_PORT, wordList, leaderboard);
    }

    /**
     * Create a game server using an assigned port and the assigned stream
     * for logging errors and messages from the server.
     * @param port Port used by server.
     * @param out Output stream for server logs.
     * @param wordList Word list used for games.
     * @param leaderboard Leaderboard instance across games.
     */
    public GameServer(OutputStream out, int port, String[] wordList, Leaderboard leaderboard) {
        this.port = port;
        logger = new Logger(out);
        this.wordList = wordList;
        this.leaderboard = leaderboard;
    }

    /**
     * Start the server.
     */
    private void serve() {
        openSelector();
        startListening();
        logger.putMessage(SERVER_NAME, "Connected to port: " + port);
        try {
            while (running) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while(keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if(!key.isValid()) {
                        continue;
                    }
                    if(key.isAcceptable()) {
                        registerClient(key);
                    }
                    else if(key.isReadable()) {
                        readFromClient(key);
                    }
                    else if(key.isWritable()) {
                        writeToClient(key);
                    }
                }
            }
        }
        catch (Exception e) {
            fatalError(e);
        }
    }

    /**
     * Create the selector for the server.
     */
    private void openSelector() {
        try {
            selector = Selector.open();
        }
        catch (IOException e) {
            fatalError(e);
        }
    }

    /**
     * Set up the server socket channel to start listening.
     */
    private void startListening() {
        try {
            channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));
            channel.register(selector, SelectionKey.OP_ACCEPT);
        }
        catch (IOException e) {
            fatalError(e);
        }
    }

    /**
     * Register a new client by accepting their connection.
     * @param key Selection key to accept.
     */
    private void registerClient(SelectionKey key) {
        try {
            ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
            SocketChannel clientChannel = serverChannel.accept();
            clientChannel.configureBlocking(false);

            ClientHandler handler = new ClientHandler(this, clientChannel, logger, wordList, leaderboard);
            clientChannel.register(selector, SelectionKey.OP_READ, handler);
            clientChannel.setOption(StandardSocketOptions.SO_LINGER, LINGER_TIME);
        }
        catch (IOException e) {
            logger.putError(SERVER_NAME, "Error setting up connection to client: " + e.getMessage());
        }
    }

    private void readFromClient(SelectionKey key) {
        ClientHandler client = (ClientHandler)key.attachment();
        String name = client.getName();
        try {
            client.receive();
        }
        catch (Exception e) {
            logger.putError(name, "Error reading from client, closing connection.");
            terminateClient((SocketChannel)key.channel());
        }
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void writeToClient(SelectionKey key) {
        ClientHandler client = (ClientHandler)key.attachment();
        String name = client.getName();
        try {
            client.send();
        }
        catch (Exception e) {
            logger.putError(name, "Error writing to client, closing connection.");
        }
        key.interestOps(SelectionKey.OP_READ);
    }

    void terminateClient(SocketChannel clientChannel) {
        ClientHandler client = (ClientHandler)clientChannel.keyFor(selector).attachment();
        try {
            clientChannel.close();
            clientChannel.keyFor(selector).cancel();
            logger.putMessage(client.getName(), "Disconnected from server.");
        }
        catch (IOException e) {
            logger.putError(SERVER_NAME, "Error closing connection to: " + client.getName());
        }
    }

    /**
     * Log a fatal error and set the server to stop running.
     * @param e Exception which caused the fatal error.
     */
    private void fatalError(Exception e) {
        logger.putError(SERVER_NAME, "Fatal server error: " + e.getMessage());
        running = false;
    }

    @Override
    public void run() {
        serve();
    }
}
