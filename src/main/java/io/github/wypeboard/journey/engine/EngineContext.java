package io.github.wypeboard.journey.engine;

import io.github.wypeboard.journey.engine.graphics.TextRenderer;
import io.github.wypeboard.journey.engine.graphics.WindowManager;
import io.github.wypeboard.journey.engine.input.InputManager;
import io.github.wypeboard.journey.engine.input.MouseManager;

public final class EngineContext {

    private final WindowManager windowManager;
    private final InputManager inputManager;
    private final MouseManager mouseManager;
    private final TextRenderer textRenderer;

    public EngineContext(WindowManager windowManager, InputManager inputManager, MouseManager mouseManager, TextRenderer textRenderer) {
        this.windowManager = windowManager;
        this.inputManager = inputManager;
        this.mouseManager = mouseManager;
        this.textRenderer = textRenderer;
    }

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public MouseManager getMouseManager() {
        return mouseManager;
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }
}
