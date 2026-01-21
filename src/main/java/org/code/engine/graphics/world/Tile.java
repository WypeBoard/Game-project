package org.code.engine.graphics.world;

public final class Tile {

    private final int gridX;
    private final int gridY;
    private TileType tileType;
    private int textureVariation;

    public Tile(int gridX, int gridY, TileType tileType) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.tileType = tileType;
        this.textureVariation = 0;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public int getTextureVariation() {
        return textureVariation;
    }

    public void setTextureVariation(int textureVariation) {
        this.textureVariation = textureVariation;
    }
}
