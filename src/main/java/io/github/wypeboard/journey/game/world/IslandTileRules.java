package io.github.wypeboard.island.escape.game.world;

import io.github.wypeboard.island.escape.engine.graphics.world.Tile;

public class IslandTileRules {


    public static boolean isTileWalkable(Tile tile) {
        IslandTileType tileType = IslandTileType.valueOf(tile.getTileType().getTileName());

        return tileType.isWalkable();
    }
}
