package oscarmat.kth.id1212.common;

public class Message {

    public static final int MAX_SIZE = 8192;

    private boolean isComplete = true;

    private Protocol.MessageType type;
    private Protocol.ConnectionState state;
    private Protocol.Action action;

    private int length;

    public Message() {

    }

    @Override
    public String toString() {
        return "";
    }

    public static Message parse(String message) {
        return new Message();
    }
}
