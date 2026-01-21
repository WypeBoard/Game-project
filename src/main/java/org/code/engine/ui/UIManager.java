package org.code.engine.ui;

import java.util.ArrayList;
import java.util.List;

public final class UIManager {

    private static UIManager instance;
    private final List<UIElement> rootElements = new ArrayList<>();

    private UIManager() {
        // Private constructor for singleton
    }

    public static UIManager getInstance() {
        if (instance == null) {
            instance = new UIManager();
        }
        return instance;
    }

    public void addElement(UIElement element) {
        rootElements.add(element);
    }

    public void removeElement(UIElement element) {
        rootElements.remove(element);
    }

    public void clearAll() {
        rootElements.clear();
    }

    public void update(double deltaTime) {
        // Update in reverse order so top elements get priority
        for (int i = rootElements.size() - 1; i >= 0; i--) {
            rootElements.get(i).update(deltaTime);
        }
    }

    public void render() {
        rootElements.forEach(UIElement::render);
    }
}
