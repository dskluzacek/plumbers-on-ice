package com.plumbers.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private final List<Block> blocks = new ArrayList<Block>();
    private final Block[][] blockArray;
    private final List<Coin> coins = new ArrayList<Coin>();
    private final List<EnemySpawner> spawners = new ArrayList<EnemySpawner>();
    private final List<FixedHazard> hazards = new ArrayList<FixedHazard>();
    private final List<Springboard> springboards = new ArrayList<Springboard>();
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
                                ENVIROMENT_KEY = "enviroment",
                                GRASSLAND_STR = "grassland",
                                WINTER_STR = "winter",
                                AUTUMN_STR = "autumn",
                                TROPICAL_STR = "tropical",
                                DUNGEON_STR = "dungeon",
                                BACKGROUND_COLOR_KEY = "background-color",
                                SPECIAL_TILE_KEY = "special",
                                COIN_STR = "coin",
                                SPIKES_STR = "spike",
                                SPRINGBOARD_STR = "springboard",
                                ENEMY_TYPE_KEY = "type",
                                SPAWN_DISTANCE_KEY = "spawndistance",
                                COLL_OFFSET_X_KEY = "relativeX",
                                COLL_OFFSET_Y_KEY = "relativeY",
                                COLL_WIDTH_KEY = "width",
                                COLL_HEIGHT_KEY = "height";
    
    public Level(String filename, TextureAtlas atlas)
            throws FileFormatException
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

    public List<Block> getBlocks()
    {
        return blocks;
    }

    public Block[][] getBlockArray()
    {
        return blockArray;
    }

    public List<Coin> getCoins()
    {
        return coins;
    }

    public List<FixedHazard> getHazards()
    {
        return hazards;
    }

    public List<EnemySpawner> getSpawners()
    {
        return spawners;
    }

    public List<Springboard> getSpringboards()
    {
        return springboards;
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
                    float x = rectObj.getRectangle().getX() * UNIT_SCALE;
                    float y = rectObj.getRectangle().getY() * UNIT_SCALE;
                    start = new Vector(x, y);
                }
                else if ( rectObj.getName().equalsIgnoreCase(FINISH_NAME) )
                {
                    com.badlogic.gdx.math.Rectangle rect = rectObj.getRectangle();

                    finish = new Rectangle(rect.getX() * UNIT_SCALE,
                                           rect.getY() * UNIT_SCALE,
                                           rect.getWidth() * UNIT_SCALE,
                                           rect.getHeight() * UNIT_SCALE);
                }
                else if ( rectObj.getProperties().containsKey(ENEMY_TYPE_KEY) )
                {
                    MapProperties props = rectObj.getProperties();
                    Enemy.Type type = Enemy.Type.getByName(
                            props.get(ENEMY_TYPE_KEY, String.class) );
                    
                    int x = UNIT_SCALE * (int) rectObj.getRectangle().getX();
                    int y = UNIT_SCALE * (int) rectObj.getRectangle().getY();

                    if (type == null)
                        return;

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
            }
        }

    }

    private void getMapProperties()
    {
        MapProperties properties = tiledMap.getProperties();

        useCeiling = nullToEmptyString(
                properties.get(CEILING_KEY, String.class)).equalsIgnoreCase("true");

        String musicStr = properties.get(SOUNDTRACK_KEY, String.class);

        if (musicStr != null)
        {
            soundtrack = Gdx.audio.newMusic( Gdx.files.internal(musicStr) );
        }

        if (properties.containsKey(SOUNDTRACK_DELAY_KEY))
        {
            soundtrackDelay = Integer.parseInt(
                    properties.get(SOUNDTRACK_DELAY_KEY, String.class).trim() );
        }
        
        String envStr = properties.get(ENVIROMENT_KEY, String.class);
        
        if (envStr != null)
        {
            if ( envStr.equalsIgnoreCase(AUTUMN_STR) )
            {
                TextureRegion land = new TextureRegion( new Texture("fall.png") );
                TextureRegion sky   = new TextureRegion( new Texture("fall-sky.png") );
                land.flip(false, true);
                sky.flip(false, true);
                background = new Background.AutumnBackground(land, sky);
            }
            else if ( envStr.equalsIgnoreCase(GRASSLAND_STR) )
            {
                TextureRegion land = new TextureRegion( new Texture("grassy.png") );
                TextureRegion sky   = new TextureRegion( new Texture("grassy-sky.png") );
                land.flip(false, true);
                sky.flip(false, true);
                background = new Background.GrasslandBackground(land, sky);
            }
            else if ( envStr.equalsIgnoreCase(WINTER_STR) )
            {
                TextureRegion land = new TextureRegion( new Texture("winter.png") );
                TextureRegion sky   = new TextureRegion( new Texture("winter-sky.png") );
                land.flip(false, true);
                sky.flip(false, true);
                background = new Background.AutumnBackground(land, sky);
            }
        }
        else
        {
            String bgStr = properties.get("background", String.class);
    
            if (bgStr != null)
            {
                TextureRegion bg = new TextureRegion( new Texture(bgStr) );
                bg.flip(false, true);
                background = new Background.ImageBackground(bg, UNIT_SCALE, 0.125);
            }
        }
        
        String colorStr = properties.get(BACKGROUND_COLOR_KEY, String.class);

        if (colorStr != null)
        {
            String[] strArr = colorStr.split(",");
            int red = Integer.parseInt( strArr[0].trim() );
            int green = Integer.parseInt( strArr[1].trim() );
            int blue = Integer.parseInt( strArr[2].trim() );

            backgroundColor = new Color(red / 255f, green / 255f, blue / 255f, 1);
        }
    }

    private void getTileProperties(int column, int row,
            Cell cell, TiledMapTileLayer blockLayer)
                    throws NumberFormatException
    {
        MapProperties props = cell.getTile().getProperties();
        
        if ( props.containsKey(SPECIAL_TILE_KEY) )
        {
            if ( props.get(SPECIAL_TILE_KEY).equals(COIN_STR) )
            {
                coins.add( new Coin(column, row, cell) );
            }
            else if ( props.get(SPECIAL_TILE_KEY).equals(SPIKES_STR) )
            {
                hazards.add( new FixedHazard(
                        new Rectangle(column * Block.SIZE + 4,
                                      row * Block.SIZE + 20,
                                      24, 12)) );
            }
            else if ( props.get(SPECIAL_TILE_KEY).equals(SPRINGBOARD_STR) )
            {
                springboards.add( new Springboard(column, row) );
                cell.setTile(null);
            }
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

            int relativeX =
                (relX == null ? 0 : Integer.parseInt(relX.trim()) * 2);
            int relativeY =
                (relY == null ? 0 : Integer.parseInt(relY.trim()) * 2); 
            int width =
                (wStr == null ? Block.SIZE : Integer.parseInt(wStr.trim()) * 2);
            int height =
                (hStr == null ? Block.SIZE : Integer.parseInt(hStr.trim()) * 2);

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
    
    private void loadWaterfallAnim(TiledMapTileSet tileset)
    {
        final String WATERFALL_1_KEY = "waterfall1";
        final String WATERFALL_2_KEY = "waterfall2";
        final int LENGTH = 16;
        
        StaticTiledMapTile[] framesA = new StaticTiledMapTile[LENGTH];
        StaticTiledMapTile[] framesB = new StaticTiledMapTile[LENGTH];
        
        for (TiledMapTile tile : tileset)
        {
            MapProperties properties = tile.getProperties();
            
            if ( properties.containsKey(WATERFALL_1_KEY) )
            {
                int index = Integer.parseInt(
                        properties.get(WATERFALL_1_KEY, String.class).trim()
                        );
                framesA[index] = (StaticTiledMapTile) tile;
            }
            else if ( properties.containsKey(WATERFALL_2_KEY) )
            {
                int index = Integer.parseInt(
                        properties.get(WATERFALL_2_KEY, String.class).trim()
                        );
                framesB[index] = (StaticTiledMapTile) tile;
            }
        }
        
        Array<StaticTiledMapTile> animFrames = new Array<StaticTiledMapTile>(2);
        
        for (int i = 0; i < framesA.length; i++)
        {
            System.out.println(framesA[i]);
            System.out.println(framesB[i]);
            
            animFrames.add(framesA[i]);
            animFrames.add(framesB[i]);    
            final AnimatedTiledMapTile animatedTile = new AnimatedTiledMapTile(1/6f, animFrames);
            final int id = framesA[i].getId();
            
            // have to do this because setting the tile in the tileset does nothing
            for (MapLayer mapLayer : tiledMap.getLayers())
            {
                if (mapLayer instanceof TiledMapTileLayer)
                {
                    TiledMapTileLayer layer = (TiledMapTileLayer) mapLayer;
                    
                    forEachCell(layer, new CellAction()
                    {
                        @Override
                        public void apply(Cell cell)
                        {
                            if (cell != null && cell.getTile().getId() == id)
                            {
                                cell.setTile(animatedTile);
                            }
                        }
                    });
                }
            }
            animFrames.clear();
        }
    }
    
    private static void forEachCell(TiledMapTileLayer layer, CellAction action)
    {
        for (int column = 0; column < layer.getWidth(); column++)
        {
            for (int row = 0; row < layer.getHeight(); row++)
            {
                Cell cell = layer.getCell(column, row);
                action.apply(cell);
            }
        }
    }
    
    private interface CellAction
    {
        void apply(Cell c);
    }

    private static String nullToEmptyString(String str)
    {
        return (str == null ? "" : str);
    }
}
