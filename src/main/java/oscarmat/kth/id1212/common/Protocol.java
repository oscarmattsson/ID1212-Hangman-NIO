package oscarmat.kth.id1212.common;

/**
 * Constants for the implementation of the Hangman-NIO Game Transfer Protocol.
 * See the protocol.txt file for the full documentation.
 * @version 1.0
 */
public class Protocol {

    static final String MESSAGE_END = "###";

    // Header constants
    static final String HEADER_SEPARATOR = "##";
    static final String FIELD_SEPARATOR = ",";
    static final String KEY_VALUE_SEPARATOR = ":";

    // JSON common fields identifiers
    public static final String STATE = "state";
    public static final String TYPE = "type";
    public static final String MSG = "msg";
    public static final String STATUS = "status";
    public static final String STATUS_OK = "ok";
    public static final String STATUS_MSG = "msg";

    // JSON msg field identifiers
    public static final String ALIAS = "name";
    public static final String SCORE = "score";
    public static final String LEADERBOARD = "leaderboard";
    public static final String GAMESTATE = "gamestate";
    public static final String GUESS = "guess";
    public static final String GUESSES = "guesses";
    public static final String WIN = "win";

    // JSON gamestate field identifiers
    public static final String GAMESTATE_FAILEDATTEMPTS = "failedAttempts";
    public static final String GAMESTATE_MAXATTEMPTS = "maxAttempts";
    public static final String GAMESTATE_WORDSTATE = "wordState";

    // JSON guess field identifiers
    public static final String GUESS_VALUE = "value";
    public static final String GUESS_CORRECT = "correct";

    /**
     * This class only contains constant static fields and does therefore not
     * need a constructor.
     */
    private Protocol() {}

    /**
     * Types of header fields can be represented in the header.
     */
    enum Header {
        LENGTH,
        TYPE,
        ID
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
    public enum SessionState {
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
        PLAY
    }
}
