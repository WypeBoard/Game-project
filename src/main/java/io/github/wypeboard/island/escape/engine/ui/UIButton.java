package io.github.wypeboard.island.escape.engine.ui;

import io.github.wypeboard.island.escape.engine.graphics.TextRenderer;
import io.github.wypeboard.island.escape.engine.input.MouseManager;
import io.github.wypeboard.island.escape.utils.Logger;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

public final class UIButton implements UIElement {

    private final UIBounds bounds;
    private final String label;
    private final Runnable onClick;

    // Optional anchor for responsive positioning
    private Optional<UIAnchor> anchor = Optional.empty();

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

    private float textScale = 1.0f;
    private float textR = 1.0f;
    private float textG = 1.0f;
    private float textB = 1.0f;

    public UIButton(float x, float y, float width, float height, String label, Runnable onClick) {
        this.bounds = new UIBounds(x, y, width, height);
        this.label = label;
        this.onClick = onClick;
        Logger.debug(getClass(), "Created button '" + label + "' at (" + x + ", " + y + ") size (" + width + "x" + height + ")");
    }

    public UIButton(UIAnchor anchor, float width, float height, String label, Runnable onClick) {
        this.anchor = Optional.of(anchor);
        this.bounds = new UIBounds(0, 0, width, height);
        this.label = label;
        this.onClick = onClick;
        updatePosition();
        Logger.debug(getClass(), "Created anchored button '" + label + "' size (" + width + "x" + height + ")");
    }

    @Override
    public void update(double delta) {
        anchor.ifPresent(x -> updatePosition());

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

        if (label != null && !label.isEmpty()) {
            TextRenderer textRenderer = TextRenderer.getInstance();
            float centerX = x + width / 2;
            float centerY = y + (height - textRenderer.getTextHeight(textScale)) / 2;
            textRenderer.drawTextCentered(label, centerX, centerY, textScale, textR, textG, textB);
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

    public void setTextColor(float r, float g, float b) {
        this.textR = r;
        this.textG = g;
        this.textB = b;
    }

    public void setTextScale(float scale) {
        this.textScale = scale;
    }

    public void setAnchor(UIAnchor anchor) {
        this.anchor = Optional.ofNullable(anchor);
        updatePosition();
    }
}
