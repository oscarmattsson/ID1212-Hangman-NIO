package oscarmat.kth.id1212.common;

import static oscarmat.kth.id1212.common.Protocol.*;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Joins data blocks into complete messages and puts them
 * in a queue.
 */
public class MessageJoiner {

    private Queue<Message> messages = new ArrayDeque<>();
    private StringBuilder message = new StringBuilder();
    private MessageListener listener;

    public MessageJoiner(MessageListener listener) {
        this.listener = listener;
    }

    /**
     * Add a data block to the joiner. Once a complete message has been
     * accumulated, a Message object will be but in the queue.
     * @param block Block of data.
     * @throws IOException If a complete message has been received but the parser cannot interpret it.
     */
    public void addBlock(String block) throws IOException {
        String[] blocks = block.split(MESSAGE_END);
        int start = 0;
        if(block.indexOf(MESSAGE_END) != -1) {
            message.append(blocks[0]);
            addMessage();
            start = 1;
        }

        for(int i = start; i < blocks.length; i++) {
            message.append(blocks[i]);
        }
    }

    private void addMessage() throws IOException {
        messages.add(MessageParser.parse(message.toString()));
        listener.alertNewMessage();
        message = new StringBuilder();
    }

    /**
     * @return true if there is at least one message in the queue, otherwise false.
     */
    public boolean hasNext() {
        return messages.peek() != null;
    }

    /**
     * @return The next message in the queue.
     * @throws java.util.NoSuchElementException If there are no message in the queue.
     */
    public Message getNext() {
        return messages.remove();
    }

}
