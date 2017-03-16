package com.plumbers.game;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
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
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;

/**
 * A game level, loaded from a Tiled map file.
 */
public final class Level
{ 
    private final OrthogonalTiledMapRenderer renderer;
//    private final List<Block> blocks = new ArrayList<Block>();
    private final Block[][] blockArray;
    private final Array<Coin> coins = new Array<Coin>(64);
    private final Array<EnemySpawner> spawners = new Array<EnemySpawner>();
    private final Array<FixedHazard> hazards = new Array<FixedHazard>();
    private final Array<Springboard> springboards = new Array<Springboard>();
    private final TiledMap tiledMap;
    private Vector start;
    private Rectangle finish;
    private final int widthInTiles, heightInTiles;
    private boolean useCeiling;

    private Music soundtrack;
    private int soundtrackDelay = 0;
    private Background background;
    private Color backgroundColor;

    public static final int UNIT_SCALE = 2;
    
    private static final String PLATFORM_LAYER_NAME = "Platform layer",
                                OBJECT_LAYER_NAME   = "Object layer";
    @SuppressWarnings("unused")
    private static final String START_NAME =  "Start",
                                FINISH_NAME = "Goal",
                                CEILING_KEY = "ceiling",
                                SOUNDTRACK_KEY = "soundtrack",
                                SOUNDTRACK_DELAY_KEY = "soundtrack-delay",
                                ENVIRONMENT_KEY = "enviroment",
                                GRASSLAND_STR = "grassland",
                                WINTER_STR = "winter",
                                AUTUMN_STR = "autumn",
                                TROPICAL_STR = "tropical",
                                DUNGEON_STR = "dungeon",
                                BACKGROUND_COLOR_KEY = "background-color",
                                ENEMY_TYPE_KEY = "type",
                                SPAWN_DISTANCE_KEY = "spawndistance",
                                COLL_OFFSET_X_KEY = "relativeX",
                                COLL_OFFSET_Y_KEY = "relativeY",
                                COLL_WIDTH_KEY = "width",
                                COLL_HEIGHT_KEY = "height";
    
    private enum MapProperty
    {
        ENVIRONMENT ("enviroment")
        {
            @Override void apply(String value, Level level) throws FileFormatException
            {
                if ( value.isEmpty() ) {
                    return;
                }
                Environment env = Environment.getByName(value);
                
                if (env == null) {
                    throw new FileFormatException("Invalid value for 'environment' property");
                }
                level.background = env.createBackground();
                
                if (level.background == null && level.backgroundColor == null) {
                    level.backgroundColor = env.getDefaultBackgroundColor();
                }
            }
        },
        USE_CEILING ("ceiling")
        {
            @Override void apply(String value, Level level)
            {
                level.useCeiling = value.equalsIgnoreCase("true");
            }
        },
        SOUNDTRACK ("soundtrack")
        {
            @Override void apply(String value, Level level)
            {
                if ( ! value.isEmpty() ) {
                    level.soundtrack = Gdx.audio.newMusic( Gdx.files.internal(value) );
                }
            }
        },
        SOUNDTRACK_DELAY ("soundtrack-delay")
        {
            @Override void apply(String value, Level level)
            {
                level.soundtrackDelay = Integer.parseInt(value);
            }
        },
        BACKGROUND_COLOR ("background-color")
        {
            @Override void apply(String value, Level level)
            {
                String[] strArr = value.split(",");
                int red = Integer.parseInt( strArr[0].trim() );
                int green = Integer.parseInt( strArr[1].trim() );
                int blue = Integer.parseInt( strArr[2].trim() );

                level.backgroundColor = new Color(red / 255f, green / 255f, blue / 255f, 1);
            }
        };
        
        private final String key;
        
        private MapProperty(String key)
        {
            this.key = key;
        }
        
        abstract void apply(String value, Level level) throws FileFormatException;
        
        String getKey()
        {
            return key;
        }
    }
    
    private enum TileProperty
    {
        DECORATIVE ("decorative")
        {
            @Override
            boolean apply(String value, int col, int row, Cell cell, Level level)
            {
                return ( value.equalsIgnoreCase("true") );
            }
        },
        SPECIAL ("special")
        {
            @Override
            boolean apply(String value, int col, int row, Cell cell, Level level)
            {
                for (SpecialTile special : SpecialTile.values())
                {
                    if ( special.getValue().equalsIgnoreCase(value) )
                    {
                        special.apply(col, row, cell, level);
                        return true;
                    }
                }
                return false;
            }
        };
        
