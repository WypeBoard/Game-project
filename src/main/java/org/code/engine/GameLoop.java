package org.code.engine;

import org.code.engine.graphics.WindowManager;
import org.code.engine.input.InputManager;
import org.code.engine.state.GameStateManager;
import org.code.engine.state.MainMenuState;
import org.code.utils.Constants;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class GameLoop {

    private static GameLoop instance;
    private final WindowManager windowManager;
    private final InputManager inputManager;
    private final GameStateManager gameStateManager;
    private boolean running = false;

    // Frame rate control
    private final double TARGET_FPS = 60.0;
    private final double FRAME_TIME = 1.0 / TARGET_FPS;

    private GameLoop() {
        // Private constructor for singleton
        windowManager = WindowManager.getInstance();
        inputManager = InputManager.getInstance();
        gameStateManager = GameStateManager.getInstance();
        // Empty
    }

    public static GameLoop getInstance() {
        if (instance == null) {
            instance = new GameLoop();
        }
        return instance;
    }

    public void run() {
        try {
            init();
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private void init() {
        System.out.println("Initializing game engine...");

        windowManager.init();

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        gameStateManager.setState(new MainMenuState());

        running = true;
        System.out.println("Game engine initialized successfully!");
    }

    private void loop() {
        double lastFrameTime = GLFW.glfwGetTime();
        double accumulator = 0.0;
        // Main game loop
        while (running && !windowManager.shouldClose()) {
            double currentTime = GLFW.glfwGetTime();
            double deltaTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            accumulator += deltaTime;

            while (accumulator >= FRAME_TIME) {
                update(FRAME_TIME);
                accumulator -= FRAME_TIME;
            }

            render();

            windowManager.update();
        }
    }

    /**
     * Draw objects
     */
    private void render() {
        // Clear the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Render current game state
        gameStateManager.render();
    }

    /**
     * Handles the game logic, input, physics etc.
     *
     * @param deltaTime time since last frame
     */
    private void update(double deltaTime) {
        // Update input state
        inputManager.update();

        // Check for global commands (like quitting)
        handleGlobalInput();

        // Update current game state
        gameStateManager.update(deltaTime);
    }

    /**
     * Global input handling that works across all states
     */
    private void handleGlobalInput() {

    }

    private void cleanup() {
        System.out.println("Cleaning up the game engine");

        if (gameStateManager != null) {
            gameStateManager.cleanup();
        }
        if (windowManager != null) {
            windowManager.cleanup();
        }

        System.out.println("Game engine cleaned up successfully");
    }

    public void stopGameLoop() {
        running = false;
    }

    // Getters for other systems that might need access
    public WindowManager getWindowManager() {
        return windowManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public GameStateManager getStateManager() {
        return gameStateManager;
    }
}
