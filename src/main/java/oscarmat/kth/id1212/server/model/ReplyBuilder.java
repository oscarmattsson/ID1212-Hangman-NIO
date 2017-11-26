package oscarmat.kth.id1212.server.model;

import oscarmat.kth.id1212.common.Protocol;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

public class ReplyBuilder {

    public static JsonObject alias(Protocol.SessionState state, String alias, int score, JsonArray leaderboard) {
        return Json.createObjectBuilder()
                .add(Protocol.STATE, state.toString())
                .add(Protocol.TYPE, Protocol.Action.ALIAS.toString())
                .add(Protocol.MSG, Json.createObjectBuilder()
                        .add(Protocol.ALIAS, alias)
                        .add(Protocol.SCORE, score)
                        .add(Protocol.LEADERBOARD, leaderboard)
                        .build())
                .add(Protocol.STATUS, Json.createObjectBuilder()
                        .add(Protocol.STATUS_OK, true)
                        .build())
                .build();
    }

    public static JsonObject newGame(Protocol.SessionState state, int failedAttempts, int maxAttempts, String wordState) {
        return Json.createObjectBuilder()
                .add(Protocol.STATE, state.toString())
                .add(Protocol.TYPE, Protocol.Action.NEW_GAME.toString())
                .add(Protocol.MSG, Json.createObjectBuilder()
                        .add(Protocol.GAMESTATE, Json.createObjectBuilder()
                                .add(Protocol.GAMESTATE_FAILEDATTEMPTS, failedAttempts)
                                .add(Protocol.GAMESTATE_MAXATTEMPTS, maxAttempts)
                                .add(Protocol.GAMESTATE_WORDSTATE, wordState)
                                .build())
                        .build())
                .add(Protocol.STATUS, Json.createObjectBuilder()
                        .add(Protocol.STATUS_OK, true)
                        .build())
                .build();
    }

    public static JsonObject play(Protocol.SessionState state, int failedAttempts, int maxAttempts, String wordState, JsonArray guesses) {
        return Json.createObjectBuilder()
                .add(Protocol.STATE, state.toString())
                .add(Protocol.TYPE, Protocol.Action.PLAY.toString())
                .add(Protocol.MSG, Json.createObjectBuilder()
                        .add(Protocol.GUESSES, guesses)
                        .add(Protocol.GAMESTATE, Json.createObjectBuilder()
                                .add(Protocol.GAMESTATE_FAILEDATTEMPTS, failedAttempts)
                                .add(Protocol.GAMESTATE_MAXATTEMPTS, maxAttempts)
                                .add(Protocol.GAMESTATE_WORDSTATE, wordState)
                                .build())
                        .build())
                .add(Protocol.STATUS, Json.createObjectBuilder()
                        .add(Protocol.STATUS_OK, true)
                        .build())
                .build();
    }

    public static JsonObject playGameOver(Protocol.SessionState state, int failedAttempts, int maxAttempts, String wordState, int score, boolean win) {
        return Json.createObjectBuilder()
                .add(Protocol.STATE, state.toString())
                .add(Protocol.TYPE, Protocol.Action.PLAY.toString())
                .add(Protocol.MSG, Json.createObjectBuilder()
                        .add(Protocol.SCORE, score)
                        .add(Protocol.WIN, win)
                        .add(Protocol.GAMESTATE, Json.createObjectBuilder()
                                .add(Protocol.GAMESTATE_FAILEDATTEMPTS, failedAttempts)
                                .add(Protocol.GAMESTATE_MAXATTEMPTS, maxAttempts)
                                .add(Protocol.GAMESTATE_WORDSTATE, wordState)
                                .build())
                        .build())
                .add(Protocol.STATUS, Json.createObjectBuilder()
                        .add(Protocol.STATUS_OK, true)
                        .build())
                .build();
    }
}
