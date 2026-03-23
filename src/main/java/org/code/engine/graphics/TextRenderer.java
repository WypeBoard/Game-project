package org.code.engine.graphics;

import org.lwjgl.opengl.GL11;
import org.w3c.dom.Text;

public final class TextRenderer {

    private static TextRenderer instance;

    private TextRenderer() {
        // Private constructor for singleton
    }

    public static TextRenderer getInstance() {
        if (instance == null) {
            instance = new TextRenderer();
        }
        return instance;
    }

    /**
     * Draw text at screen position (left-aligned)
     * Note: This uses a fallback approach - drawing colored rectangles as character placeholders
     *
     * @param text The text to render
     * @param x Screen X position
     * @param y Screen Y position
     * @param scale Size multiplier (1.0 = normal)
     */
    public void drawText(String text, float x, float y, float scale) {
        drawText(text, x, y, scale, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Draw text with custom color
     *
     * @param text The text to render
     * @param x Screen X position
     * @param y Screen Y position
     * @param scale Size multiplier
     * @param r Red (0-1)
     * @param g Green (0-1)
     * @param b Blue (0-1)
     */
    public void drawText(String text, float x, float y, float scale, float r, float g, float b) {
        if (text == null || text.isEmpty()) {
            return;
        }

        float charWidth = 8 * scale;   // 8 pixels per character
        float charHeight = 12 * scale; // 12 pixels tall
        float spacing = 2 * scale;     // 2 pixel spacing between chars

        GL11.glColor3f(r, g, b);

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            float charX = x + i * (charWidth + spacing);

            // Draw each character as a filled rectangle (placeholder)
            // In a real implementation, this would render from a texture atlas
            drawCharacterPlaceholder(c, charX, y, charWidth, charHeight);
        }
    }

    /**
     * Draw centered text
     */
    public void drawTextCentered(String text, float centerX, float y, float scale) {
        drawTextCentered(text, centerX, y, scale, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Draw centered text with color
     */
    public void drawTextCentered(String text, float centerX, float y, float scale, float r, float g, float b) {
        float textWidth = getTextWidth(text, scale);
        float x = centerX - textWidth / 2;
        drawText(text, x, y, scale, r, g, b);
    }

    /**
     * Draw right-aligned text
     */
    public void drawTextRight(String text, float rightX, float y, float scale) {
        drawTextRight(text, rightX, y, scale, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Draw right-aligned text with color
     */
    public void drawTextRight(String text, float rightX, float y, float scale, float r, float g, float b) {
        float textWidth = getTextWidth(text, scale);
        float x = rightX - textWidth;
        drawText(text, x, y, scale, r, g, b);
    }

    /**
     * Calculate the width of rendered text
     */
    public float getTextWidth(String text, float scale) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        float charWidth = 8 * scale;
        float spacing = 2 * scale;
        return text.length() * charWidth + (text.length() - 1) * spacing;
    }

    /**
     * Calculate the height of rendered text
     */
    public float getTextHeight(float scale) {
        return 12 * scale;
    }

    /**
     * Temporary placeholder - draws a simple rectangle for each character
     * This makes text visible but not readable - good enough to see where text appears
     */
    private void drawCharacterPlaceholder(char c, float x, float y, float width, float height) {
        // Skip rendering spaces
        if (c == ' ') {
            return;
        }

        // Draw a simple filled rectangle
        // Different characters could have different patterns here
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();

        // Draw a border to make it look slightly more text-like
        GL11.glColor3f(0.0f, 0.0f, 0.0f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
    }

    /**
     * Draw a background box behind text (useful for labels on busy backgrounds)
     */
    public void drawTextWithBackground(String text, float x, float y, float scale,
                                       float textR, float textG, float textB,
                                       float bgR, float bgG, float bgB, float bgAlpha) {
        float width = getTextWidth(text, scale);
        float height = getTextHeight(scale);
        float padding = 4 * scale;

        // Enable blending for transparent background
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Draw background
        GL11.glColor4f(bgR, bgG, bgB, bgAlpha);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x - padding, y - padding);
        GL11.glVertex2f(x + width + padding, y - padding);
        GL11.glVertex2f(x + width + padding, y + height + padding);
        GL11.glVertex2f(x - padding, y + height + padding);
        GL11.glEnd();

        GL11.glDisable(GL11.GL_BLEND);

        // Draw text
        drawText(text, x, y, scale, textR, textG, textB);
    }
}
