package org.code.engine.input;

import org.code.utils.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.util.HashMap;
import java.util.Map;

public class MouseManager {

    private static MouseManager instance;

    // Mouse position
    private double mouseX = 0.0;
    private double mouseY = 0.0;
    private double lastMouseX = 0.0;
    private double lastMouseY = 0.0;

    // Mouse delta (movement since last frame)
    private double deltaX = 0.0;
    private double deltaY = 0.0;

    // Scroll wheel
    private double scrollX = 0.0;
    private double scrollY = 0.0;

    // Button states
    private final Map<Integer, Boolean> pressedButtons = new HashMap<>();
    private final Map<Integer, Boolean> justPressedButtons = new HashMap<>();
    private final Map<Integer, Boolean> justReleasedButtons = new HashMap<>();

    // Callbacks
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWScrollCallback scrollCallback;

    private MouseManager() {
        // Private constructor for singleton
    }

    public static MouseManager getInstance() {
        if (instance == null) {
            instance = new MouseManager();
        }
        return instance;
    }

    public void init(long windowHandle) {
        Logger.debug(getClass(), "Initializing mouse callbacks");
        cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xPos, double yPos) {
                mouseX = xPos;
                mouseY = yPos;
            }
        };
        GLFW.glfwSetCursorPosCallback(windowHandle, cursorPosCallback);

        mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                boolean isPressed = action != GLFW.GLFW_RELEASE;
                boolean wasPressed = pressedButtons.getOrDefault(button, false);

                pressedButtons.put(button, isPressed);
                justPressedButtons.put(button, !wasPressed && isPressed);
                justReleasedButtons.put(button, wasPressed && !isPressed);

                Logger.debug(getClass(), button + " BUTTON PRESSED at (" + mouseX + ", " + mouseY + ")");

            }
        };
        GLFW.glfwSetMouseButtonCallback(windowHandle, mouseButtonCallback);

        scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xOffset, double yOffset) {
                scrollX = xOffset;
                scrollY = yOffset;
            }
        };
        GLFW.glfwSetScrollCallback(windowHandle, scrollCallback);

        Logger.debug(getClass(), "Mouse callbacks initialized successfully");
    }

    public void update() {
        // Calculate delta offset
        deltaX = mouseX - lastMouseX;
        deltaY = mouseY - lastMouseY;

        // Update last position
        lastMouseX = mouseX;
        lastMouseY = mouseY;

        if (!justPressedButtons.isEmpty()) {
            Logger.debugThrottled(getClass(),"mouse_just_pressed_clear",
                    "Clearing justPressed buttons: " + justPressedButtons.keySet());
        }
        // Clear single-frame events
        justPressedButtons.clear();
        justReleasedButtons.clear();

        // Reset scroll
        scrollX = 0.0;
        scrollY = 0.0;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    // === Button Methods ===

    public boolean isButtonPressed(int button) {
        return pressedButtons.getOrDefault(button, false);
    }

    public boolean isButtonJustPressed(int button) {
        Boolean result = justPressedButtons.getOrDefault(button, false);
        if (result && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            Logger.debug(getClass(), "isButtonJustPressed(LEFT) returning TRUE");
        }
        return result;
    }

    public boolean isButtonJustReleased(int button) {
        return justReleasedButtons.getOrDefault(button, false);
    }

    // Convenience methods for common buttons
    public boolean isLeftButtonPressed() {
        return isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT);
    }

    public boolean isLeftButtonJustPressed() {
        return isButtonJustPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT);
    }

    public boolean isRightButtonPressed() {
        return isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
    }

    public boolean isRightButtonJustPressed() {
        return isButtonJustPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
    }

    public boolean isMiddleButtonPressed() {
        return isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_MIDDLE);
    }

    // === Scroll Methods ===

    public double getScrollX() {
        return scrollX;
    }

    public double getScrollY() {
        return scrollY;
    }

    public boolean isScrollingUp() {
        return scrollY > 0;
    }

    public boolean isScrollingDown() {
        return scrollY < 0;
    }

    /**
     * Check if mouse is within a rectangular area
     */
    public boolean isMouseInRect(double x, double y, double width, double height) {
        return mouseX >= x && mouseX <= x + width &&
                mouseY >= y && mouseY <= y + height;
    }

    /**
     * Cleanup callbacks
     */
    public void cleanup() {
        if (cursorPosCallback != null) {
            cursorPosCallback.free();
        }
        if (mouseButtonCallback != null) {
            mouseButtonCallback.free();
        }
        if (scrollCallback != null) {
            scrollCallback.free();
        }
    }
}
