package io.github.wypeboard.journey;

import io.github.wypeboard.journey.engine.GameLoop;
import io.github.wypeboard.journey.game.state.MainMenuState;

public class Main {

    public static void main(String[] args) {
        GameLoop gameLoop = GameLoop.getInstance();
        gameLoop.run(MainMenuState::new);
    }
}