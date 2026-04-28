package io.github.wypeboard.island.escape.game.entity.type;

import io.github.wypeboard.island.escape.engine.graphics.TextRenderer;
import io.github.wypeboard.island.escape.engine.graphics.world.Grid;
import io.github.wypeboard.island.escape.engine.graphics.world.TileType;
import io.github.wypeboard.island.escape.engine.input.InputManager;
import io.github.wypeboard.island.escape.engine.systems.inventory.Inventory;
import io.github.wypeboard.island.escape.engine.ui.UIBounds;
import io.github.wypeboard.island.escape.game.entity.Entity;
import io.github.wypeboard.island.escape.game.resources.ItemType;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public final class Player implements Entity {

    private static final float SPEED = 120f; // world pixels per second
    private static final float SIZE = 24f;  // rendered square size

    private float x; // world position (centre)
    private float y;

    private final Grid grid;
    private final int tileSize;

    private final Inventory inventory = new Inventory();

    private boolean active = true;

    public Player(float startX, float startY, Grid grid, int tileSize) {
        this.x = startX;
        this.y = startY;
        this.grid = grid;
        this.tileSize = tileSize;
    }


    @Override
    public void update(double deltaTime) {
        InputManager input = InputManager.getInstance();
        float dt = (float) deltaTime;

        float dx = 0;
        float dy = 0;

        if (input.isKeyPressed(GLFW.GLFW_KEY_W) || input.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            dy -= SPEED * dt;
        }
        if (input.isKeyPressed(GLFW.GLFW_KEY_S) || input.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            dy += SPEED * dt;
        }
        if (input.isKeyPressed(GLFW.GLFW_KEY_A) || input.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            dx -= SPEED * dt;
        }
        if (input.isKeyPressed(GLFW.GLFW_KEY_D) || input.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            dx += SPEED * dt;
        }

        // Try each axis independently so the player slides along walls
        if (dx != 0 && canMoveTo(x + dx, y)) {
            x += dx;
        }
        if (dy != 0 && canMoveTo(x, y + dy)) {
            y += dy;
        }
    }

    @Override
    public void render() {
        renderPlayer();
    }

    private void renderPlayer() {
        float half = SIZE / 2f;
        // Body — bright green square so it's easy to spot
        GL11.glColor3f(0.2f, 0.9f, 0.3f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x - half, y - half);
        GL11.glVertex2f(x + half, y - half);
        GL11.glVertex2f(x + half, y + half);
        GL11.glVertex2f(x - half, y + half);
        GL11.glEnd();

        // Outline so the player is visible against bright tiles
        GL11.glColor3f(0.0f, 0.4f, 0.1f);
        GL11.glLineWidth(1.5f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(x - half, y - half);
        GL11.glVertex2f(x + half, y - half);
        GL11.glVertex2f(x + half, y + half);
        GL11.glVertex2f(x - half, y + half);
        GL11.glEnd();

        // Direction indicator — small dot at the top so you can see which way is "up"
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glPointSize(4f);
        GL11.glBegin(GL11.GL_POINTS);
        GL11.glVertex2f(x, y - half + 5f);
        GL11.glEnd();
    }

    @Override
    public UIBounds getBounds() {
        float half = SIZE / 2f;
        return new UIBounds(x - half, y - half, SIZE, SIZE);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    private boolean canMoveTo(float newX, float newY) {
        float half = (SIZE / 2f) - 2f; // slight inset so corner-grazing feels natural

        return isTileWalkable(newX - half, newY - half)
                && isTileWalkable(newX + half, newY - half)
                && isTileWalkable(newX + half, newY + half)
                && isTileWalkable(newX - half, newY + half);
    }

    private boolean isTileWalkable(float worldX, float worldY) {
        int tileX = (int) (worldX / tileSize);
        int tileY = (int) (worldY / tileSize);

        return grid.getTile(tileX, tileY)
                .map(tile -> TileType.getLandTiles().contains(tile.getTileType()))
                .orElse(false); // off-grid = not walkable
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
