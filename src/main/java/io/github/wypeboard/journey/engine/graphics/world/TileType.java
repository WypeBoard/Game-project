package io.github.wypeboard.journey.engine.graphics.world;

public interface TileType {

    String getTileName();

    String getTextureName();

    float getFallbackR();

    float getFallbackG();

    float getFallbackB();
}
