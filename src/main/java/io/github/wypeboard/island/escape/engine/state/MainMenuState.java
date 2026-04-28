package io.github.wypeboard.island.escape.engine.state;

import io.github.wypeboard.island.escape.engine.GameLoop;
import io.github.wypeboard.island.escape.engine.graphics.TextRenderer;
import io.github.wypeboard.island.escape.engine.graphics.ViewportManager;
import io.github.wypeboard.island.escape.engine.ui.UIAnchor;
import io.github.wypeboard.island.escape.engine.ui.UIButton;
import io.github.wypeboard.island.escape.engine.ui.UIManager;
import io.github.wypeboard.island.escape.engine.ui.UIPanel;
import org.lwjgl.opengl.GL11;

public class MainMenuState extends GameState {

    private UIManager uiManager;
    private UIPanel menuPanel;

    @Override
    public void init() {
        System.out.println("Main menu initialized");

        ViewportManager viewport = ViewportManager.getInstance();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, viewport.getWidth(), viewport.getHeight(), 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);

        uiManager = UIManager.getInstance();
        setupUI();
    }

    private void setupUI() {
        ViewportManager viewport = ViewportManager.getInstance();
        float panelWidth = 300;
        float panelHeight = 300;
        float panelX = viewport.getCenterX() - panelWidth / 2;
        float panelY = viewport.getCenterY() - panelHeight / 2;

        menuPanel = new UIPanel(panelX, panelY, panelWidth, panelHeight);
        float buttonWidth = 200;
        float buttonHeight = 40;
        float buttonSpacing = 60;
        float startY = -buttonSpacing; // Offset from center

        UIButton playButton = new UIButton(
                UIAnchor.center(-buttonWidth / 2, startY),
                buttonWidth, buttonHeight,
                "Play",
                () -> {
                    System.out.println("Starting the game...");
                    GameStateManager.getInstance().setState(new PlayState());
                }
        );
        UIButton settingsButton = new UIButton(
                UIAnchor.center(-buttonWidth / 2, startY + buttonSpacing),
                buttonWidth, buttonHeight,
                "Settings",
                () -> {
                    System.out.println("Settings.. Out of order");
                }
        );

        UIButton exitButton = new UIButton(
                UIAnchor.center(-buttonWidth / 2, startY + buttonSpacing * 2),
                buttonWidth, buttonHeight,
                "Exit",
                () -> {
                    System.out.println("Exiting game");
                    GameLoop.getInstance().stopGameLoop();
                }
        );

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

        ViewportManager viewport = ViewportManager.getInstance();
        TextRenderer textRenderer = TextRenderer.getInstance();

        textRenderer.drawTextCentered(
                "TRANSPORT TYCOON",
                viewport.getCenterX(),
                viewport.getCenterY() - 150,
                2.5f,
                1.0f, 1.0f, 0.0f
        );

        textRenderer.drawTextCentered(
                "Prototype",
                viewport.getCenterX(),
                viewport.getCenterY() - 110,
                1.2f,
                0.8f, 0.8f, 0.8f
        );


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
