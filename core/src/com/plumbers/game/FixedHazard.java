package com.plumbers.game;

/** 
 * Used to represent a non-moving hazard that has one collision box.
 */
public class FixedHazard implements Hazard {
    private final Rectangle rectangle;

    public FixedHazard(Rectangle rectangle) {
        this.rectangle = new Rectangle(rectangle);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
