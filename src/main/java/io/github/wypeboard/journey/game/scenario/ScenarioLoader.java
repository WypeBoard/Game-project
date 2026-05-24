package io.github.wypeboard.journey.game.scenario;

import io.github.wypeboard.journey.engine.graphics.world.Grid;
import io.github.wypeboard.journey.game.world.LoadedWorld;

public final class ScenarioLoader {

    public static LoadedWorld load(Scenario scenario) {
        Grid grid = buildGrid(scenario);
    }

    private static Grid buildGrid(Scenario scenario) {
        Grid grid = new Grid(scenario.worldWidth(), scenario.worldHeight());

        return null;
    }
}
