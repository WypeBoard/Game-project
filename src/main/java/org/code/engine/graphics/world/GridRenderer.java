package org.code.engine.graphics.world;

import org.code.engine.graphics.Texture;
import org.code.engine.graphics.TileTextureManager;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

public final class GridRenderer {

    private final Grid grid;
    private final float tileSize;
    private Optional<TileTextureManager> textureManager;

    public GridRenderer(Grid grid, float tileSize) {
        this.grid = grid;
        this.tileSize = tileSize;
        this.textureManager = Optional.empty();
    }

    public void setTextureManager(TileTextureManager textureManager) {
        this.textureManager = Optional.of(textureManager);
    }

    public void renderer(Camera camera, int viewportWidth, int viewportHeight) {
        GL11.glPushMatrix();

        float zoom = camera.getZoom();
        GL11.glTranslatef(viewportWidth / 2f, viewportHeight / 2f, 0);
        GL11.glScalef(zoom, zoom, 1);
        GL11.glTranslatef(-camera.getX(), -camera.getY(), 0);

        float worldLeft = camera.screenToWorldX(0, viewportWidth);
        float worldRight = camera.screenToWorldX(viewportWidth, viewportWidth);
        float worldTop = camera.screenToWorldY(0, viewportHeight);
        float worldBottom = camera.screenToWorldY(viewportHeight, viewportHeight);

        int startX = Math.max(0, (int) (worldLeft / tileSize));
        int endX = Math.min(grid.getWidth() - 1, (int) (worldRight / tileSize) + 1);
        int startY = Math.max(0, (int) (worldTop / tileSize));
        int endY = Math.min(grid.getHeight() - 1, (int) (worldBottom / tileSize) + 1);

        boolean useTextures = textureManager.map(TileTextureManager::hasAnyTextures).orElse(false);

        if (useTextures) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y < endY; y++) {
                Optional<Tile> optionalTile = grid.getTile(x, y);
                optionalTile.ifPresent(tile -> {
                    if (useTextures) {
                        renderTileTextured(tile);
                    } else {
                        renderTileColored(tile);
                    }
                });
            }
        }

        if (useTextures) {
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }

        renderGridLines(startX, endX, startY, endY);

        GL11.glPopMatrix();
    }

    private void renderTileTextured(Tile tile) {
        TileTextureManager tileTextureManager = textureManager.orElseThrow(() -> new RuntimeException("Unable to procede without texture Manager"));
        Texture texture = tileTextureManager.getTexture(tile.getTileType(), tile.getTextureVariation());

        if (texture == null) {
            renderTileColored(tile);
            return;
        }

        texture.bind();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        float x = tile.getGridX() * tileSize;
        float y = tile.getGridY() * tileSize;

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(x, y);

        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(x + tileSize, y);

        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(x + tileSize, y + tileSize);

        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(x, y + tileSize);
        GL11.glEnd();

        texture.unbind();
    }

    private void renderTileColored(Tile tile) {
        TileType type = tile.getTileType();
        GL11.glColor3f(type.getFallbackR(), type.getFallbackG(), type.getFallbackB());

        float x = tile.getGridX() * tileSize;
        float y = tile.getGridY() * tileSize;

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + tileSize, y);
        GL11.glVertex2f(x + tileSize, y + tileSize);
        GL11.glVertex2f(x, y + tileSize);
        GL11.glEnd();
    }

    private void renderGridLines(int startX, int endX, int startY, int endY) {
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.3f);
        GL11.glLineWidth(1.0f);

        GL11.glBegin(GL11.GL_LINES);

        for (int x = startX; x <= endX + 1; x++) {
            float xPos = x * tileSize;
            GL11.glVertex2f(xPos, startY * tileSize);
            GL11.glVertex2f(xPos, (endY + 1) * tileSize);
        }

        for (int y = startY; y <= endY + 1; y++) {
            float yPos = y * tileSize;
            GL11.glVertex2f(startX * tileSize, yPos);
            GL11.glVertex2f((endX + 1) * tileSize, yPos);
        }

        GL11.glEnd();
    }
}