        private final String key;
        
        private TileProperty(String key)
        {
            this.key = key;
        }
        
        /**
         * Returns true if the tile with this property should be ignored for collision purposes.
         */
        abstract boolean apply(String value, int column, int row, Cell cell, Level level);
        
        String getKey()
        {
            return key;
        }
    }
    
    private enum SpecialTile
    {
        COIN ("coin")
        {
            @Override void apply(int column, int row, Cell cell, Level level)
            {
                level.coins.add( new Coin(column, row, cell) );
            }
        },
        SPIKES ("spike")
        {
            @Override void apply(int column, int row, Cell cell, Level level)
            {
                level.hazards.add( new Spikes(column, row) );
            }
        },
        SPRINGBOARD ("springboard")
        {
            @Override void apply(int column, int row, Cell cell, Level level)
            {
                level.springboards.add( new Springboard(column, row) );
                
                // Springboard is a Drawable and not rendered by the map renderer
                cell.setTile(null);
            }
        };
        
        private final String value;
        
        private SpecialTile(String value)
        {
            this.value = value;
        }
        
        abstract void apply(int column, int row, Cell cell, Level level);
        
        String getValue()
        {
            return value;
        }
    }
    
    public Level(String filename) throws FileFormatException
    {
        // load the map
        TmxMapLoader.Parameters mapParams = new TmxMapLoader.Parameters();
        mapParams.flipY = false;
        tiledMap = new TmxMapLoader().load(filename, mapParams);
        
        // set every tile in every tileset to be flipped vertically
        for ( TiledMapTileSet tileset : tiledMap.getTileSets() )
        {
            for (TiledMapTile tile : tileset)
            {
                tile.getTextureRegion().flip(false, true);
            }
            
            // special case enabling waterfall animated tiles
            if ( tileset.getName().equalsIgnoreCase("castle-tiles") )
            {
                loadWaterfallAnim(tileset);
            }
        }
        
        // tiles the player can interact with, like blocks and coins,
        // are to be found in the platform layer
        TiledMapTileLayer blockLayer =
                (TiledMapTileLayer) tiledMap.getLayers().get(PLATFORM_LAYER_NAME);
        widthInTiles = blockLayer.getWidth();
        heightInTiles = blockLayer.getHeight();
        blockArray = new Block[widthInTiles][heightInTiles];
        
        try
        {
            // iterate over every cell in the layer and get tile properties
            for (int column = 0; column < blockLayer.getWidth(); column++)
            {
                for (int row = 0; row < blockLayer.getHeight(); row++)
                {
                    Cell cell = blockLayer.getCell(column, row);

                    if (cell != null) {
                        getTileProperties(column, row, cell, blockLayer);
                    }
                }
            }
            
            // load map properties and objects
            getMapProperties();
            getMapObjects();
        }
        catch (NumberFormatException nfe)
        {
            throw new FileFormatException(
                    "Error trying to parse an integer in " + filename, nfe);
        }
        
        // finally, construct the renderer
        renderer = new OrthogonalTiledMapRenderer(tiledMap, UNIT_SCALE);
    }

    public Vector getStartPosition()
    {
        return start;
    }

    public Rectangle getFinish()
    {
        return finish;
    }

//    public List<Block> getBlocks()
//    {
//        return blocks;
//    }

    public Block[][] getBlockArray()
    {
        return blockArray;
    }
    
    /**
     * Returns an iterator for the level's <code>Coin</code>s.
     * The same iterator instance is returned for each call to this method.
     */
    public Iterator<Coin> getCoins()
    {
        return coins.iterator();
    }
    
    /**
     * Returns an iterator for the level's <code>FixedHazard</code>s.
     * The same iterator instance is returned for each call to this method.
     */
    public Iterator<FixedHazard> getHazards()
    {
        return hazards.iterator();
    }
    
    /**
     * Returns an iterator for the level's <code>EnemySpawner</code>s.
     * The same iterator instance is returned for each call to this method.
     */
    public Iterator<EnemySpawner> getSpawners()
    {
        return spawners.iterator();
    }
    
    /**
     * Returns an iterator for the level's <code>Springboard</code>s.
     * The same iterator instance is returned for each call to this method.
     */
    public Iterator<Springboard> getSpringboards()
    {
        return springboards.iterator();
    }

