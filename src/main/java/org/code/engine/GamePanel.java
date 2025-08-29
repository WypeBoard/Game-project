package org.code.engine;

import org.code.utils.Constants;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * GamePanel handles the main game loop, rendering, and updates.
 * It runs on its own thread to manage timing independent of the UI thread.
 * <p>
 * Why: Using a separate thread for the loop ensures consistent updates and smoother gameplay.
 */
public class GamePanel extends JPanel {

    private BufferedImage image;

    public GamePanel() {
        setPreferredSize(new Dimension(Constants.GAME_WIDTH, Constants.GAME_HEIGHT));  // Set initial screen size
        setDoubleBuffered(true);
        setFocusable(true); // Ensures keyboard focus
        requestFocusInWindow(); // Requests focus when the panel is shown
        addKeyListener(new InputHandler()); // Defines how key presses are handled
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }
}
