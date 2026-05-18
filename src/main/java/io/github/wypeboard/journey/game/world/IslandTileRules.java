package io.github.wypeboard.journey.game.world;

import io.github.wypeboard.journey.engine.graphics.world.Tile;

public class IslandTileRules {

    public static boolean isTileWalkable(Tile tile) {
        IslandTileType tileType = IslandTileType.valueOf(tile.getTileType().getTileName());

        return tileType.isWalkable();
    }
}
