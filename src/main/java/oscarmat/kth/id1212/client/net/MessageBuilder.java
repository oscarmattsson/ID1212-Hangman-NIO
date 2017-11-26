package oscarmat.kth.id1212.client.net;

import oscarmat.kth.id1212.common.Message;
import oscarmat.kth.id1212.common.Protocol;

import javax.json.Json;
import javax.json.JsonObject;

public class MessageBuilder {

    public static Message aliasRequest(Protocol.SessionState state, String alias) {
        JsonObject data = Json.createObjectBuilder()
                .add(Protocol.STATE, state.toString())
                .add(Protocol.TYPE, Protocol.Action.ALIAS.toString())
                .add(Protocol.MSG, Json.createObjectBuilder()
                        .add(Protocol.ALIAS, alias)
                        .build())
                .build();
        return Message.build(Protocol.MessageType.REQUEST, data);
    }

    public static Message newGameRequest(Protocol.SessionState state) {
        JsonObject data = Json.createObjectBuilder()
                .add(Protocol.STATE, state.toString())
                .add(Protocol.TYPE, Protocol.Action.NEW_GAME.toString())
                .build();
        return Message.build(Protocol.MessageType.REQUEST, data);
    }

    public static Message playRequest(Protocol.SessionState state, String guess) {
        JsonObject data = Json.createObjectBuilder()
                .add(Protocol.STATE, state.toString())
                .add(Protocol.TYPE, Protocol.Action.PLAY.toString())
                .add(Protocol.MSG, Json.createObjectBuilder()
                        .add(Protocol.GUESS, guess)
                        .build())
                .build();
        return Message.build(Protocol.MessageType.REQUEST, data);
    }
}
