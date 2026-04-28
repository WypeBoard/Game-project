package io.github.wypeboard.island.escape.game.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Owns all active entities. PlayState creates this and calls update/render each frame.
 * Not a singleton — PlayState owns it directly, keeping scope clear.
 */
public final class EntityManager {

    private final List<Entity> entities = new ArrayList<>();

    public void add(Entity entity) {
        entities.add(entity);
    }

    public void remove(Entity entity) {
        entities.remove(entity);
    }
    /**
     * Update all active entities, then remove any that became inactive this frame.
     */
    public void update(double deltaTime) {
        for (Entity entity : entities) {
            if (entity.isActive()) {
                entity.update(deltaTime);
            }
        }
        entities.removeIf(e -> !e.isActive());
    }

    /**
     * Render all active entities. Order matters — add z-sorting here later if needed.
     */
    public void render() {
        for (Entity entity : entities) {
            if (entity.isActive()) {
                entity.render();
            }
        }
    }

    /**
     * Convenience — returns all entities of a given type.
     * Useful for PlayState interaction checks without exposing the full list.
     */
    public <T extends Entity> List<T> getEntitiesOfType(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (Entity e : entities) {
            if (type.isInstance(e)) {
                result.add(type.cast(e));
            }
        }
        return result;
    }

    public void clear() {
        entities.clear();
    }
}
