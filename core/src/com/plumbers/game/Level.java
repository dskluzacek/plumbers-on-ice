package com.plumbers.game;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Level { 
	private List<Block> blocks = new ArrayList<Block>(); 
	private List<Decoration> decorations = new ArrayList<Decoration>();
	private List<Coin> coins = new ArrayList<Coin>();
//	private List<EnemySpawner> spawners;
	private List<Hazard> hazards = new ArrayList<Hazard>();
	private TiledMap tiledMap;
	private int width, height;
	private OrthogonalTiledMapRenderer renderer;
	private static final String PLATFORM_LAYER_NAME = "Platform layer";
	
	private Music soundtrack;
	private Background background;

	public Level(TextureAtlas atlas) {
		TmxMapLoader.Parameters mapParams = new TmxMapLoader.Parameters();
		mapParams.flipY = false;

		tiledMap = new TmxMapLoader().load("castle.tmx", mapParams);
		
		for ( TiledMapTileSet tileset : tiledMap.getTileSets() ) {
			for (TiledMapTile tile : tileset) {
				tile.getTextureRegion().flip(false, true);
			}
		}
		
		TiledMapTileLayer blockLayer = (TiledMapTileLayer) tiledMap.getLayers().get(PLATFORM_LAYER_NAME);
		width = blockLayer.getWidth();
		height = blockLayer.getHeight();
		
		for (int row = 0; row < blockLayer.getWidth(); row++) {
			for (int col = 0; col < blockLayer.getHeight(); col++) {
				TiledMapTileLayer.Cell cell = blockLayer.getCell(row, col);
				
				if ( cell != null ) {
					MapProperties props = cell.getTile().getProperties();
					if ( props.containsKey("special") && props.get("special").equals("coin") ) {
						coins.add( new Coin(row, col, cell) );
					}
					else if ( props.containsKey("special") && props.get("special").equals("spike") ) {
						hazards.add( new Hazard(
						    new Rectangle(row * GameModel.TILE_SIZE + 4,
						                  col * GameModel.TILE_SIZE + 20,
					                            24, 12)
						                       ) );
					}
					else {
						blocks.add( new Block(row, col, cell, blockLayer) );
					}
				}
			}
		}
		renderer = new OrthogonalTiledMapRenderer(tiledMap, 2);
		
		String musicStr = tiledMap.getProperties().get("soundtrack", String.class);
		
		if (musicStr != null) {
			soundtrack = Gdx.audio.newMusic( Gdx.files.internal(musicStr) );
		}
				
		String bgStr = tiledMap.getProperties().get("background", String.class);
		
		if (bgStr != null) {
    		TextureRegion bg = new TextureRegion( new Texture(bgStr) );
    		bg.flip(false, true);
    		background = new Background(bg, 2, 0.125);
		}
	}

	public List<Block> getBlocks(){
		return blocks; 
	}
	
	public List<Coin> getCoins() {
		return coins;
	}
	
	public List<Hazard> getHazards() {
		return hazards;
	}
	
	public Background getBackground() {
		return background;
	}

	public List<Decoration> getDecorations(){
		return decorations; 
	}
	
	public Music getSoundtrack() {
		return soundtrack;
	}
	
	public OrthogonalTiledMapRenderer getRenderer() {
		return renderer;
	}
	
	public int getWidthInTiles() {
		return width;
	}
	
	public int getHeightInTiles() {
		return height;
	}
	
	public void resetCoins() {
		for (Coin c : coins) {
			c.setCollected(false);
		}
	}

//  public List<EnemySpawn> getEnemies(){
//    return enemies; 
//  }
//  
//  public Background getBackground(){
//    return bg; 
//  }
//  
//  public Soundtrack getSoundtrack(){
//    return music; 
//  }

}
