package oscarmat.kth.id1212.common;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Breaks messages down into blocks of a given size and puts those blocks in a
 * queue. One block may contain parts of different messages, extract these
 * messages using a MessageJoiner.
 */
public class MessageSplitter {

    private final Queue<ByteBuffer> blocks = new ArrayDeque<>();
    private final ByteBuffer buffer;

    /**
     * Create a message splitter which breaks messages down into blocks.
     * @param maxBufferSize Size of blocks.
     */
    public MessageSplitter(int maxBufferSize) {
        buffer = ByteBuffer.allocateDirect(maxBufferSize);
    }

    /**
     * Breaks the message down into blocks that fit in the maxBufferSize.
     * @param message Message to add to the queue.
     */
    public void addMessage(Message message) {
        ByteBuffer messageBuffer = ByteBuffer.wrap(
                message.toString().getBytes());

        while(buffer.remaining() < messageBuffer.remaining()) {
            buffer.put(messageBuffer.array(),
                    messageBuffer.position(),
                    buffer.remaining());
            addBlock();
        }
        buffer.put(messageBuffer.array(),
                messageBuffer.position(),
                messageBuffer.remaining());
    }

    private void addBlock() {
        buffer.flip();
        byte[] block = new byte[buffer.limit()];
        buffer.get(block);
        blocks.add(ByteBuffer.wrap(block));
        buffer.clear();
    }

    /**
     * @return True if there is at least one block in the queue, false otherwise.
     */
    public boolean hasNext() {
        return blocks.peek() != null || buffer.position() != 0;
    }

    /**
     * @return The next block.
     */
    public ByteBuffer getNext() {
        ByteBuffer next = blocks.poll();
        if(next == null) {
            buffer.flip();
            next = buffer.duplicate();
            buffer.clear();
        }
        return next;
    }
}
