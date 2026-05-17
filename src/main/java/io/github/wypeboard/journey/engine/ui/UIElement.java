package io.github.wypeboard.journey.engine.ui;

public interface UIElement {

    void update(double delta);

    void render();

    boolean isVisible();

    void setVisible(boolean visible);

    boolean isEnabled();

    void setEnabled(boolean enabled);
}
