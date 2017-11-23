package oscarmat.kth.id1212.common;

/**
 * Constants for the implementation of the Hangman-NIO Game Transfer Protocol.
 * See the protocol.txt file for the full documentation.
 * @version 1.0
 */
public class Protocol {

    // Header constants
    static final String HEADER_SEPARATOR = "###";
    static final String FIELD_SEPARATOR = ",";
    static final String KEY_VALUE_SEPARATOR = ":";

    // JSON common fields identifiers
    static final String STATE = "state";
    static final String TYPE = "type";
    static final String MSG = "msg";
    static final String STATUS = "status";
    static final String STATUS_OK = "ok";
    static final String STATUS_MSG = "msg";

    // JSON msg field identifiers
    static final String NAME = "name";
    static final String SCORE = "score";
    static final String LEADERBOARD = "leaderboard";
    static final String GAMESTATE = "gamestate";
    static final String GUESS = "guess";
    static final String WIN = "win";

    // JSON gamestate field identifiers
    static final String GAMESTATE_FAILEDATTEMPTS = "failedAttempts";
    static final String GAMESTATE_MAXATTEMPTS = "maxAttempts";
    static final String GAMESTATE_WORDSTATE = "wordState";

    // JSON guess field identifiers
    static final String GUESS_VALUE = "value";
    static final String GUESS_CORRECT = "correct";

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
