package org.code.engine.state;

public abstract class GameState {

    protected boolean initialized = false;

    public abstract void init();
    public abstract void update(double deltaTime);
    public abstract void render();
    public abstract void cleanup();

    // Called when state becomes active
    public void onEnter() {
        if (!initialized) {
            init();
            initialized = true;
        }
    }

    /**
     * Called when state becomes inactive
     */
    public void onExit() {
        // Override if needed
    }

}
