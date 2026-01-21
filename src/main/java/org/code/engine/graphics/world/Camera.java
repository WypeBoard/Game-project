package org.code.engine.graphics.world;

public final class Camera {

    private float x;
    private float y;
    private float zoom;

    private final float minZoom;
    private final float maxZoom;

    public Camera(float x, float y, float zoom) {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
        this.minZoom = 0.5f;
        this.maxZoom = 3.0f;
    }

    public void move(float deltaX, float deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void adjustZoom(float delta) {
        this.zoom += delta;

        // Clamp zoom
        if (this.zoom < minZoom) {
            this.zoom = minZoom;
        }
        if (this.zoom > maxZoom) {
            this.zoom = maxZoom;
        }
    }

    public void setZoom(float zoom) {
        this.zoom = Math.max(minZoom, Math.min(maxZoom, zoom));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZoom() {
        return zoom;
    }

    /**
     * Convert screen coordinates to world coordinates
     */
    public float screenToWorldX(float screenX, float viewportWidth) {
        return (screenX - viewportWidth / 2) / zoom + x;
    }

    public float screenToWorldY(float screenY, float viewportHeight) {
        return (screenY - viewportHeight / 2) / zoom + y;
    }
}
