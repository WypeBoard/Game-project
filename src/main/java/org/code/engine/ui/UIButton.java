package org.code.engine.ui;

import org.code.engine.input.MouseManager;
import org.code.utils.Logger;
import org.lwjgl.opengl.GL11;

public final class UIButton implements UIElement {

    private final UIBounds bounds;
    private final String label;
    private final Runnable onClick;

    private boolean visible = true;
    private boolean enabled = true;
    private boolean hovered = false;


    private float normalR = 0.5f;
    private float normalG = 0.5f;
    private float normalB = 0.7f;

    private float disabledR = 0.3f;
    private float disabledG = 0.3f;
    private float disabledB = 0.3f;

    private float hoverR = 0.8f;
    private float hoverG = 0.8f;
    private float hoverB = 0.2f;

    public UIButton(float x, float y, float width, float height, String label, Runnable onClick) {
        this.bounds = new UIBounds(x, y, width, height);
        this.label = label;
        this.onClick = onClick;
        Logger.debug(getClass(), "Created button '" + label + "' at (" + x + ", " + y + ") size (" + width + "x" + height + ")");
    }

    @Override
    public void update(double delta) {
        if (!visible || !enabled) {
            hovered = false;
            return;
        }

        MouseManager mouseManager = MouseManager.getInstance();
        hovered = bounds.containsMouse();

        Logger.debugOnChange(getClass(), "button_" + label + "_hovered", hovered);
        // Log mouse position when hovering this button
        if (hovered) {
            Logger.debugThrottled(getClass(), "button_" + label + "_mouse_pos",
                    "Mouse over '" + label + "': (" + mouseManager.getMouseX() + ", " + mouseManager.getMouseY() + ")");
        }
        if (hovered && mouseManager.isLeftButtonJustPressed() && onClick != null) {
            Logger.debug(getClass(), "Button '" + label + "' CLICKED!");
            onClick.run();
        }
    }

    @Override
    public void render() {
        if (!visible) {
            return;
        }

        // Choose color based on state
        float r, g, b;
        if (!enabled) {
            r = disabledR;
            g = disabledG;
            b = disabledB;
        } else if (hovered) {
            r = hoverR;
            g = hoverG;
            b = hoverB;
        } else {
            r = normalR;
            g = normalG;
            b = normalB;
        }

        float x = bounds.getX();
        float y = bounds.getY();
        float width = bounds.getWidth();
        float height = bounds.getHeight();

        GL11.glColor3f(r, g, b);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();

        // TODO: Render text label (requires font system)
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

    public boolean isHovered() {
        return hovered;
    }

    public void setColors(float normalR, float normalG, float normalB,
                          float hoverR, float hoverG, float hoverB) {
        this.normalR = normalR;
        this.normalG = normalG;
        this.normalB = normalB;
        this.hoverR = hoverR;
        this.hoverG = hoverG;
        this.hoverB = hoverB;
    }
}
