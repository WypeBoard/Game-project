package io.github.wypeboard.island.escape.engine.state;

import io.github.wypeboard.island.escape.engine.graphics.TextRenderer;
import io.github.wypeboard.island.escape.engine.graphics.TileTextureManager;
import io.github.wypeboard.island.escape.engine.graphics.WindowManager;
import io.github.wypeboard.island.escape.engine.graphics.world.Camera;
import io.github.wypeboard.island.escape.engine.graphics.world.Grid;
import io.github.wypeboard.island.escape.engine.graphics.world.GridRenderer;
import io.github.wypeboard.island.escape.engine.graphics.world.TileType;
import io.github.wypeboard.island.escape.engine.input.InputManager;
import io.github.wypeboard.island.escape.engine.input.MouseManager;
import io.github.wypeboard.island.escape.game.entity.EntityManager;
import io.github.wypeboard.island.escape.game.entity.type.Npc;
import io.github.wypeboard.island.escape.game.entity.type.Player;
import io.github.wypeboard.island.escape.game.resources.ItemType;
import io.github.wypeboard.island.escape.utils.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class PlayState extends GameState {

    private static final int WORLD_WIDTH = 64;
    private static final int WORLD_HEIGHT = 64;
    private static final int TILE_SIZE = 32;
    // Starting island size (radius from center)
    private static final int ISLAND_RADIUS = 3;

    // How close the player must be to trigger NPC dialogue (world pixels)
    private static final float INTERACT_RADIUS = 48f;

    private Grid grid;
    private GridRenderer gridRenderer;
    private Camera camera;
    private Player player;
    private EntityManager entityManager;

    // The one NPC for the MVP quest — held separately so PlayState can check it directly
    private Npc questNPC;

    // Current dialogue line being shown, null when no dialogue active
    private String activeDialogue = null;

    private static WindowManager windowManager;
    private static InputManager inputManager;
    private static MouseManager mouseManager;
    private static TextRenderer textRenderer;
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
        setupEntities();
    }

    private void initManagers() {
        windowManager = WindowManager.getInstance();
        inputManager = InputManager.getInstance();
        mouseManager = MouseManager.getInstance();
        textRenderer = TextRenderer.getInstance();
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
                    } else if (dx >= ISLAND_RADIUS -1 && dy == ISLAND_RADIUS - 1) {
                        grid.setTile(x, y, TileType.TREE);
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
        camera = new Camera(worldCX, worldCY, 1.5f);
    }

    private void setupEntities() {
        entityManager = new EntityManager();

        // Spawn player at the island centre
        float spawnX = (WORLD_WIDTH  / 2f) * TILE_SIZE + TILE_SIZE / 2f;
        float spawnY = (WORLD_HEIGHT / 2f) * TILE_SIZE + TILE_SIZE / 2f;
        player = new Player(spawnX, spawnY, grid, TILE_SIZE);
        entityManager.add(player);

        // Spawn the quest NPC one tile north of the player
        float npcX = spawnX;
        float npcY = spawnY - TILE_SIZE * 2;
        questNPC = new Npc(npcX, npcY, "Fisherman",
                new String[]{
                        "Hey! You look new here.",
                        "This island has been quiet since the storm.",
                        "Bring me 10 wood and I'll tell you what I know."
                }
        );
        entityManager.add(questNPC);
    }


    @Override
    public void update(double deltaTime) {
        entityManager.update(deltaTime);
        handleCameraFollow();
        handleCameraZoom();
        handleInteraction();
    }

    /**
     * Camera smoothly follows the player.
     */
    private void handleCameraFollow() {
        float targetX = player.getX();
        float targetY = player.getY();

        // Lerp for a soft follow feel — 0.1f is gentle, raise toward 1.0f for snappy
        float lerp = 0.1f;
        float currentX = camera.getX();
        float currentY = camera.getY();

        camera.setPosition(
                currentX + (targetX - currentX) * lerp,
                currentY + (targetY - currentY) * lerp
        );
    }

    private void handleCameraZoom() {
        MouseManager mouse = MouseManager.getInstance();
        if (mouse.isScrollingUp()) {
            camera.adjustZoom( ZOOM_SPEED * camera.getZoom());
        }
        if (mouse.isScrollingDown()) {
            camera.adjustZoom(-ZOOM_SPEED * camera.getZoom());
        }
    }

    private void handleInteraction() {
        if (!inputManager.isKeyJustPressed(GLFW.GLFW_KEY_E)) {
            return;
        }

        // If dialogue is already showing, dismiss it first
        if (activeDialogue != null) {
            activeDialogue = null;
            return;
        }

        if (questNPC.isInRange(player.getX(), player.getY(), INTERACT_RADIUS)) {
            activeDialogue = questNPC.interact();
        }
    }

    @Override
    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        int w = windowManager.getWidth();
        int h = windowManager.getHeight();

        gridRenderer.renderer(camera, w, h);
        renderEntitiesInWorldSpace(w, h);

        // HUD (screen space — no camera transform)
        renderHUD();
    }

    /**
     * Push the same camera matrix the grid uses so entities sit correctly on tiles.
     */
    private void renderEntitiesInWorldSpace(int viewportW, int viewportH) {
        GL11.glPushMatrix();

        float zoom = camera.getZoom();
        GL11.glTranslatef(viewportW / 2f, viewportH / 2f, 0);
        GL11.glScalef(zoom, zoom, 1);
        GL11.glTranslatef(-camera.getX(), -camera.getY(), 0);

        entityManager.render();

        GL11.glPopMatrix();
    }

    /**
     * Dialogue box and any other HUD elements — rendered in screen space after the world.
     */
    private void renderHUD() {
        if (activeDialogue != null) {
            renderDialogueBox(activeDialogue);
        }

        // "Press E" hint when near NPC and not already talking
        if (activeDialogue == null
                && questNPC.isInRange(player.getX(), player.getY(), INTERACT_RADIUS)) {
            renderInteractHint();
        }

        renderInventory();
    }

    private void renderInventory() {
        TextRenderer textRenderer = TextRenderer.getInstance();
        textRenderer.drawText(
                ItemType.WOOD.getName() +": " + player.getInventory().getCount(ItemType.WOOD),
                20, 20, 1.0f, 1f, 1f, 1f
        );
    }


    private void renderDialogueBox(String text) {
        int w = windowManager.getWidth();
        int h = windowManager.getHeight();

        float boxH  = 80f;
        float boxY  = h - boxH - 20f;
        float boxX  = 40f;
        float boxW  = w - 80f;

        // Semi-transparent dark background
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0.05f, 0.05f, 0.1f, 0.85f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(boxX,        boxY);
        GL11.glVertex2f(boxX + boxW, boxY);
        GL11.glVertex2f(boxX + boxW, boxY + boxH);
        GL11.glVertex2f(boxX,        boxY + boxH);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);

        // Border
        GL11.glColor3f(0.6f, 0.5f, 0.2f);
        GL11.glLineWidth(1.5f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(boxX,        boxY);
        GL11.glVertex2f(boxX + boxW, boxY);
        GL11.glVertex2f(boxX + boxW, boxY + boxH);
        GL11.glVertex2f(boxX,        boxY + boxH);
        GL11.glEnd();

        // "Press E to continue" hint in corner
        // Text rendering is still placeholder blocks — replace when bitmap fonts land
        textRenderer.drawText(text, boxX + 16, boxY + 20, 1.2f, 1f, 1f, 0.9f);
        textRenderer.drawText("[E] continue", boxX + boxW - 120, boxY + boxH - 20, 1f, 0.6f, 0.6f, 0.6f);
    }

    private void renderInteractHint() {
        // Small indicator above NPC in world space would be ideal,
        // but a simple screen-space prompt works fine for now.
        // Replace with textRenderer.drawText("[E]", ...) once fonts are ready.
    }

    @Override
    public void cleanup() {
        entityManager.clear();
    }
}
