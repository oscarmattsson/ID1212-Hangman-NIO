package oscarmat.kth.id1212.common;

/**
 * Is thrown when a partial parsed messaged is converted into a Message object.
 */
public class MessageIncompleteException extends Exception {

    public MessageIncompleteException() {
        super("Message object could not be retrieved because the message was incomplete.");
    }
}
