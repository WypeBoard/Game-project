package io.github.wypeboard.island.escape.game.world;

import io.github.wypeboard.island.escape.engine.graphics.world.TileType;

import java.util.Set;

public enum IslandTileType implements TileType {
    GRASS("grass", 0.2f, 0.6f, 0.2f, true),
    WATER("water", 0.2f, 0.4f, 0.8f),
    DIRT("dirt", 0.6f, 0.4f, 0.2f, true),
    STONE("stone", 0.5f, 0.5f, 0.5f),
    SAND("sand", 0.9f, 0.8f, 0.5f, true),
    TREE("tree", 0.6f, 0.6f, 0.2f)
    ;

    private final String textureName;
    private final float fallbackR;
    private final float fallbackG;
    private final float fallbackB;
    private boolean walkable;

    IslandTileType(String textureName, float fallbackR, float fallbackG, float fallbackB) {
        this.textureName = textureName;
        this.fallbackR = fallbackR;
        this.fallbackG = fallbackG;
        this.fallbackB = fallbackB;
        this.walkable = false;
    }

    IslandTileType(String textureName, float fallbackR, float fallbackG, float fallbackB, boolean walkable) {
        this.textureName = textureName;
        this.fallbackR = fallbackR;
        this.fallbackG = fallbackG;
        this.fallbackB = fallbackB;
        this.walkable = walkable;
    }

    @Override
    public String getTileName() {
        return name();
    }

    @Override
    public String getTextureName() {
        return textureName;
    }

    @Override
    public float getFallbackR() {
        return fallbackR;
    }

    @Override
    public float getFallbackG() {
        return fallbackG;
    }

    @Override
    public float getFallbackB() {
        return fallbackB;
    }

    public boolean isWalkable() {
        return walkable;
    }
}
