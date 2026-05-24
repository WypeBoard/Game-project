package io.github.wypeboard.journey.game.systems;

import io.github.wypeboard.journey.engine.graphics.world.GridRenderer;
import io.github.wypeboard.journey.engine.systems.PlayStateSystem;
import io.github.wypeboard.journey.game.scenario.Scenario;
import io.github.wypeboard.journey.game.scenario.ScenarioContext;
import io.github.wypeboard.journey.game.scenario.ScenarioId;
import io.github.wypeboard.journey.game.world.LoadedWorld;

public class WorldCreationSystem implements PlayStateSystem {
    private final Scenario scenario;
    private final ScenarioId scenarioId;
    private final LoadedWorld loadedWorld;
    private final GridRenderer gridRenderer;

    public WorldCreationSystem(ScenarioContext context) {
        this.scenario = context.getCurrentScenario();
        this.scenarioId = context.getCurrentScenario().id();
        this.loadedWorld = context.getLoadedWorld();
        this.gridRenderer = new GridRenderer(this.loadedWorld.getGrid(), this.scenario.tileSize());
    }

    @Override
    public void init() {

    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render() {

    }

    @Override
    public void cleanup() {

    }
}
