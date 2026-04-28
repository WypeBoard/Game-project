package io.github.wypeboard.island.escape.engine.systems.inventory;

public interface Item {

    /**
     * The name of the item. This will be used within the inventory/loot system.
     *
     * @return name of an item
     */
    String getName();

    /**
     * An in depth description of the item it self. This will be shown when the item has been hovered over
     *
     * @return an in depth description
     */
    String getDescription();

    /**
     * Defines the stack size of the item. Once the limit has been reached any additional item count will take up a
     * new inventory slot
     * <p>
     * Currently not in use
     *
     * @return default stack size 1000. Can be overridden if required.
     */
    default int stackSize() {
        return 1000;
    }

    ;
}
