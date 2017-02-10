package com.plumbers.game;

public class Spikes extends FixedHazard
{
    public Spikes(int column, int row)
    {
        super( new Rectangle(column * Block.SIZE + 2 * Level.UNIT_SCALE,
                             row * Block.SIZE + 10 * Level.UNIT_SCALE,
                             12 * Level.UNIT_SCALE,
                             6 * Level.UNIT_SCALE) );
    }
}
