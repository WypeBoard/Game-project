package org.code.engine.state;

import org.code.engine.graphics.TileTextureManager;
import org.code.engine.graphics.WindowManager;
import org.code.engine.graphics.world.Camera;
import org.code.engine.graphics.world.Grid;
import org.code.engine.graphics.world.GridRenderer;
import org.code.engine.graphics.world.TileType;
import org.code.engine.input.InputManager;
import org.code.engine.input.MouseManager;
import org.code.utils.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class PlayState extends GameState {

    private static final int WORLD_WIDTH = 64;
    private static final int WORLD_HEIGHT = 64;
    private static final float TILE_SIZE = 32f;

    // Starting island size (radius from center)
    private static final int ISLAND_RADIUS = 3;

    private Grid grid;
    private GridRenderer gridRenderer;
    private Camera camera;

    private static WindowManager windowManager;
    private static InputManager inputManager;
    private static MouseManager mouseManager;

    // Camera pan speed (world units per second)
    private static final float PAN_SPEED  = 300f;
    private static final float ZOOM_SPEED = 0.05f;

    @Override
    public void init() {
        Logger.debug(getClass(), "Play state initialized");

        initManagers();
        setupProjection();
        buildWorld();
        setupCamera();
    }

    private void initManagers() {
        windowManager = WindowManager.getInstance();
        inputManager = InputManager.getInstance();
        mouseManager = MouseManager.getInstance();
    }

    private void setupProjection() {
        int w = windowManager.getWidth();
        int h = windowManager.getHeight();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        // y-down so tile (0,0) is top-left, matching grid convention
        GL11.glOrtho(0, w, h, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glClearColor(0.05f, 0.05f, 0.1f, 1.0f);
    }

    private void buildWorld() {
        grid = new Grid(WORLD_WIDTH, WORLD_HEIGHT);

        // Fill everything with water first
        for (int x = 0; x < WORLD_WIDTH; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                grid.setTile(x, y, TileType.WATER);
            }
        }

        // Carve out a small starting island in the centre
        int cx = WORLD_WIDTH  / 2;
        int cy = WORLD_HEIGHT / 2;

        for (int x = cx - ISLAND_RADIUS; x <= cx + ISLAND_RADIUS; x++) {
            for (int y = cy - ISLAND_RADIUS; y <= cy + ISLAND_RADIUS; y++) {
                // Circular-ish island using Chebyshev distance for a square-ish feel
                int dx = Math.abs(x - cx);
                int dy = Math.abs(y - cy);
                if (dx <= ISLAND_RADIUS && dy <= ISLAND_RADIUS) {
                    // Outer ring is sand, inner is dirt/grass
                    if (dx == ISLAND_RADIUS || dy == ISLAND_RADIUS) {
                        grid.setTile(x, y, TileType.SAND);
                    } else if (dx >= ISLAND_RADIUS - 1 || dy >= ISLAND_RADIUS - 1) {
                        grid.setTile(x, y, TileType.DIRT);
                    } else {
                        grid.setTile(x, y, TileType.GRASS);
                    }
                }
            }
        }

        gridRenderer = new GridRenderer(grid, TILE_SIZE);

        // Wire up textures if available — falls back to colours gracefully
        TileTextureManager textures = new TileTextureManager("assets/textures/tiles");
        textures.loadTileTextures();
        if (textures.hasAnyTextures()) {
            gridRenderer.setTextureManager(textures);
        }
    }

    private void setupCamera() {
        // Start camera centred on the island
        float worldCX = (WORLD_WIDTH  / 2f) * TILE_SIZE;
        float worldCY = (WORLD_HEIGHT / 2f) * TILE_SIZE;
        camera = new Camera(worldCX, worldCY, 1.0f);
    }


    @Override
    public void update(double deltaTime) {
        handleCameraInput((float) deltaTime);
    }

    private void handleCameraInput(float dt) {
        InputManager input  = inputManager;
        MouseManager  mouse = mouseManager;

        float speed = PAN_SPEED / camera.getZoom(); // pan speed feels consistent at any zoom

        // WASD / arrow key panning
        if (input.isKeyPressed(GLFW.GLFW_KEY_W) || input.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            camera.move(0,      -speed * dt);
        }
        if (input.isKeyPressed(GLFW.GLFW_KEY_S) || input.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            camera.move(0,       speed * dt);
        }
        if (input.isKeyPressed(GLFW.GLFW_KEY_A) || input.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            camera.move(-speed * dt, 0);
        }
        if (input.isKeyPressed(GLFW.GLFW_KEY_D) || input.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            camera.move( speed * dt, 0);
        }

        // Scroll to zoom
        if (mouse.isScrollingUp()) {
            camera.adjustZoom( ZOOM_SPEED * camera.getZoom());
        }
        if (mouse.isScrollingDown()) {
            camera.adjustZoom(-ZOOM_SPEED * camera.getZoom());
        }
    }

    @Override
    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        int w = windowManager.getWidth();
        int h = windowManager.getHeight();

        gridRenderer.renderer(camera, w, h);
    }

    @Override
    public void cleanup() {

    }

}
