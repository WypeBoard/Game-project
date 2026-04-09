package org.code.engine.graphics;

import org.code.utils.Logger;
import org.lwjgl.opengl.GL11;

/**
 * Singleton text renderer backed by a real bitmap font (STB TrueType).
 * Drop-in replacement for the old placeholder version — same public API.
 */
public final class TextRenderer {

    private static final String FONT_PATH  = "assets/fonts/FiraSans-Regular.ttf";
    private static final float  FONT_SIZE  = 16f;

    private static TextRenderer instance;

    private BitmapFont font;
    private boolean initialized = false;

    private TextRenderer() {
        // Private constructor for singleton
    }

    public static TextRenderer getInstance() {
        if (instance == null) {
            instance = new TextRenderer();
        }
        return instance;
    }

    public void init() {
        if (initialized) {
            return;
        }
        try {
            font = new BitmapFont(FONT_PATH, FONT_SIZE);
            initialized = true;
        } catch (Exception e) {
            Logger.error(this.getClass(), "Failed to load font, falling back to nothing.", e);
        }
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
        if (!initialized || text == null || text.isEmpty()) {
            return;
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureId());
        GL11.glColor3f(r, g, b);

        // STB TrueType uses a Y-down baseline. We pass (x, y + lineHeight) so that
        // "y" behaves as the top of the text, matching the old TextRenderer convention.
        float baseline = y + font.getLineHeight() * scale;

        GL11.glPushMatrix();
        GL11.glTranslatef(x, baseline, 0);
        GL11.glScalef(scale, scale, 1);

        GL11.glBegin(GL11.GL_QUADS);
        float cursorX = 0;
        for (int i = 0; i < text.length(); i++) {
            cursorX = font.renderChar(text.charAt(i), cursorX, 0);
        }
        GL11.glEnd();

        GL11.glPopMatrix();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
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
    public float getTextHeight(String text, float scale) {
        if (!initialized || text == null || text.isEmpty()) {
            return 0;
        }
        return font.measureWidth(text) * scale;
    }

    public float getTextHeight(float scale) {
        if (!initialized) {
            return 16f * scale; // safe fallback
        }
        return font.getLineHeight() * scale;
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

    public void cleanup() {
        if (font != null) {
            font.cleanup();
        }
        initialized = false;
    }
}
