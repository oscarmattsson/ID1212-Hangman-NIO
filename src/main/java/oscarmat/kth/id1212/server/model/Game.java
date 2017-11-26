/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oscarmat.kth.id1212.server.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an instance of a hangman game.
 * Controls the remaining amount of attempts
 * and the word selected for the game.
 * @author oscar
 */
public class Game {
    
    private Word word;
    private int attempts;
    private Map<String, Boolean> guesses;

    /**
     * Create a new game instance with a random word.
     * @param wordList List of potential words.
     */
    public Game(String[] wordList) {
        attempts = 0;
        word = new Word(wordList);
        guesses = new HashMap<>();
    }
    
    /**
     * @return Get maximum amount of allowed attempts.
     */
    public int getMaximumAllowedAttempts() {
        return word.getLength();
    }
    
    /**
     * @return Get current amount of failed attempts.
     */
    public int getFailedAttempts() {
        return attempts;
    }
    
    /**
     * @return Current state of guessed word.
     */
    public String getWordState() {
        return word.getClientWord();
    }

    /**
     * @return true if the game is either won or lost, false otherwise.
     */
    public boolean isGameLost() {
        return getFailedAttempts() == getMaximumAllowedAttempts();
    }
    
    /**
     * @return true if the game has been won, false otherwise.
     */
    public boolean isGameWon() {
        return word.getWord().equals(word.getClientWord());
    }
    
    /**
     * @return true if the game is either won or lost, false otherwise.
     */
    public boolean isGameOver() {
        return isGameLost() || isGameWon();
    }

    public Map<String, Boolean> getGuesses() {
        return guesses;
    }
    
    /**
     * Check if submitted guess is correct and adjust
     * the game state accordingly.
     * @param guess Submitted guess.
     * @return true if guess is correct, false otherwise.
     */
    public boolean play(String guess) throws GameOverException {
        if(!isGameOver()) {
            boolean isCorrect = word.guess(guess);
            if (!isCorrect) {
                attempts++;
            }
            guesses.put(guess, isCorrect);
            return isCorrect;
        }
        else {
            throw new GameOverException();
        }
    }
    
}
