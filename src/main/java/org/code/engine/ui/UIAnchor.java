package org.code.engine.ui;

import org.code.engine.graphics.ViewportManager;

public final class UIAnchor {

    public enum HorizontalAnchor {
        LEFT,      // Fixed distance from left edge
        CENTER,    // Centered horizontally
        RIGHT      // Fixed distance from right edge
    }

    public enum VerticalAnchor {
        TOP,       // Fixed distance from top edge
        CENTER,    // Centered vertically
        BOTTOM     // Fixed distance from bottom edge
    }

    private HorizontalAnchor horizontalAnchor;
    private VerticalAnchor verticalAnchor;
    private float offsetX;  // Offset from anchor point
    private float offsetY;  // Offset from anchor point

    public UIAnchor(HorizontalAnchor horizontal, VerticalAnchor vertical, float offsetX, float offsetY) {
        this.horizontalAnchor = horizontal;
        this.verticalAnchor = vertical;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Static factory methods for common anchor patterns
     */
    public static UIAnchor topLeft(float marginX, float marginY) {
        return new UIAnchor(HorizontalAnchor.LEFT, VerticalAnchor.TOP, marginX, marginY);
    }

    public static UIAnchor topCenter(float offsetX, float marginY) {
        return new UIAnchor(HorizontalAnchor.CENTER, VerticalAnchor.TOP, offsetX, marginY);
    }

    public static UIAnchor topRight(float marginX, float marginY) {
        return new UIAnchor(HorizontalAnchor.RIGHT, VerticalAnchor.TOP, -marginX, marginY);
    }

    public static UIAnchor centerLeft(float marginX, float offsetY) {
        return new UIAnchor(HorizontalAnchor.LEFT, VerticalAnchor.CENTER, marginX, offsetY);
    }

    public static UIAnchor center(float offsetX, float offsetY) {
        return new UIAnchor(HorizontalAnchor.CENTER, VerticalAnchor.CENTER, offsetX, offsetY);
    }

    public static UIAnchor centerRight(float marginX, float offsetY) {
        return new UIAnchor(HorizontalAnchor.RIGHT, VerticalAnchor.CENTER, -marginX, offsetY);
    }

    public static UIAnchor bottomLeft(float marginX, float marginY) {
        return new UIAnchor(HorizontalAnchor.LEFT, VerticalAnchor.BOTTOM, marginX, -marginY);
    }

    public static UIAnchor bottomCenter(float offsetX, float marginY) {
        return new UIAnchor(HorizontalAnchor.CENTER, VerticalAnchor.BOTTOM, offsetX, -marginY);
    }

    public static UIAnchor bottomRight(float marginX, float marginY) {
        return new UIAnchor(HorizontalAnchor.RIGHT, VerticalAnchor.BOTTOM, -marginX, -marginY);
    }

    /**
     * Calculate the actual screen X position based on current viewport
     */
    public float calculateX(float elementWidth) {
        ViewportManager viewport = ViewportManager.getInstance();

        switch (horizontalAnchor) {
            case LEFT:
                return offsetX;
            case CENTER:
                return viewport.getCenterX() + offsetX - elementWidth / 2;
            case RIGHT:
                return viewport.getWidth() + offsetX - elementWidth;
            default:
                return offsetX;
        }
    }

    /**
     * Calculate the actual screen Y position based on current viewport
     */
    public float calculateY(float elementHeight) {
        ViewportManager viewport = ViewportManager.getInstance();

        switch (verticalAnchor) {
            case TOP:
                return offsetY;
            case CENTER:
                return viewport.getCenterY() + offsetY - elementHeight / 2;
            case BOTTOM:
                return viewport.getHeight() + offsetY - elementHeight;
            default:
                return offsetY;
        }
    }

    /**
     * Update the anchor position (useful for animating or adjusting offsets)
     */
    public void setOffset(float x, float y) {
        this.offsetX = x;
        this.offsetY = y;
    }

    public void setHorizontalAnchor(HorizontalAnchor anchor) {
        this.horizontalAnchor = anchor;
    }

    public void setVerticalAnchor(VerticalAnchor anchor) {
        this.verticalAnchor = anchor;
    }

    public HorizontalAnchor getHorizontalAnchor() {
        return horizontalAnchor;
    }

    public VerticalAnchor getVerticalAnchor() {
        return verticalAnchor;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }
}
