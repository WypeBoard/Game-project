package io.github.wypeboard.island.escape.engine.graphics;

/**
 * Manages viewport dimensions and provides coordinate conversion utilities.
 * Singleton to track current window/viewport size for responsive UI.
 */
public class ViewportManager {

    private static ViewportManager instance;

    private int width;
    private int height;

    private ViewportManager() {
        this.width = 800;  // Default
        this.height = 600; // Default
    }

    public static ViewportManager getInstance() {
        if (instance == null) {
            instance = new ViewportManager();
        }
        return instance;
    }

    /**
     * Update viewport dimensions (called by WindowManager on resize)
     */
    public void updateViewport(int width, int height) {
        this.width = width;
        this.height = height;
        System.out.println("Viewport updated: " + width + "x" + height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Get X coordinate for left-aligned elements with margin
     */
    public float getLeftMargin(float margin) {
        return margin;
    }

    /**
     * Get X coordinate for right-aligned elements with margin
     */
    public float getRightMargin(float margin) {
        return width - margin;
    }

    /**
     * Get X coordinate for center
     */
    public float getCenterX() {
        return width / 2.0f;
    }

    /**
     * Get Y coordinate for top-aligned elements with margin
     */
    public float getTopMargin(float margin) {
        return margin;
    }

    /**
     * Get Y coordinate for bottom-aligned elements with margin
     */
    public float getBottomMargin(float margin) {
        return height - margin;
    }

    /**
     * Get Y coordinate for center
     */
    public float getCenterY() {
        return height / 2.0f;
    }

    /**
     * Convert percentage to actual X coordinate
     */
    public float percentToX(float percent) {
        return width * (percent / 100.0f);
    }

    /**
     * Convert percentage to actual Y coordinate
     */
    public float percentToY(float percent) {
        return height * (percent / 100.0f);
    }
}
