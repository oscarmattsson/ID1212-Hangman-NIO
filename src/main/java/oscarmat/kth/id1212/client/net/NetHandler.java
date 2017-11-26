package oscarmat.kth.id1212.client.net;

import oscarmat.kth.id1212.common.*;

import javax.json.JsonObject;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NetHandler implements Runnable, MessageListener {

    private final ByteBuffer messageBuffer = ByteBuffer.allocateDirect(Message.BUFFER_CAPACITY);
    private final MessageJoiner incoming = new MessageJoiner(this);
    private final MessageSplitter outgoing = new MessageSplitter(Message.BUFFER_CAPACITY);

    private final List<NetListener> listeners = new ArrayList<>();

    private InetSocketAddress serverAddress;
    private SocketChannel channel;
    private Selector selector;
    private boolean connected;
    private volatile boolean sending;

    public NetHandler() {
    }

    public void addListener(NetListener listener) {
        listeners.add(listener);
    }

    public void connect(String host, int port) {
        serverAddress = new InetSocketAddress(host, port);
        new Thread(this).start();
    }

    public void setAlias(Protocol.SessionState state, String alias) {
        queueMessage(MessageBuilder.aliasRequest(state, alias));
    }

    public void newGame(Protocol.SessionState state) {
        queueMessage(MessageBuilder.newGameRequest(state));
    }

    public void play(Protocol.SessionState state, String guess) {
        queueMessage(MessageBuilder.playRequest(state, guess));
    }

    @Override
    public void run() {
        try {
            openConnection();
            openSelector();

            while(connected) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while(keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if(!key.isValid()) {
                        continue;
                    }
                    if(key.isConnectable()) {
                        finalizeConnection(key);
                    }
                    else if(key.isReadable()) {
                        receive();
                    }
                    else if(key.isWritable()) {
                        send(key);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            endConnection();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receive() throws IOException {
        System.out.println("Read ready");
        messageBuffer.clear();
        int byteCount = channel.read(messageBuffer);
        if (byteCount == -1) {
            throw new IOException("Server has closed the connection.");
        }
        incoming.addBlock(readBuffer());
    }

    private void queueMessage(Message message) {
        outgoing.addMessage(message);
        channel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
        selector.wakeup();
    }

    private void send(SelectionKey key) throws IOException {
        System.out.println("Write ready");
        while(outgoing.hasNext()) {
            channel.write(outgoing.getNext());
        }
        key.interestOps(SelectionKey.OP_READ);
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

    private void openConnection() throws IOException {
        System.out.println("Open connection");
        channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(serverAddress);
        connected = true;
    }

    private void openSelector() throws IOException {
        System.out.println("Open selector");
        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_CONNECT);
    }

    public void disconnect() {
        connected = false;
    }

    private void endConnection() throws IOException {
        channel.close();
        channel.keyFor(selector).cancel();
        notifyDisconnected();
    }

    private void finalizeConnection(SelectionKey key) throws IOException {
        System.out.println("Finalize Connection");
        channel.finishConnect();
        key.interestOps(SelectionKey.OP_READ);
        notifyConnected(serverAddress);
    }

    private void notifyNewMessage(Message.Data data) {
        for(NetListener listener : listeners) {
            listener.alertNewMessage(data);
        }
    }

    private void notifyConnected(InetSocketAddress serverAddress) {
        for(NetListener listener : listeners) {
            listener.alertConnected(serverAddress);
        }
    }

    private void notifyDisconnected() {
        for(NetListener listener : listeners) {
            listener.alertDisconnected();
        }
    }

    @Override
    public void alertNewMessage() {
        while(incoming.hasNext()) {
            notifyNewMessage(incoming.getNext().getData());
        }
    }

    public boolean isConnected() {
        if(channel == null) {
            return false;
        } else {
            return channel.isConnected();
        }
    }
}
