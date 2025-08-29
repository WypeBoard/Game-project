package org.code.engine;

import org.code.utils.Constants;

import javax.swing.*;

/**
 * The Game class sets up the main game window.
 * It initializes the JFrame and attaches the GameBoard which contains the game loop and rendering.
 * <p>
 * Why: Separating GameFrame from GameBoard keeps logic clean—Game is window-level, GameBoard is game-level.
 */
public class GameFrame extends JFrame {

    public GameFrame(GamePanel panel) {
        // create a window frame and set the title in the toolbar
        setTitle(Constants.TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // Static or dynamic window size
        // add the jpanel to the window
        add(panel);
        // pack() should be called after setResizable() to avoid issues on some platforms
        pack();
        // open window in the center of the screen
        setLocationRelativeTo(null);
        // display the window
        setVisible(true);
    }

}
