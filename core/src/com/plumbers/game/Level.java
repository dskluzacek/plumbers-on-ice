package com.plumbers.game;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer renderer;
	private TextureRegion background;
	private static final String PLATFORM_LAYER_NAME = "Platform layer",
	                            COIN_LAYER_NAME = "Coin layer";
	
//  private List<EnemySpawn> enemies; 
//  private Background bg; 
//  private Soundtrack music; 

	public Level(TextureAtlas atlas) {
		TmxMapLoader.Parameters mapParams = new TmxMapLoader.Parameters();
		mapParams.flipY = false;

		tiledMap = new TmxMapLoader().load("winter.tmx", mapParams);
		
		for ( TiledMapTileSet tileset : tiledMap.getTileSets() ) {
			for (TiledMapTile tile : tileset) {
				tile.getTextureRegion().flip(false, true);
			}
		}
		
		TiledMapTileLayer blockLayer = (TiledMapTileLayer) tiledMap.getLayers().get(PLATFORM_LAYER_NAME);
		
		for (int row = 0; row < blockLayer.getTileWidth(); row++) {
			for (int col = 0; col < blockLayer.getTileHeight(); col++) {
				TiledMapTileLayer.Cell cell = blockLayer.getCell(row, col);
				
				if ( cell != null ) {
					blocks.add( new Block(row, col, cell, blockLayer) );
				}
			}
		}
		
		TiledMapTileLayer coinLayer = (TiledMapTileLayer) tiledMap.getLayers().get(COIN_LAYER_NAME);
		
		for (int row = 0; row < coinLayer.getWidth(); row++) {
			for (int col = 0; col < coinLayer.getHeight(); col++) {
				TiledMapTileLayer.Cell cell = coinLayer.getCell(row, col);
				
				if ( cell != null ) {
					coins.add( new Coin(row, col, cell) );
				}
			}
		}
		
		renderer = new OrthogonalTiledMapRenderer(tiledMap, 2);
		
		background = new TextureRegion( new Texture("grassy.png") );
		background.flip(false, true);
	}

	public List<Block> getBlocks(){
		return blocks; 
	}
	
	public List<Coin> getCoins() {
		return coins;
	}
	
	public TextureRegion getBackground() {
		return background;
	}

	public List<Decoration> getDecorations(){
		return decorations; 
	}
	
	public OrthogonalTiledMapRenderer getRenderer() {
		return renderer;
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
