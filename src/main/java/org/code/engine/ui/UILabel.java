package org.code.engine.ui;

import org.code.engine.graphics.TextRenderer;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

public final class UILabel implements UIElement {

    public enum Alignment {
        LEFT, CENTER, RIGHT
    }

    private final UIBounds bounds;
    private String text;
    private Alignment alignment = Alignment.LEFT;

    // Optional anchor for responsive positioning
    private Optional<UIAnchor> anchor = Optional.empty();

    private boolean visible = true;
    private boolean enabled = true;

    private float textScale = 1.0f;
    private float colorR = 1.0f;
    private float colorG = 1.0f;
    private float colorB = 1.0f;

    private boolean showBackground = false;
    private float bgR = 0.0f;
    private float bgG = 0.0f;
    private float bgB = 0.0f;
    private float bgAlpha = 0.7f;


    public UILabel(float x, float y, float width, float height, String text) {
        this.bounds = new UIBounds(x, y, width, height);
        this.text = text;
    }


    public UILabel(float x, float y, String text) {
        // Auto-size constructor - calculates width/height based on text
        TextRenderer textRenderer = TextRenderer.getInstance();
        float width = textRenderer.getTextWidth(text, 1.0f);
        float height = textRenderer.getTextHeight(1.0f);
        this.bounds = new UIBounds(x, y, width, height);
        this.text = text;
    }

    /**
     * Constructor with anchor for responsive positioning
     */
    public UILabel(UIAnchor anchor, float width, float height, String text) {
        this.anchor = Optional.of(anchor);
        this.bounds = new UIBounds(0, 0, width, height);
        this.text = text;
        updatePosition(); // Calculate initial position
    }

    /**
     * Auto-size constructor with anchor
     */
    public UILabel(UIAnchor anchor, String text) {
        TextRenderer renderer = TextRenderer.getInstance();
        float width = renderer.getTextWidth(text, 1.0f);
        float height = renderer.getTextHeight(1.0f);
        this.anchor = Optional.of(anchor);
        this.bounds = new UIBounds(0, 0, width, height);
        this.text = text;
        updatePosition();
    }

    @Override
    public void update(double delta) {
        anchor.ifPresent(x -> updatePosition());
    }

    /**
     * Recalculate position based on anchor
     */
    private void updatePosition() {
        anchor.ifPresent(anch -> {
            float x = anch.calculateX(bounds.getWidth());
            float y = anch.calculateY(bounds.getHeight());
            bounds.setX(x);
            bounds.setY(y);
        });
    }

    @Override
    public void render() {
        if (!visible || text == null || text.isEmpty()) {
            return;
        }

        TextRenderer textRenderer = TextRenderer.getInstance();

        float x = bounds.getX();
        float y = bounds.getY();
        float width = bounds.getWidth();
        float height = bounds.getHeight();

        if (showBackground) {
            GL11.glBegin(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glColor4f(bgR, bgG, bgB, bgAlpha);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(x, y);
            GL11.glVertex2f(x + width, y);
            GL11.glVertex2f(x + width, y + height);
            GL11.glVertex2f(x, y + height);
            GL11.glEnd();

            GL11.glDisable(GL11.GL_BLEND);
        }

        // Render text based on alignment
        float textY = y + (height - textRenderer.getTextHeight(textScale)) / 2;

        switch (alignment) {
            case LEFT:
                textRenderer.drawText(text, x, textY, textScale, colorR, colorG, colorB);
                break;
            case CENTER:
                float centerX = x + width / 2;
                textRenderer.drawTextCentered(text, centerX, textY, textScale, colorR, colorG, colorB);
                break;
            case RIGHT:
                float rightX = x + width;
                textRenderer.drawTextRight(text, rightX, textY, textScale, colorR, colorG, colorB);
                break;
        }
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;

        anchor.ifPresent(anch -> {
            TextRenderer textRenderer = TextRenderer.getInstance();
            bounds.setWidth(textRenderer.getTextWidth(text, textScale));
            updatePosition();
        });
    }

    public UIBounds getBounds() {
        return bounds;
    }

    public void setColor(float r, float g, float b) {
        this.colorR = r;
        this.colorG = g;
        this.colorB = b;
    }

    public void setTextScale(float textScale) {
        this.textScale = textScale;

        anchor.ifPresent(anch -> {
            TextRenderer textRenderer = TextRenderer.getInstance();
            bounds.setWidth(textRenderer.getTextWidth(text, textScale));
            bounds.setHeight(textRenderer.getTextHeight(textScale));
            updatePosition();
        });

    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public void setShowBackground(boolean show) {
        this.showBackground = show;
    }

    public void setBackgroundColor(float r, float g, float b, float alpha) {
        this.bgR = r;
        this.bgG = g;
        this.bgB = b;
        this.bgAlpha = alpha;
    }

    public void setAnchor(UIAnchor anchor) {
        this.anchor = Optional.ofNullable(anchor);
        updatePosition();
    }
}
