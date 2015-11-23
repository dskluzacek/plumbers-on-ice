package com.plumbers.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;

public final class Coin {
	private final int row;
	private final int column;
	private final Rectangle hitbox;
	
	private boolean collected;
	private TiledMapTileLayer.Cell cell;
	
	private static AnimatedTiledMapTile coinTile;
	private static final float COIN_FRAME_DURATION = 1/5f;
	
	public static void createCoinTile(TextureAtlas atlas) {
		Array<StaticTiledMapTile> frames = new Array<StaticTiledMapTile>();
		frames.add( new StaticTiledMapTile(atlas.findRegion("coin-1")) );
		frames.add( new StaticTiledMapTile(atlas.findRegion("coin-2")) );
		frames.add( new StaticTiledMapTile(atlas.findRegion("coin-3")) );
		frames.add( new StaticTiledMapTile(atlas.findRegion("coin-4")) );
		coinTile = new AnimatedTiledMapTile(COIN_FRAME_DURATION, frames);
	}
	
	public Coin(int row, int col, TiledMapTileLayer.Cell cell) {
		this.row = row;
		this.column = col;
		this.cell = cell;
		hitbox = new Rectangle(row * GameModel.TILE_SIZE, 
		                       col * GameModel.TILE_SIZE,
		                       GameModel.TILE_SIZE,
		                       GameModel.TILE_SIZE);
		cell.setTile(coinTile);
	}
	
	public void setCollected(boolean collected) {
		this.collected = collected;
		
		if (collected) {
			cell.setTile(null);
		} else {
			cell.setTile(coinTile);
		}
	}
	
	public boolean isCollected() {
		return collected;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public Rectangle getRectangle() {
		return hitbox;
	}
}
