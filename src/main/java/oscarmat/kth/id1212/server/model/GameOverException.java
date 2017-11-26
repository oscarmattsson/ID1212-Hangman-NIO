/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oscarmat.kth.id1212.server.model;

/**
 * Is thrown when a client attempts to continue playing a game
 * which is already finished.
 * @author oscar
 */
public class GameOverException extends RuntimeException {

    GameOverException() {
        super("The game is already over, no more plays allowed.");
    }
    
}
