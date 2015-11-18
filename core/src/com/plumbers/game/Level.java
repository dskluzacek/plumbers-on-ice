package com.plumbers.game;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Level { 
	private List<Block> blocks = new ArrayList<Block>(); 
	private List<Decoration> decorations = new ArrayList<Decoration>();
	
	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer renderer;

//  private List<EnemySpawn> enemies; 
//  private Background bg; 
//  private Soundtrack music; 

	public Level() {
		TmxMapLoader.Parameters mapParams = new TmxMapLoader.Parameters();
		mapParams.flipY = false;

		tiledMap = new TmxMapLoader().load("grassy.tmx", mapParams);
		
		for ( TiledMapTileSet tileset : tiledMap.getTileSets() ) {
			for (TiledMapTile tile : tileset) {
				tile.getTextureRegion().flip(false, true);
			}
		}
		
		TiledMapTileLayer blockLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Platform layer");
		
		for (int row = 0; row < blockLayer.getWidth(); row++) {
			for (int col = 0; col < blockLayer.getHeight(); col++) {
				TiledMapTileLayer.Cell cell = blockLayer.getCell(row, col);
				
				if ( cell != null ) {
					blocks.add( new Block(row, col, cell, blockLayer) );
				}
					
			}
		}
		
		renderer = new OrthogonalTiledMapRenderer(tiledMap, 2);
	}

	public List<Block> getBlocks(){
		return blocks; 
	}

	public List<Decoration> getDecoration(){
		return decorations; 
	}
	
	public OrthogonalTiledMapRenderer getRenderer() {
		return renderer;
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
