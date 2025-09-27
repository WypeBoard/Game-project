package org.code.engine.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.HashMap;
import java.util.Map;

public class InputManager extends GLFWKeyCallback {

    private static InputManager instance;
    private final Map<Integer, Boolean> pressedKeys = new HashMap<>();
    private final Map<Integer, Boolean> justPressedKeys = new HashMap<>();
    private final Map<Integer, Boolean> justReleasedKeys = new HashMap<>();

    private InputManager() {
        // Private constructor for singleton
    }

    public static InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    /**
     * @param window   the window that received the event
     * @param key      the keyboard key that was pressed or released
     * @param scancode the platform-specific scancode of the key
     * @param action   the key action. One of:<br><table><tr><td>{@link GLFW#GLFW_PRESS PRESS}</td><td>{@link GLFW#GLFW_RELEASE RELEASE}</td><td>{@link GLFW#GLFW_REPEAT REPEAT}</td></tr></table>
     * @param mods     bitfield describing which modifiers keys were held down
     */
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        boolean isPressed = action != GLFW.GLFW_RELEASE;
        boolean wasPressed = pressedKeys.getOrDefault(key, false);

        pressedKeys.put(key, isPressed);

        // Track just pressed/released for single frame event
        justPressedKeys.put(key, !wasPressed && isPressed);
        justReleasedKeys.put(key, wasPressed && !isPressed);
    }

    public boolean isKeyPressed(int key) {
        return pressedKeys.getOrDefault(key, false);
    }

    public boolean isKeyJustPressed(int key) {
        return justPressedKeys.getOrDefault(key, false);
    }

    public boolean isKeyJustReleased(int key) {
        return justReleasedKeys.getOrDefault(key, false);
    }

    // Call this at the end of each frame to clear just pressed/released states
    public void update() {
        justPressedKeys.clear();
        justReleasedKeys.clear();
    }

    public void cleanup() {
        if (instance != null) {
            instance.free();
            instance = null;
        }
    }
}
