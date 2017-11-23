package oscarmat.kth.id1212.common;

/**
 * Constants for the implementation of the Hangman-NIO Game Transfer Protocol.
 * See the protocol.txt file for the full documentation.
 * @version 1.0
 */
public class Protocol {

    private static final String SEPARATOR = "###";
    private static final String NEXT_FIELD = ",";
    private static final String VALUE = ":";

    /**
     * This class only contains constant static fields and does therefore not
     * need a constructor.
     */
    private Protocol() {}

    /**
     * Types of header fields can be represented in the header.
     */
    public enum Header {
        LENGTH,
        TYPE
    }

    /**
     * Types of messages which can be sent and received.
     */
    public enum MessageType {
        REQUEST,
        REPLY,
        BROADCAST,
        TERMINATE
    }

    /**
     * State of the connection between client and server.
     */
    public enum ConnectionState {
        INIT,
        NO_GAME,
        PLAYING,
        ENDED
    }

    /**
     * Actions available during the PLAYING state.
     */
    public enum Action {
        ALIAS,
        NEW_GAME,
        PLAY,
        TERMINATE
    }
}
