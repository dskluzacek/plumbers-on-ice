package com.plumbers.game;

/**
 * Represents a solid block occupying one tile of the game map's 2D grid.
 */
public final class Block implements TileObject
{
    private final int column;
    private final int row;
    private final Rectangle rectangle;
    
    /** the size of one tile (or block) in game units */
    public static final int SIZE = 32;

    public Block(int column, int row)
    {
        this.column = column;
        this.row = row;
        this.rectangle = new Rectangle(SIZE * column, SIZE * row, SIZE, SIZE);
    }

    public Block(int column, int row,
                 float hitboxOffsetX, float hitboxOffsetY,
                 float hitboxWidth, float hitboxHeight)
    {
        this(column, row);
        rectangle.setX( rectangle.getX() + hitboxOffsetX );
        rectangle.setY( rectangle.getY() + hitboxOffsetY );
        rectangle.setW(hitboxWidth);
        rectangle.setH(hitboxHeight);
    }
    
    @Override
    public Rectangle getRectangle()
    {
        return rectangle;
    }
    
    @Override
    public boolean isSolidTo(CharacterType ct)
    {
        return true;
    }
    
    @Override
    public void onCollision(IPlayer p, Rectangle.Collision c)
    {
    }

    public int getColumn()
    {
        return column;
    }

    public int getRow()
    {
        return row;
    }
}
