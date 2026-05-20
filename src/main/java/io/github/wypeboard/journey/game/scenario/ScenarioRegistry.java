package io.github.wypeboard.journey.game.scenario;

public class ScenarioRegistry {

    private ScenarioRegistry() {}

    public static Scenario get(ScenarioId id) {
        switch (id) {
            case TUTORIAL:
                return buildTutorial();
            default:
                throw new IllegalArgumentException("Unknown scenario: " + id);
        }
    }

    private static Scenario buildTutorial() {
        return Scenario.builder()
                .withId(ScenarioId.TUTORIAL)
                .withSeed(42L)
                .withWorldWidth(128)
                .withWorldHeight(128)
                .withTileSize(32)
                .withSpawnTileX(32)
                .withSpawnTileY(32)
                .build();
    }
}
