package io.github.wypeboard.journey.game.scenario;

import lombok.Builder;

/**
 * @param next null = end of game
 */
@Builder(setterPrefix = "with")
public record Scenario(
        ScenarioId id,
        ScenarioId next,
        long seed,
        int worldWidth,
        int worldHeight,
        int tileSize,
        int spawnTileX,
        int spawnTileY) {
}
