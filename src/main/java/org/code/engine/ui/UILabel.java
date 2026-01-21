package org.code.engine.ui;

import org.lwjgl.opengl.GL11;

public final class UILabel implements UIElement {

    private final UIBounds bounds;
    private String text;

    private boolean visible = true;
    private boolean enabled = true;

    private float colorR = 1.0f;
    private float colorG = 1.0f;
    private float colorB = 1.0f;

    public UILabel(float x, float y, float width, float height, String text) {
        this.bounds = new UIBounds(x, y, width, height);
        this.text = text;
    }

    @Override
    public void update(double delta) {
        // Labels typically don't need updates
    }

    @Override
    public void render() {
        if (!visible) {
            return;
        }

        // For now, just render a colored rectangle
        // TODO: Implement actual text rendering
        float x = bounds.getX();
        float y = bounds.getY();
        float width = bounds.getWidth();
        float height = bounds.getHeight();

        GL11.glColor3f(colorR, colorG, colorB);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
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
    }

    public UIBounds getBounds() {
        return bounds;
    }

    public void setColor(float r, float g, float b) {
        this.colorR = r;
        this.colorG = g;
        this.colorB = b;
    }
}
