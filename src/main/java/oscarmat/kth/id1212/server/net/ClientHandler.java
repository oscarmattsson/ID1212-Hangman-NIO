package oscarmat.kth.id1212.server.net;

import oscarmat.kth.id1212.common.Message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

class ClientHandler {

    private SocketChannel channel;
    private ByteBuffer messageBuffer = ByteBuffer.allocateDirect(Message.MAX_SIZE);

    ClientHandler(SocketChannel channel) {
        this.channel = channel;
    }

    /**
     * Reads a message from the client
     * @throws IOException If message cannot be read.
     */
    void receive() throws IOException {
        messageBuffer.clear();
        int byteCount = channel.read(messageBuffer);
        if (byteCount == -1) {
            throw new IOException("Client has closed the connection.");
        }
        Message message =
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

}
