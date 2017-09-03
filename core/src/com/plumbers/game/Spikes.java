package com.plumbers.game;

public class Spikes implements TileObject
{
    private final Rectangle rectangle;
    
    public Spikes(int column, int row)
    {
        rectangle = new Rectangle( column * Block.SIZE + 2 * Level.UNIT_SCALE,
                                   row * Block.SIZE + 10 * Level.UNIT_SCALE,
                                   12 * Level.UNIT_SCALE,
                                   6 * Level.UNIT_SCALE  );
    }

    @Override
    public final boolean isSolidTo(CharacterType ct)
    {
        return false;
    }

    @Override
    public void onCollision(IPlayer p, Rectangle.Collision coll)
    {
        p.kill();
    }

    @Override
    public Rectangle getRectangle()
    {
        return rectangle;
    }
}
