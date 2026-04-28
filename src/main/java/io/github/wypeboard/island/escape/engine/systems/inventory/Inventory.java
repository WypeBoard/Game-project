package io.github.wypeboard.island.escape.engine.systems.inventory;

import java.util.HashMap;
import java.util.Map;

public final class Inventory {

    private final Map<Item, Integer> items = new HashMap<>();

    public void add(Item item, int amount) {
        items.merge(item, amount, Integer::sum);
    }

    /**
     * TODO Initial version. Should return true/false, if the item could be removed (is current > amount?)
     */
    public void remove(Item item, int amount) {
        int current = getCount(item);
        if (current <= 0) {
            return;
        }
        items.put(item, Math.max(0, current - amount));
    }

    public boolean hasItem(Item item, int amount) {
        return getCount(item) >= amount;
    }

    public int getCount(Item item) {
        return items.getOrDefault(item, 0);
    }
}
