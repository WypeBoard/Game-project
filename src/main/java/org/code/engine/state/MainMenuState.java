package org.code.engine.state;

import org.code.engine.GameLoop;
import org.code.engine.input.InputManager;
import org.code.engine.input.MouseManager;
import org.code.engine.ui.UIButton;
import org.code.engine.ui.UIManager;
import org.code.engine.ui.UIPanel;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class MainMenuState extends GameState {

    private UIManager uiManager;
    private UIPanel menuPanel;

    @Override
    public void init() {
        System.out.println("Main menu initialized");

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);

        uiManager = UIManager.getInstance();
        setupUI();
    }

    private void setupUI() {
        menuPanel = new UIPanel(250, 150, 300, 300);
        float buttonY = 200;
        float buttonSpacing = 60;

        UIButton playButton = new UIButton(300, buttonY, 200, 40, "Play", () -> {
            System.out.println("Starting the game...");
            GameStateManager.getInstance().setState(new PlayState());
        });

        UIButton settingsButton = new UIButton(300, buttonY + buttonSpacing, 200, 40, "Settings", () -> {
            System.out.println("Settings.. Out of order");
        });

        UIButton exitButton = new UIButton(300, buttonY + buttonSpacing * 2, 200, 40, "Exit", () -> {
            System.out.println("Exiting game");
            GameLoop.getInstance().stopGameLoop();
        });

        menuPanel.addChild(playButton);
        menuPanel.addChild(settingsButton);
        menuPanel.addChild(exitButton);

        uiManager.addElement(menuPanel);
    }

    @Override
    public void update(double deltaTime) {
        uiManager.update(deltaTime);
    }

    @Override
    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(350, 100);
        GL11.glVertex2f(450, 100);
        GL11.glVertex2f(450, 140);
        GL11.glVertex2f(350, 140);
        GL11.glEnd();

        uiManager.render();
    }

    @Override
    public void cleanup() {
        uiManager.clearAll();
    }

    @Override
    public void onExit() {
        super.onExit();
        uiManager.clearAll();
    }
}
