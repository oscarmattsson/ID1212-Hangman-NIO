package oscarmat.kth.id1212.client.view.components;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import oscarmat.kth.id1212.sceneframework.CustomComponent;

import java.io.IOException;

/**
 * Represents a letter which can be guessed in the Hangman game.
 */
public class GuessLetter extends CustomComponent {

    private static final String PATH = "/client/components/game/GuessLetter.fxml";

    private static final String NOT_GUESSED = "grey-text";
    private static final String CORRECT = "green-text";
    private static final String INCORRECT = "red-text";

    @FXML private Label letterLabel;

    private final char letter;

    public GuessLetter(char letter) throws IOException {
        super(PATH);
        letterLabel.setText(Character.toString(letter));
        this.letter = letter;
    }

    /**
     * Update the color of the letter to indicate if it was correct or not.
     * @param correct true if correct, otherwise false.
     */
    public void setCorrect(boolean correct) {
        if(correct) {
            setLetterStyle(CORRECT);
        }
        else {
            setLetterStyle(INCORRECT);
        }
    }

    /**
     * Reset the color of the letter to indicate it is unknown whether or not
     * it is correct.
     */
    public void reset() {
        setLetterStyle(NOT_GUESSED);
    }

    private void setLetterStyle(String style) {
        letterLabel.getStyleClass().clear();
        letterLabel.getStyleClass().add(style);
    }

    /**
     * @return Character represented in the component.
     */
    public char getChar() {
        return letter;
    }
}
