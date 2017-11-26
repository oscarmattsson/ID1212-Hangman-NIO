package oscarmat.kth.id1212.client.view.scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import oscarmat.kth.id1212.client.controller.GameController;
import oscarmat.kth.id1212.client.view.components.GuessLetter;
import oscarmat.kth.id1212.client.view.components.Hangman;
import oscarmat.kth.id1212.client.view.components.WordLetter;
import oscarmat.kth.id1212.common.Protocol;
import oscarmat.kth.id1212.sceneframework.CustomComponent;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.IOException;
import java.util.*;

public class GameScene extends CustomComponent {

    private static final String PATH = "/client/components/scenes/Game.fxml";

    @FXML private AnchorPane hangmanPane;
    @FXML private FlowPane letterPane;
    @FXML private FlowPane wordPane;
    @FXML private TextField wordField;
    @FXML private Button guessButton;

    private Map<Character, GuessLetter> guessLetters = new HashMap<>();
    private List<WordLetter> wordLetters = new ArrayList<>();
    private final Hangman hangman;

    private final GameController controller;

    public GameScene(GameController controller) throws IOException {
        super(PATH);
        this.controller = controller;

        hangman = new Hangman();
        initGameScene();
    }

     private void initGameScene() throws IOException {
        for(char c = 'A'; c <= 'Z'; c++) {
            GuessLetter letter = new GuessLetter(c);
            guessLetters.put(c, letter);
            letterPane.getChildren().add(letter);
        }
        hangmanPane.getChildren().add(hangman);
        guessButton.setOnAction(this::guessWord);
    }

    /**
     * Reset the state of the game scene for a new game.
     * @param word State of the new guessed word.
     * @param failedAttempts Failed attempts so far.
     * @param maxAttempts Maximum amount of attempts.
     */
    public void newGame(String word, int failedAttempts, int maxAttempts) {
        for(Map.Entry<Character, GuessLetter> entry : guessLetters.entrySet()) {
            GuessLetter letter = entry.getValue();
            letter.setOnMouseClicked(this::guessLetter);
            letter.reset();
        }

        wordLetters.clear();
        wordPane.getChildren().clear();
        try {
            for (int i = 0; i < word.length(); i++) {
                WordLetter letter = new WordLetter();
                wordLetters.add(letter);
                wordPane.getChildren().add(letter);
            }
        }
        catch (IOException e) {
            System.err.println("Could not load WordLetter objects, shutting down application.");
            controller.exit();
        }

        hangman.setProgress(failedAttempts, maxAttempts);
    }

    /**
     * Update the state of the game scene.
     * @param guesses All guesses so far and whether or not they were correct.
     * @param wordState State of the guessed word.
     * @param failedAttempts Number of failed attempts so far.
     * @param maxAttempts Maximum amount of failed attempts allowed.
     */
    public void update(JsonArray guesses, String wordState, int failedAttempts, int maxAttempts) {
        hangman.setProgress(failedAttempts, maxAttempts);
        updateWord(wordState);
        updateGuesses(guesses);
    }

    private void updateWord(String wordState) {
        for(int i = 0; i < wordLetters.size(); i++) {
            char letter = wordState.charAt(i);
            if(letter != '_') {
                wordLetters.get(i).setLetter(letter);
            }
        }
    }

    private void updateGuesses(JsonArray guesses) {
        for(JsonValue entry : guesses) {
            JsonObject guess = (JsonObject)entry;
            String value = guess.getString(Protocol.GUESS);
            boolean correct = guess.getBoolean(Protocol.GUESS_CORRECT);
            if(value.length() == 1) {
                guessLetters.get(value.charAt(0)).setCorrect(correct);
            }
        }
    }

    /**
     * Click event handler for GuessLetter objects.
     */
    private void guessLetter(MouseEvent event) {
        GuessLetter letter = (GuessLetter)event.getSource();
        char guess = letter.getChar();
        letter.removeEventHandler(event.getEventType(), this::guessLetter);

        controller.play(Character.toString(guess));
    }

    private void guessWord(ActionEvent event) {
        controller.play(wordField.getText());
    }

}
