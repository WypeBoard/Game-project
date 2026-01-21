package org.code.engine.graphics.world;

public enum TileType {

    GRASS("grass", 0.2f, 0.6f, 0.2f),
    WATER("water", 0.2f, 0.4f, 0.8f),
    DIRT("dirt", 0.6f, 0.4f, 0.2f),
    STONE("stone", 0.5f, 0.5f, 0.5f),
    SAND("sand", 0.9f, 0.8f, 0.5f);

    private final String textureName;
    private final float fallbackR, fallbackG, fallbackB;

    TileType(String textureName, float fallbackR, float fallbackG, float fallbackB) {
        this.textureName = textureName;
        this.fallbackR = fallbackR;
        this.fallbackG = fallbackG;
        this.fallbackB = fallbackB;
    }

    public String getTextureName() {
        return textureName;
    }

    public float getFallbackR() {
        return fallbackR;
    }

    public float getFallbackG() {
        return fallbackG;
    }

    public float getFallbackB() {
        return fallbackB;
    }
}
