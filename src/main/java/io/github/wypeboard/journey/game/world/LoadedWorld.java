package io.github.wypeboard.journey.game.world;

import io.github.wypeboard.journey.engine.graphics.world.Grid;
import io.github.wypeboard.journey.game.entity.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class LoadedWorld {

    private final Grid grid;
    private final List<Entity> entities;
    private final float spawnX;
    private final float spawnY;
    private final int tileSize;
}
