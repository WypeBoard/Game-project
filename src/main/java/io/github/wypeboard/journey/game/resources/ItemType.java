package io.github.wypeboard.journey.game.resources;

import io.github.wypeboard.journey.engine.systems.inventory.Item;

public enum ItemType implements Item {
    WOOD("Wood", "Used for building ships");

    private final String name;
    private final String description;

    ItemType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
