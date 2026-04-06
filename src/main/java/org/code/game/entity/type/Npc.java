package org.code.game.entity.type;

import org.code.engine.ui.UIBounds;
import org.code.game.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * A stationary NPC with hardcoded dialogue lines and a simple interaction state.
 * PlayState decides WHEN to call interact() — the NPC just manages its own state.
 */
public final class Npc implements Entity {

    private static final float SIZE = 24f;

    // Simple state machine — no framework needed for two states
    public enum State {
        IDLE,        // waiting for the player
        TALKING      // currently showing dialogue
    }

    private final float x;
    private final float y;
    private final String name;
    private final String[] dialogueLines;

    private State state = State.IDLE;
    private int dialogueIndex = 0;
    private boolean active = true;

    public Npc(float x, float y, String name, String[] dialogueLines) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.dialogueLines = dialogueLines;
    }

    @Override
    public void update(double deltaTime) {
        // Stationary for now — add patrol logic here later if wanted
    }

    @Override
    public void render() {
        float half = SIZE / 2f;

        // Body — orange so NPCs are visually distinct from the player
        GL11.glColor3f(0.95f, 0.6f, 0.1f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x - half, y - half);
        GL11.glVertex2f(x + half, y - half);
        GL11.glVertex2f(x + half, y + half);
        GL11.glVertex2f(x - half, y + half);
        GL11.glEnd();

        GL11.glColor3f(0.5f, 0.3f, 0.0f);
        GL11.glLineWidth(1.5f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(x - half, y - half);
        GL11.glVertex2f(x + half, y - half);
        GL11.glVertex2f(x + half, y + half);
        GL11.glVertex2f(x - half, y + half);
        GL11.glEnd();

        // Small indicator above NPC when player is nearby — rendered by PlayState,
        // but we draw a subtle "!" when in IDLE so the player knows to approach
    }

    @Override
    public UIBounds getBounds() {
        float half = SIZE / 2f;
        return new UIBounds(x - half, y - half, SIZE, SIZE);
    }

    @Override
    public boolean isActive() {
        return active;
    }


    /**
     * Advance through dialogue one line at a time.
     * Returns the current line to display, or null when dialogue is finished.
     */
    public String interact() {
        if (dialogueLines == null || dialogueLines.length == 0) {
            return null;
        }

        state = State.TALKING;
        String line = dialogueLines[dialogueIndex];
        dialogueIndex++;

        if (dialogueIndex >= dialogueLines.length) {
            dialogueIndex = 0; // loop back so it can be re-read
            state = State.IDLE;
            return line; // return the last line before resetting
        }

        return line;
    }

    /**
     * True when the player is close enough to interact.
     * PlayState calls this — keeps interaction radius logic out of NPC itself.
     */
    public boolean isInRange(float playerX, float playerY, float radius) {
        float dx = playerX - x;
        float dy = playerY - y;
        return (dx * dx + dy * dy) <= (radius * radius);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public State getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}

