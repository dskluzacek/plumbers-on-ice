package com.plumbers.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public final class Level { 
    private final OrthogonalTiledMapRenderer renderer;
    private final List<Block> blocks = new ArrayList<Block>();
    private final Block[][] blockArray;
	private final List<Coin> coins = new ArrayList<Coin>();
	private final List<EnemySpawner> spawners = new ArrayList<EnemySpawner>();
	private final List<FixedHazard> hazards = new ArrayList<FixedHazard>();
	private final TiledMap tiledMap;
	private Vector start;
	private Rectangle finish;
	private final int widthInTiles, heightInTiles;
	private boolean useCeiling;
	
	private Music soundtrack;
	private int soundtrackDelay = 0;
	private Background background;
	private Color backgroundColor;
    
	private static final String PLATFORM_LAYER_NAME = "Platform layer",
	                            OBJECT_LAYER_NAME = "Object layer";
	
	public Level(String filename, TextureAtlas atlas)
		throws FileFormatException
	{
		TmxMapLoader.Parameters mapParams = new TmxMapLoader.Parameters();
		mapParams.flipY = false;

		tiledMap = new TmxMapLoader().load(filename, mapParams);
		
		for ( TiledMapTileSet tileset : tiledMap.getTileSets() ) {
			for (TiledMapTile tile : tileset) {
				tile.getTextureRegion().flip(false, true);
				
				if ( nullToEmptyString(
				        tile.getProperties().get("opaque", String.class)
				        ).equals("true") )
		        {
				    tile.setBlendMode(TiledMapTile.BlendMode.NONE);
		        }
			}
		}
		
		TiledMapTileLayer blockLayer =
		    (TiledMapTileLayer) tiledMap.getLayers().get(PLATFORM_LAYER_NAME);
		widthInTiles = blockLayer.getWidth();
		heightInTiles = blockLayer.getHeight();
		blockArray = new Block[widthInTiles][heightInTiles];
		try
		{
    		for (int column = 0; column < blockLayer.getWidth(); column++) {
    			for (int row = 0; row < blockLayer.getHeight(); row++) {
    				Cell cell = blockLayer.getCell(column, row);
    				
    				if ( cell != null ) {
						getTileProperties(column, row, cell, blockLayer);
    				}
    			}
    		}
    		getMapProperties();
    		getMapObjects();
		} catch (NumberFormatException nfe) {
            throw new FileFormatException(
                    "Error trying to parse an integer in " + filename,
                    nfe);
		}
		renderer = new OrthogonalTiledMapRenderer(tiledMap, 2);
	}
	
	public Vector getStartPosition() {
		return start;
	}
	
	public Rectangle getFinish() {
		return finish;
	}

	public List<Block> getBlocks(){
		return blocks; 
	}
	
	public Block[][] getBlockArray() {
	    return blockArray;
	}
	
	public List<Coin> getCoins() {
		return coins;
	}
	
	public List<FixedHazard> getHazards() {
		return hazards;
	}
	
	public List<EnemySpawner> getSpawners() {
		return spawners;
	}
	
	public boolean hasBackground() {
		return background != null;
	}
	
	public Background getBackground() {
		return background;
	}
	
	public boolean hasBackgroundColor() {
		return backgroundColor != null;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public Music getSoundtrack() {
		return soundtrack;
	}
	
	public int getSoundtrackDelay() {
	    return soundtrackDelay;
	}
	
	public OrthogonalTiledMapRenderer getRenderer() {
		return renderer;
	}
	
	public int getWidthInTiles() {
		return widthInTiles;
	}
	
	public int getHeightInTiles() {
		return heightInTiles;
	}
	
	public boolean useCeiling() {
		return useCeiling;
	}
	
	public void resetCoins() {
		for (Coin c : coins) {
			c.setCollected(false);
		}
	}
	
	private void getMapObjects()
	{
	    MapObjects objects = 
	            tiledMap.getLayers().get(OBJECT_LAYER_NAME).getObjects();
	    
	    for (MapObject obj : objects)
	    {
	        if (obj instanceof RectangleMapObject)
	        {
	            RectangleMapObject rectObj = (RectangleMapObject) obj;
	            
	            if ( rectObj.getName().equalsIgnoreCase("Start") )
	            {
	                float x = rectObj.getRectangle().getX() * 2;
	                float y = rectObj.getRectangle().getY() * 2;
	                start = new Vector(x, y);
	            
	            }
	            else if ( rectObj.getName().equalsIgnoreCase("Goal") )
	            {
	                com.badlogic.gdx.math.Rectangle rect = rectObj.getRectangle();
	            	
	            	finish = new Rectangle(rect.getX() * 2, rect.getY() * 2,
	            	              rect.getWidth() * 2, rect.getHeight() * 2);
	            }
	            else if ( rectObj.getProperties().containsKey("type") )
	            {
	            	MapProperties props = rectObj.getProperties();
	            	Enemy.Type type =
	            			Enemy.Type.get( props.get("type", String.class) );
	            	int x = 2 * (int) rectObj.getRectangle().getX();
	            	int y = 2 * (int) rectObj.getRectangle().getY();
	            	
	            	if (type == null)
	            		return;
	            	
	            	if ( props.containsKey("spawndistance") )
	            	{
	            		int spawnDistance = 2 * Integer.parseInt(
	            		        props.get("spawndistance", String.class) );
	            		
	            		spawners.add( new EnemySpawner(x, y, type, spawnDistance) );
	            	}
	            	else
	            	{
	            		spawners.add( new EnemySpawner(x, y, type) );
	            	}
	            }
	        }
	    }
	    
	}
	
	private void getMapProperties() {
		MapProperties properties = tiledMap.getProperties();
		
		useCeiling = nullToEmptyString(
          properties.get("ceiling", String.class) ).equalsIgnoreCase("true");

        String musicStr = properties.get("soundtrack", String.class);
        
        if (musicStr != null) {
        	soundtrack = Gdx.audio.newMusic( Gdx.files.internal(musicStr) );
        }
        
        if ( properties.containsKey("soundtrack-delay") ) {
        	soundtrackDelay = Integer.parseInt(
        	        properties.get("soundtrack-delay", String.class).trim()
        	        );
        }
        		
        String bgStr =
                properties.get("background", String.class);
        
        if (bgStr != null) {
        	TextureRegion bg = new TextureRegion( new Texture(bgStr) );
        	bg.flip(false, true);
        	background = new Background(bg, 2, 0.125);
        }
        
        String colorStr =
                properties.get("background-color", String.class);
        
        if (colorStr != null) {
        	String[] strArr = colorStr.split(",");
        	int red = Integer.parseInt( strArr[0].trim() );
        	int green = Integer.parseInt( strArr[1].trim() );
        	int blue = Integer.parseInt( strArr[2].trim() );
        	
        	backgroundColor
        	        = new Color(red / 255f, green / 255f, blue / 255f, 1);
        }
	}
	
	private void getTileProperties(int column, int row,
	                               Cell cell, TiledMapTileLayer blockLayer)
		throws NumberFormatException
	{
		MapProperties props = cell.getTile().getProperties();
		
		if ( props.containsKey("special")
				&& props.get("special").equals("coin") )
		{
			coins.add( new Coin(column, row, cell) );
		}
		else if ( props.containsKey("special")
				&& props.get("special").equals("spike") )
		{
			hazards.add( new FixedHazard(
			    new Rectangle(column * Block.SIZE + 4,
			                  row * Block.SIZE + 20,
		                      24, 12)) );
		}
		else if ( props.containsKey("relativeX")
		         || props.containsKey("relativeY")
		         || props.containsKey("width") 
				 || props.containsKey("height") )
		{
			String relX = props.get("relativeX", String.class);
			String relY = props.get("relativeY", String.class);
			String wStr = props.get("width", String.class);
			String hStr = props.get("height", String.class);
			
			int relativeX =
			    (relX == null ? 0 : Integer.parseInt(relX.trim()) * 2);
			int relativeY =
			    (relY == null ? 0 : Integer.parseInt(relY.trim()) * 2); 
			int width = (wStr == null ?
			        Block.SIZE : Integer.parseInt(wStr.trim()) * 2); 
			int height = (hStr == null ?
			        Block.SIZE : Integer.parseInt(hStr.trim()) * 2);
			
			Block b = new Block(column, row, cell, blockLayer,
                    relativeX, relativeY, width, height);
			blocks.add(b);
			blockArray[column][row] = b;
		}
		else
		{
			Block b =  new Block(column, row, cell, blockLayer);
		    blocks.add(b);
		    blockArray[column][row] = b;
		}
	}
	
	private String nullToEmptyString(String str) {
		return (str == null ? "" : str);
	}
}
