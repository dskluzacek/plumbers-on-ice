package com.plumbers.game;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class Block {
	private final int row;
	private final int column;
	private TiledMapTileLayer layer;
	private Cell cell;
	private static final int SIZE = 32;
	
	public Block(int row, int column, Cell cell, TiledMapTileLayer layer) {
        this.row = row;
        this.column = column;
        this.cell = cell;
        this.layer = layer;
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(SIZE * row, SIZE * column, SIZE, SIZE);
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
