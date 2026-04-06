package org.code.game.entity;

import org.code.engine.ui.UIBounds;

/**
 * Anything that exists in the world and needs to update + render each frame.
 * Deliberately minimal
 */
public interface Entity {

    void update(double deltaTime);

    void render();

    /**
     * Used by PlayState to check proximity for interaction, collision, etc.
     */
    UIBounds getBounds();

    boolean isActive();
}
