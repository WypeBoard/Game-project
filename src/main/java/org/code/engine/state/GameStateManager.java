package org.code.engine.state;

import java.util.Optional;

public class GameStateManager {

    private static GameStateManager instance;

    private GameState currentState ;
    private GameState nextState;

    private GameStateManager() {
        // Private constructor for singleton
    }

    public static GameStateManager getInstance() {
        if (instance == null) {
            instance = new GameStateManager();
        }
        return instance;
    }

    public void setState(GameState newState) {
        this.nextState = newState;
    }

    public void update(double deltaTime) {
        // Handle state transitions
        if (this.nextState != null) {
            if (this.currentState != null) {
                this.currentState.onExit();
            }
            this.currentState = this.nextState;
            this.currentState.onEnter();
            this.nextState = null;
        }

        // Update current state
        if (this.currentState != null) {
            currentState.update(deltaTime);
        }
    }

    public void render() {
        if (this.currentState != null) {
            this.currentState.render();
        }
    }

    public void cleanup() {
        if (currentState != null) {
            currentState.cleanup();
        }
    }

    public GameState getCurrentState() {
        return currentState;
    }
}
