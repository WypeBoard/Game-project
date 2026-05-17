package io.github.wypeboard.journey.engine.systems;

public interface PlayStateSystem {

    void init();

    void update(double deltaTime);

    void render();

    void cleanup();
}
