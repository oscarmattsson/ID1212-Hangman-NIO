package oscarmat.kth.id1212.server.net;

import oscarmat.kth.id1212.common.*;
import oscarmat.kth.id1212.server.controller.ClientController;
import oscarmat.kth.id1212.server.model.Leaderboard;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ForkJoinPool;

class ClientHandler implements MessageListener, Runnable {

    private final GameServer server;
    private final SocketChannel channel;
    private final Logger logger;
    private final MessageJoiner incoming = new MessageJoiner(this);
    private final MessageSplitter outgoing = new MessageSplitter(Message.BUFFER_CAPACITY);
    private final ByteBuffer messageBuffer = ByteBuffer.allocateDirect(Message.BUFFER_CAPACITY);

    private final ClientController controller;

    private final String name;

    ClientHandler(
            GameServer server,
            SocketChannel channel,
            Logger logger,
            String[] wordList,
            Leaderboard leaderboard) throws IOException {
        this.server = server;
        this.channel = channel;
        this.logger = logger;
        controller = new ClientController(wordList, leaderboard);

        name = channel.socket().getRemoteSocketAddress().toString();
        logger.putMessage(name, "New connection accepted.");
    }

    String getName() {
        return name;
    }

    @Override
    public void run() {
        try {
            while (incoming.hasNext()) {
                Message message = incoming.getNext();
                switch (message.getType()) {
                    case REQUEST:
                        handleRequest(message);
                        break;
                    case TERMINATE:
                        terminateConnection();
                        return;
                }
            }
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void handleRequest(Message message) throws IOException {
        Message.Data data = message.getData();
        String id = message.getId();
        JsonObject replyData = Json.createObjectBuilder().build();
        switch (data.getState()) {
            case INIT:
                replyData = handleInit(data);
                break;
            case NO_GAME:
                replyData = handleNoGame(data);
                break;
            case PLAYING:
                replyData = handlePlaying(data);
                break;
            case ENDED:
                terminateConnection();
                return;
        }
        outgoing.addMessage(Message.build(Protocol.MessageType.REPLY, replyData, id));
        channel.keyFor(server.getSelector()).interestOps(SelectionKey.OP_WRITE);
        server.getSelector().wakeup();
    }

    private JsonObject handleInit(Message.Data data) {
        switch (data.getAction()) {
            case ALIAS:
                JsonObject msg = data.getMessage();
                String alias = msg.getString(Protocol.ALIAS);
                JsonObject replyData = controller.setAlias(alias);
                logger.putMessage(name, "Assigned alias: " + alias);
                return replyData;
            default:
                logger.putError(name, "Invalid state. Disconnecting.");
                terminateConnection();
                return null;
        }
    }

    private JsonObject handleNoGame(Message.Data data) {
        switch (data.getAction()) {
            case NEW_GAME:
                JsonObject replyData = controller.newGame();
                logger.putMessage(name, "Started new game.");
                return replyData;
            default:
                logger.putError(name, "Invalid state. Disconnecting.");
                terminateConnection();
                return null;
        }
    }

    private JsonObject handlePlaying(Message.Data data) {
        switch (data.getAction()) {
            case PLAY:
                JsonObject msg = data.getMessage();
                String guess = msg.getString(Protocol.GUESS);
                JsonObject replyData = controller.play(guess);
                logger.putMessage(name, "Played guess: " + guess);
                return replyData;
            default:
                logger.putError(name, "Invalid state. Disconnecting.");
                terminateConnection();
                return null;
        }
    }

    /**
     * Terminate the connection with the client.
     */
    private void terminateConnection() {
        server.terminateClient(channel);
    }

    /**
     * Reads a message from the client.
     * @throws IOException If message cannot be read.
     */
    void receive() throws IOException {
        messageBuffer.clear();
        int byteCount = channel.read(messageBuffer);
        if (byteCount == -1) {
            throw new IOException("Client has closed the connection.");
        }
        incoming.addBlock(readBuffer());
    }

    /**
     * Sends all messages in the queue to the client.
     * @throws IOException
     */
    void send() throws IOException {
        while(outgoing.hasNext()) {
            channel.write(outgoing.getNext());
        }
    }

    /**
     * Extracts a message from the message buffer.
     * @return Extracted message.
     */
    private String readBuffer() {
        messageBuffer.flip();
        byte[] message = new byte[messageBuffer.remaining()];
        messageBuffer.get(message);
        return new String(message);
    }

    @Override
    public void alertNewMessage() {
        ForkJoinPool.commonPool().execute(this);
    }
}
