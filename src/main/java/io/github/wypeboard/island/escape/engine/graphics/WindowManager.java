package io.github.wypeboard.island.escape.engine.graphics;

import io.github.wypeboard.island.escape.engine.input.InputManager;
import io.github.wypeboard.island.escape.engine.input.MouseManager;
import io.github.wypeboard.island.escape.utils.Constants;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class WindowManager {

    private static WindowManager windowManager;
    private long windowHandle;
    private String title;
    private int width;
    private int height;
    private boolean vSync = true;
    private boolean resizable = true;

    private WindowManager() {
        this.title = Constants.TITLE;
        this.width = Constants.GAME_WIDTH;
        this.height = Constants.GAME_HEIGHT;
    }

    public static WindowManager getInstance() {
        if (windowManager == null) {
            windowManager = new WindowManager();
        }
        return windowManager;
    }

    public void init() {
        // Defining the error callback
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new RuntimeException("Unable to start GLFW");
        }

        createWindow();
        createCallbacks();
        showWindow();
    }

    private void createWindow() {
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, resizable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);

        // Create the window
        windowHandle = GLFW.glfwCreateWindow(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, Constants.TITLE, MemoryUtil.NULL, MemoryUtil.NULL);
        if (windowHandle == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the glfw window");
        }

        // Make the OpenGl context current
        GLFW.glfwMakeContextCurrent(windowHandle);
        // Enable v-sync
        GLFW.glfwSwapInterval(vSync ? 1 : 0);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }

    private void createCallbacks() {
        // Keyboard callback
        GLFW.glfwSetKeyCallback(windowHandle, InputManager.getInstance());

        // Mouse callbacks
        MouseManager.getInstance().init(windowHandle);

        // Resize callback
        GLFW.glfwSetFramebufferSizeCallback(windowHandle, (window, w, h) -> {
            this.width = w;
            this.height = h;
            GL11.glViewport(0, 0, w, h);

            // Update ViewportManager so UI can respond to size changes
            ViewportManager.getInstance().updateViewport(w, h);

            // Update projection matrix for new viewport size
            updateProjection();
        });

        // Get initial window size and update viewport manager
        int[] w = new int[1];
        int[] h = new int[1];
        GLFW.glfwGetFramebufferSize(windowHandle, w, h);
        this.width = w[0];
        this.height = h[0];
        ViewportManager.getInstance().updateViewport(this.width, this.height);
    }

    private void showWindow() {
        // Make the window visable
        GLFW.glfwShowWindow(this.windowHandle);
    }

    private void updateProjection() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
    }

    public void update() {
        GLFW.glfwSwapBuffers(windowHandle);
        GLFW.glfwPollEvents();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(windowHandle);
    }

    public void cleanup() {
        InputManager.getInstance().cleanup();
        MouseManager.getInstance().cleanup();
        GLFW.glfwDestroyWindow(windowHandle);
        GLFW.glfwTerminate();
        GLFWErrorCallback callback = GLFW.glfwSetErrorCallback(null);
        if (callback != null) {
            callback.free();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
