package oscarmat.kth.id1212.server.controller;

import oscarmat.kth.id1212.common.Protocol;
import oscarmat.kth.id1212.common.Protocol.SessionState;
import oscarmat.kth.id1212.server.model.Game;
import oscarmat.kth.id1212.server.model.Leaderboard;
import oscarmat.kth.id1212.server.model.ReplyBuilder;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.util.Map;

public class ClientController {
    private Game game;
    private String alias;
    private SessionState state;
    int score;

    String[] wordList;
    Leaderboard leaderboard;

    public ClientController(String[] wordList, Leaderboard leaderboard) {
        this.wordList = wordList;
        this.leaderboard = leaderboard;
        state = SessionState.INIT;
        score = 0;
    }

    public SessionState getState() {
        return state;
    }

    public JsonObject setAlias(String alias) {
        if(state == SessionState.INIT) {
            this.alias = alias;
            state = SessionState.NO_GAME;
        }
        else {
            throw new IllegalStateException();
        }

        JsonArray jsonLeaderboard;
        synchronized (leaderboard) {
            jsonLeaderboard = leaderboard.getLeaderboard();
        }
        return ReplyBuilder.alias(
                state,
                alias,
                score,
                jsonLeaderboard
        );
    }

    public JsonObject newGame() {
        switch(state) {
            case PLAYING:
                score--;
            case NO_GAME:
                game = new Game(wordList);
                state = SessionState.PLAYING;
                break;
            default:
                throw new IllegalStateException();
        }
        int failedAttempts = game.getFailedAttempts();
        int maxAttempts = game.getMaximumAllowedAttempts();
        String wordState = game.getWordState();
        return ReplyBuilder.newGame(
                state,
                failedAttempts,
                maxAttempts,
                wordState
        );
    }

    public JsonObject play(String guess) {
        if(state == SessionState.PLAYING) {
            game.play(guess);
            int failedAttempts = game.getFailedAttempts();
            int maxAttempts = game.getMaximumAllowedAttempts();
            String wordState = game.getWordState();

            if(!game.isGameOver()) {
                JsonArray guesses = guessesToJson();
                return ReplyBuilder.play(
                        state,
                        failedAttempts,
                        maxAttempts,
                        wordState,
                        guesses);
            }
            else {
                boolean win = game.isGameWon();
                state = SessionState.NO_GAME;
                return ReplyBuilder.playGameOver(
                        state,
                        failedAttempts,
                        maxAttempts,
                        wordState,
                        score,
                        win
                );
            }
        }
        else {
            throw new IllegalStateException();
        }
    }

    private JsonArray guessesToJson() {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for(Map.Entry<String, Boolean> entry : game.getGuesses().entrySet()) {
            builder.add(Json.createObjectBuilder()
                    .add(Protocol.GUESS, entry.getKey())
                    .add(Protocol.GUESS_CORRECT, entry.getValue())
                    .build()
            );
        }
        return builder.build();
    }
}
