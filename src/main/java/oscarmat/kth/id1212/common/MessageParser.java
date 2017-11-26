package oscarmat.kth.id1212.common;


import javax.json.Json;
import javax.json.JsonObject;

import java.io.IOException;
import java.io.StringReader;

import static oscarmat.kth.id1212.common.Protocol.*;

/**
 * Parses a Message from a string according to the protocol described in
 * Protocol.txt.
 */
class MessageParser {

    /**
     * Parse a message.
     * @param message Protocol-compliant message.
     * @throws IOException If the message does not follow the protocol.
     * @return Message object of the parsed String.
     */
    static Message parse(String message) throws IOException {
        MessageType type = null;
        String id = null;
        JsonObject data;

        String[] messageParts = message.split(HEADER_SEPARATOR);
        String[] headers = messageParts[0].split(FIELD_SEPARATOR);

        for (String field : headers) {
            String[] keyValue = field.split(KEY_VALUE_SEPARATOR);
            Header key = Header.valueOf(keyValue[0]);
            String value = keyValue[1];
            switch (key) {
                case LENGTH:
                    break;
                case TYPE:
                    type = MessageType.valueOf(value);
                    break;
                case ID:
                    id = value;
                default:
                    throw new IOException(message);
            }
        }

        data = Json.createReader(
                new StringReader(messageParts[1])).readObject();
        return new Message(id, type, data);
    }
}