    public boolean hasBackground()
    {
        return background != null;
    }

    public Background getBackground()
    {
        return background;
    }

    public boolean hasBackgroundColor()
    {
        return backgroundColor != null;
    }

    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    public Music getSoundtrack()
    {
        return soundtrack;
    }

    public int getSoundtrackDelay()
    {
        return soundtrackDelay;
    }

    public OrthogonalTiledMapRenderer getRenderer()
    {
        return renderer;
    }

    public int getWidthInTiles()
    {
        return widthInTiles;
    }

    public int getHeightInTiles()
    {
        return heightInTiles;
    }

    public boolean useCeiling()
    {
        return useCeiling;
    }

    public void resetCoins()
    {
        for (Coin c : coins)
        {
            c.setCollected(false);
        }
    }
    
    public void resetSpringboards()
    {
        for (Springboard sb : springboards)
        {
            sb.reset();
        }
    }
    
    public void resetEnemySpawners()
    {
        for (EnemySpawner s : spawners)
        {
            s.reset();
        }
    }
    
    private void getMapProperties() throws FileFormatException
    {
        MapProperties properties = tiledMap.getProperties();
        
        for (MapProperty property : MapProperty.values())
        {
            String key =  property.getKey();
            
            if ( properties.containsKey(key) )
            {
                property.apply(properties.get(key, String.class).trim(), this);
            }
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

                if ( rectObj.getName().equalsIgnoreCase(START_NAME) )
                {
                    loadStart(rectObj);
                }
                else if ( rectObj.getName().equalsIgnoreCase(FINISH_NAME) )
                {
                    loadFinish(rectObj);
                }
                else if ( rectObj.getProperties().containsKey(ENEMY_TYPE_KEY) )
                {
                    loadEnemySpawner(rectObj);
                }
            }
        }
    }
    
    private void loadStart(RectangleMapObject rectObj)
    {
        float x = rectObj.getRectangle().getX() * UNIT_SCALE;
        float y = rectObj.getRectangle().getY() * UNIT_SCALE;
        start = new Vector(x, y);
    }
    
    private void loadFinish(RectangleMapObject rectObj)
    {
        com.badlogic.gdx.math.Rectangle rect = rectObj.getRectangle();

        finish = new Rectangle(rect.getX() * UNIT_SCALE,
                               rect.getY() * UNIT_SCALE,
                               rect.getWidth() * UNIT_SCALE,
                               rect.getHeight() * UNIT_SCALE);
    }
    
    private void loadEnemySpawner(RectangleMapObject rectObj)
    {
        MapProperties props = rectObj.getProperties();
        Enemy.Type type = Enemy.Type.getByName( props.get(ENEMY_TYPE_KEY, String.class) );
        
        if (type == null) {
            return;
        }
        
        int x = UNIT_SCALE * (int) rectObj.getRectangle().getX();
        int y = UNIT_SCALE * (int) rectObj.getRectangle().getY();

        if ( props.containsKey(SPAWN_DISTANCE_KEY) )
        {
            int spawnDistance = UNIT_SCALE * Integer.parseInt(
                    props.get(SPAWN_DISTANCE_KEY, String.class) );

            spawners.add( new EnemySpawner(x, y, type, spawnDistance) );
        }
        else
        {
            spawners.add( new EnemySpawner(x, y, type) );
        }
    }

    private void getTileProperties(int column, int row,
            Cell cell, TiledMapTileLayer blockLayer)
                    throws NumberFormatException
    {
        MapProperties props = cell.getTile().getProperties();
        boolean propertyApplied = false;
        
        for (TileProperty property : TileProperty.values())
        {
            String key = property.getKey();
            
            if ( props.containsKey(key)
                 && property.apply( props.get(key, String.class).trim(),
                                              column, row, cell, this) )
            {
                propertyApplied = true;
            }
        }
        
        if (propertyApplied)
        {
            return;
        }
        else if ( props.containsKey(COLL_OFFSET_X_KEY)
                  || props.containsKey(COLL_OFFSET_Y_KEY)
                  || props.containsKey(COLL_WIDTH_KEY)
                  || props.containsKey(COLL_HEIGHT_KEY) )
        {
            String relX = props.get(COLL_OFFSET_X_KEY, String.class);
            String relY = props.get(COLL_OFFSET_Y_KEY, String.class);
            String wStr = props.get(COLL_WIDTH_KEY, String.class);
            String hStr = props.get(COLL_HEIGHT_KEY, String.class);

            int offsetX =
                (relX == null ? 0 : Integer.parseInt(relX.trim()) * UNIT_SCALE);
            int offsetY =
                (relY == null ? 0 : Integer.parseInt(relY.trim()) * UNIT_SCALE); 
            int width =
                (wStr == null ? Block.SIZE : Integer.parseInt(wStr.trim()) * UNIT_SCALE);
            int height =
                (hStr == null ? Block.SIZE : Integer.parseInt(hStr.trim()) * UNIT_SCALE);

            Block b = new Block(column, row, cell, blockLayer,
                                offsetX, offsetY, width, height);
//            blocks.add(b);
            blockArray[column][row] = b;
        }
        else
        {
            Block b =  new Block(column, row, cell, blockLayer);
//            blocks.add(b);
            blockArray[column][row] = b;
        }
    }
    
    // loads the waterfall animation from the dungeon environment
    private void loadWaterfallAnim(TiledMapTileSet tileset)
    {
        final String WATERFALL_A_KEY = "waterfall1";
        final String WATERFALL_B_KEY = "waterfall2";
        final int LENGTH = 16;
        
        StaticTiledMapTile[] framesA = new StaticTiledMapTile[LENGTH];
        StaticTiledMapTile[] framesB = new StaticTiledMapTile[LENGTH];
        
        // search for waterfall frame tiles and load them into array by index
        for (TiledMapTile tile : tileset)
        {
            MapProperties properties = tile.getProperties();
            
            if ( properties.containsKey(WATERFALL_A_KEY) )
            {
                int index = Integer.parseInt(
                        properties.get(WATERFALL_A_KEY, String.class).trim()
                        );
                framesA[index] = (StaticTiledMapTile) tile;
            }
            else if ( properties.containsKey(WATERFALL_B_KEY) )
            {
                int index = Integer.parseInt(
                        properties.get(WATERFALL_B_KEY, String.class).trim()
                        );
                framesB[index] = (StaticTiledMapTile) tile;
            }
        }
        
        // create a 2-frame animated tile for each waterfall tile
        Array<StaticTiledMapTile> animFrames = new Array<StaticTiledMapTile>(2);
        
        for (int i = 0; i < framesA.length; i++)
        {   
            animFrames.add(framesA[i]);
            animFrames.add(framesB[i]);    
            final AnimatedTiledMapTile animatedTile = new AnimatedTiledMapTile(1/6f, animFrames);
            final int id = framesA[i].getId();
            
            animatedTile.getProperties().put("decorative", "true");
            
            // Search for the tile in the map layers and replace.
            // Have to do this because setting the tile in the tileset does nothing.
            for (MapLayer mapLayer : tiledMap.getLayers())
            {
                if (mapLayer instanceof TiledMapTileLayer)
                {
                    TiledMapTileLayer layer = (TiledMapTileLayer) mapLayer;
                    
                    for ( Cell cell : cellIterable(layer) )
                    {
                        if (cell != null && cell.getTile().getId() == id)
                        {
                            cell.setTile(animatedTile);
                        }
                    }
                }
            }
            animFrames.clear();
        }
    }
    
    private static Iterable<Cell> cellIterable(final TiledMapTileLayer layer)
    {
        return new Iterable<Cell>()
        {
            @Override
            public Iterator<Cell> iterator()
            {
                return new CellIterator(layer);
            }
        };
    }
    
    private static class CellIterator implements Iterator<Cell>
    {
        private TiledMapTileLayer layer;
        private int column = 0;
        private int row = 0;
        private boolean hasNext = true;
        
        CellIterator(TiledMapTileLayer layer)
        {
            this.layer = layer; 
        }
        
        @Override
        public boolean hasNext()
        {
            return hasNext;
        }
        
        @Override
        public Cell next()
        {
            if (! hasNext) {
                throw new NoSuchElementException();
            }
            
            Cell result = layer.getCell(column, row);
            
            if ( row < layer.getHeight() ) {
                row++;
            }
            else {
                column++;
                row = 0;
                
                if ( column >= layer.getWidth() ) {
                    hasNext = false;
                }
            }
            return result;
        }
        
        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }

//    private static String nullToEmptyString(String str)
//    {
//        return (str == null ? "" : str);
//    }
}
