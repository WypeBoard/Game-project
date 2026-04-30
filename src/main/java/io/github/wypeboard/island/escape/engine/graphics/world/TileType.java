package io.github.wypeboard.island.escape.engine.graphics.world;

public interface TileType {

    String getTileName();

    String getTextureName();

    float getFallbackR();

    float getFallbackG();

    float getFallbackB();
}
