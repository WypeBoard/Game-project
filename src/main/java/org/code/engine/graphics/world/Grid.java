package org.code.engine.graphics.world;

import java.util.Optional;

public final class Grid {

    private final int width;
    private final int height;
    private final Tile[][] tiles;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];

        // Initialize all tiles
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.tiles[x][y] = new Tile(x, y, TileType.GRASS);
            }
        }
    }

    public Optional<Tile> getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return Optional.empty();
        }
        return Optional.of(tiles[x][y]);
    }

    public void setTile(int x, int y, TileType tileType) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            tiles[x][y].setTileType(tileType);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
