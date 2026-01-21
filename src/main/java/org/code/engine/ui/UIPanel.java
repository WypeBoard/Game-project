package org.code.engine.ui;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public final class UIPanel implements UIElement {

    private final UIBounds bounds;
    private final List<UIElement> children = new ArrayList<>();

    private boolean visible = true;
    private boolean enabled = true;

    private float backgroundR = 0.1f;
    private float backgroundG = 0.1f;
    private float backgroundB = 0.2f;
    private float backgroundA = 0.8f;

    public UIPanel(float x, float y, float width, float height) {
        this.bounds = new UIBounds(x, y, width, height);
    }

    public void addChild(UIElement child) {
        children.add(child);
    }

    public void removeChild(UIElement child) {
        children.remove(child);
    }

    public void clearChildren() {
        children.clear();
    }

    public List<UIElement> getChildren() {
        return new ArrayList<>(children);
    }

    @Override
    public void update(double delta) {
        if (!visible) {
            return;
        }

        children.stream()
                .filter(UIElement::isEnabled)
                .forEach(child -> child.update(delta));
    }

    @Override
    public void render() {
        if (!visible) {
            return;
        }

        // Render background
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        float x = bounds.getX();
        float y = bounds.getY();
        float width = bounds.getWidth();
        float height = bounds.getHeight();

        GL11.glColor4f(backgroundR, backgroundG, backgroundB, backgroundA);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();

        GL11.glDisable(GL11.GL_BLEND);

        // Render all children
        children.forEach(UIElement::render);
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

    public UIBounds getBounds() {
        return bounds;
    }

    public void setBackgroundColor(float r, float g, float b, float a) {
        this.backgroundR = r;
        this.backgroundG = g;
        this.backgroundB = b;
        this.backgroundA = a;
    }
}
