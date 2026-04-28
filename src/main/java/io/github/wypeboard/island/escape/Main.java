package io.github.wypeboard.island.escape;

import io.github.wypeboard.island.escape.engine.GameLoop;

public class Main {

    public static void main(String[] args) {
        GameLoop gameLoop = GameLoop.getInstance();
        gameLoop.run();
    }
}