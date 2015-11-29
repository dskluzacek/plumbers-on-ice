package com.plumbers.game;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.utils.Pools;

public final class Block {
	private final int row;
	private final int column;
	private TiledMapTileLayer layer;
	private Cell cell;
	private Rectangle rectangle;
	public static final int SIZE = 32;
	
	public Block(int row, int column, Cell cell, TiledMapTileLayer layer) {
        this.row = row;
        this.column = column;
        this.cell = cell;
        this.layer = layer;
        this.rectangle = new Rectangle(SIZE * row, SIZE * column, SIZE, SIZE);
	}
	
	public Block(int row, int column, Cell cell, TiledMapTileLayer layer,
	             float hitboxRelativeX, float hitboxRelativeY,
	             float hitboxWidth, float hitboxHeight)
	{
		this(row, column, cell, layer);
		rectangle.setX( rectangle.getX() + hitboxRelativeX );
		rectangle.setY( rectangle.getY() + hitboxRelativeY );
		rectangle.setW(hitboxWidth);
		rectangle.setH(hitboxHeight);
	}
	
	public Rectangle getRectangle() {
		return Pools.get(Rectangle.class).obtain().set(rectangle);
	}

	public int getRow() {
		return row;
	}
	public int getColumn() {
		return column;
	}
	
	public Cell getCell() {
		return cell;
	}
	
	public TiledMapTileLayer getLayer() {
		return layer;
	}
}
