package oscarmat.kth.id1212.server.net;

import com.sun.xml.internal.stream.events.StartElementEvent;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
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

    private boolean running = true;
    private Selector selector;
    private ServerSocketChannel channel;

    /**
     * Create a game server using the default port and the assigned stream
     * for logging errors and messages from the server.
     * @param out Output stream for server logs.
     */
    public GameServer(OutputStream out) {
        this(out, DEFAULT_PORT);
    }

    /**
     * Create a game server using an assigned port and the assigned stream
     * for logging errors and messages from the server.
     * @param port Port used by server.
     * @param out Output stream for server logs.
     */
    public GameServer(OutputStream out, int port) {
        this.port = port;
        logger = new Logger(out);
    }

    /**
     * Start the server.
     */
    private void serve() {
        openSelector();
        startListening();
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

            ClientHandler handler = new ClientHandler(clientChannel);
            clientChannel.register(selector, SelectionKey.OP_READ, handler);
            clientChannel.setOption(StandardSocketOptions.SO_LINGER, LINGER_TIME);
        }
        catch (IOException e) {
            logger.putError(SERVER_NAME, e.getMessage());
        }
    }

    /**
     * Log a fatal error and set the server to stop running.
     * @param e Exception which caused the fatal error.
     */
    private void fatalError(Exception e) {
        logger.putError(SERVER_NAME, e.getMessage());
        running = false;
    }

    @Override
    public void run() {
        serve();
    }
}
