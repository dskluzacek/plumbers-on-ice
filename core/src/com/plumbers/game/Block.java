package com.plumbers.game;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public final class Block {
	private final int column;
	private final int row;
	private final TiledMapTileLayer layer;
	private final Cell cell;
	private final Rectangle rectangle;
	public static final int SIZE = 32;
	
	public Block(int column, int row, Cell cell, TiledMapTileLayer layer) {
        this.column = column;
        this.row = row;
        this.cell = cell;
        this.layer = layer;
        this.rectangle = new Rectangle(SIZE * column, SIZE * row, SIZE, SIZE);
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
		return rectangle;
	}

	public int getColumn() {
		return column;
	}
	
	public int getRow() {
		return row;
	}
	
	public Cell getCell() {
		return cell;
	}
	
	public TiledMapTileLayer getLayer() {
		return layer;
	}
}
