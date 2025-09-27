package org.code.engine.state;

import org.code.engine.GameLoop;
import org.code.engine.input.InputManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class MainMenuState extends GameState {

    private int selectedOption = 0;
    private final String[] menuOptions = {"Play", "Settings", "Exit"};

    @Override
    public void init() {
        System.out.println("Main menu initialized");

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
    }

    @Override
    public void update(double deltaTime) {
        InputManager input = InputManager.getInstance();

        if (input.isKeyJustPressed(GLFW.GLFW_KEY_W)
                || input.isKeyJustPressed(GLFW.GLFW_KEY_UP)) {
            // -1 +length as we cannot allow the value to become negative
            selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length;
            System.out.println("Selected: " + menuOptions[selectedOption]);
        }
        if (input.isKeyJustPressed(GLFW.GLFW_KEY_S)
                || input.isKeyJustPressed(GLFW.GLFW_KEY_DOWN)) {
            selectedOption = (selectedOption + 1) % menuOptions.length;
            System.out.println("Selected: " + menuOptions[selectedOption]);
        }
        if (input.isKeyJustPressed(GLFW.GLFW_KEY_ENTER)) {
            handleMenuSelection();
        }
    }

    private void handleMenuSelection() {
        switch (selectedOption) {
            case 0:
                System.out.println("Starting game....");
                GameStateManager.getInstance().setState(new PlayState());
                break;
            case 1:
                break;
            case 2:
                System.out.println("Existing game");
                GameLoop.getInstance().stopGameLoop();
                break;
            default:
                throw new UnsupportedOperationException("Selected value is supported");
        }
    }

    @Override
    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        float startY = 200;
        float itemHeight = 60;

        for (int i = 0; i < menuOptions.length; i++) {
            float y = startY + (i * itemHeight);

            if (i == selectedOption) {
                GL11.glColor3f(0.8f, 0.8f,0.2f);
            } else {
                GL11.glColor3f(0.5f, 0.5f, 0.7f);
            }

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(300, y);
            GL11.glVertex2f(300, y + 40);
            GL11.glVertex2f(500, y);
            GL11.glVertex2f(500, y + 40);
            GL11.glEnd();
        }
        // Draw title (simple rectangle)
        GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(350, 100);
        GL11.glVertex2f(450, 100);
        GL11.glVertex2f(450, 140);
        GL11.glVertex2f(350, 140);
        GL11.glEnd();
    }

    @Override
    public void cleanup() {

    }
}
