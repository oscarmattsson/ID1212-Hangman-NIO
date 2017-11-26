package oscarmat.kth.id1212.client.view.components;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import oscarmat.kth.id1212.sceneframework.CustomComponent;

import java.io.IOException;

/**
 * Represents a slot for a letter in a word to be guessed in the Hangman game.
 */
public class WordLetter extends CustomComponent {

    private static final String PATH = "/client/components/game/WordLetter.fxml";

    @FXML private Label letterLabel;

    public WordLetter() throws IOException {
        super(PATH);
    }

    /**
     * Update the displayed letter.
     * @param letter Letter to be displayed.
     */
    public void setLetter(char letter) {
        letterLabel.setText(Character.toString(letter));
    }
}
