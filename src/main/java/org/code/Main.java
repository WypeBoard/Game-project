package org.code;

import org.code.engine.GameLoop;

public class Main {

    public static void main(String[] args) {
        GameLoop gameLoop = GameLoop.getInstance();
        gameLoop.run();
    }
}