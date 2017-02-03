package com.plumbers.game;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

/**
 * Represents a solid block occupying one tile of the game map's 2D grid.
 */
public final class Block
{
    private final int column;
    private final int row;
    private final TiledMapTileLayer layer;
    private final Cell cell;
    private final Rectangle rectangle;
    
    /** the size of one tile (or block) in game units */
    public static final int SIZE = 32;

    public Block(int column, int row, Cell cell, TiledMapTileLayer layer)
    {
        this.column = column;
        this.row = row;
        this.cell = cell;
        this.layer = layer;
        this.rectangle = new Rectangle(SIZE * column, SIZE * row, SIZE, SIZE);
    }

    public Block(int column, int row, Cell cell, TiledMapTileLayer layer,
                 float hitboxOffsetX, float hitboxOffsetY,
                 float hitboxWidth, float hitboxHeight)
    {
        this(column, row, cell, layer);
        rectangle.setX( rectangle.getX() + hitboxOffsetX );
        rectangle.setY( rectangle.getY() + hitboxOffsetY );
        rectangle.setW(hitboxWidth);
        rectangle.setH(hitboxHeight);
    }

    public Rectangle getRectangle()
    {
        return rectangle;
    }

    public int getColumn()
    {
        return column;
    }

    public int getRow()
    {
        return row;
    }

    public Cell getCell()
    {
        return cell;
    }

    public TiledMapTileLayer getLayer()
    {
        return layer;
    }
}
