package io.github.wypeboard.journey.game.world;

import io.github.wypeboard.journey.engine.graphics.world.Tile;

public class BiomeTileRules {

    public static boolean isTileWalkable(Tile tile) {
        BiomeTileType tileType = BiomeTileType.valueOf(tile.getTileType().getTileName());

        return tileType.isWalkable();
    }
}
