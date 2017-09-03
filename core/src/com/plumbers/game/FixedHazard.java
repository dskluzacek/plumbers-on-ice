package com.plumbers.game;

/** 
 * Used to represent a non-moving hazard that has one collision box.
 */
public class FixedHazard implements InteractiveMapObject
{
    private final Rectangle rectangle;

    public FixedHazard(Rectangle rectangle)
    {
        this.rectangle = new Rectangle(rectangle);
    }

    public final Rectangle getRectangle()
    {
        return rectangle;
    }

    @Override
    public final void activate(IPlayer p, Rectangle.Collision coll, int t)
    {
        p.kill();
    }
}
