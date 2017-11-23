package oscarmat.kth.id1212.common;

public class MalformedMessageException extends Exception {

    public MalformedMessageException(String message) {
        super("Unable to parse the message: " + message);
    }
}
