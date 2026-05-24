package io.github.wypeboard.journey.game.scenario;

import io.github.wypeboard.journey.engine.graphics.world.Camera;
import io.github.wypeboard.journey.game.entity.type.Player;
import io.github.wypeboard.journey.game.world.LoadedWorld;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScenarioContext {

    private final LoadedWorld loadedWorld;
    private final Camera camera;
    private final Scenario currentScenario;
    private Player player;
    private String activeDialog;

    public ScenarioContext(LoadedWorld loadedWorld, Camera camera, Scenario currentScenario) {
        this.loadedWorld = loadedWorld;
        this.camera = camera;
        this.currentScenario = currentScenario;
    }

}
