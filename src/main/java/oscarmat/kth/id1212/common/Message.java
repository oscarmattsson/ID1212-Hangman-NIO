package oscarmat.kth.id1212.common;

import javax.json.*;
import java.util.Random;

import static oscarmat.kth.id1212.common.Protocol.*;

/**
 * Represents a message using the protocol as described in protocol.txt.
 */
public class Message {

    public static final int BUFFER_CAPACITY = 8192;

    private String id;
    private int length;
    private MessageType type;
    private Data data;

    /**
     * Container for Json-data with protocol-compliant getters.
     */
    public class Data {
        private final JsonObject data;

        public Data(JsonObject data) {
            this.data = data;
        }

        public SessionState getState() {
            return SessionState.valueOf(data.getString(STATE));
        }

        public Action getAction() {
            return Action.valueOf(data.getString(TYPE));
        }

        private JsonObject getData() {
            return data;
        }

        public JsonObject getMessage() {
            return data.getJsonObject(MSG);
        }

        public JsonObject getStatus() {
            return data.getJsonObject(STATUS);
        }
    }

    Message(MessageType type, JsonObject data) {
        this(
                Integer.toString(new Random().nextInt(100000)),
                type, data);
    }

    Message(String id, MessageType type, JsonObject data) {
        this.id = id;
        this.type = type;
        this.data = new Data(data);
    }

    public MessageType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Data getData() {
        return data;
    }

    public static Message build(MessageType type, JsonObject data, String id) {
        return new Message(id, type, data);
    }

    public static Message build(MessageType type, JsonObject data) {
        Random random = new Random();
        String id = Integer.toString(random.nextInt(100000));
        return build(type, data, id);
    }

    @Override
    public String toString() {
        String jsonData = data.getData().toString();

        StringBuilder builder = new StringBuilder();
        builder.append(Header.ID).append(KEY_VALUE_SEPARATOR).append(id);
        builder.append(FIELD_SEPARATOR);
        builder.append(Header.LENGTH).append(KEY_VALUE_SEPARATOR).append(jsonData.length());
        builder.append(FIELD_SEPARATOR);
        builder.append(Header.TYPE).append(KEY_VALUE_SEPARATOR).append(type);
        builder.append(HEADER_SEPARATOR);
        builder.append(jsonData);
        builder.append(MESSAGE_END);

        return builder.toString();
    }


}
