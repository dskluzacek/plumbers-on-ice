package com.plumbers.game;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public abstract class BonusBlock implements Drawable, TileObject
{
    private final int column;
    private final int row;
    private final Cell cell;
    private final Rectangle hitbox;

    public BonusBlock(int column, int row, Cell cell)
    {
        this.column = column;
        this.row = row;
        this.cell = cell;
        this.hitbox = new Rectangle(Block.SIZE * column,
                                    Block.SIZE * row,
                                    Block.SIZE, Block.SIZE);
    }

    protected abstract void activate(IPlayer p);
    
    public void reset()
    {
    }
    
    @Override
    public final void onCollision(IPlayer p, Rectangle.Collision coll)
    {
    }
    
    @Override
    public final Rectangle getRectangle()
    {
        return hitbox;
    }
    
    public final int getColumn()
    {
        return column;
    }

    public final int getRow()
    {
        return row;
    }
    
    public final Cell getCell()
    {
        return cell;
    }
}
