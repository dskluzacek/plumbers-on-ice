package com.plumbers.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;

/**
 * A coin within a game level, able to be collected by the player.
 */
public final class Coin
{
    private final int row;
    private final int column;
    private final Rectangle hitbox;

    private boolean collected;
    private final TiledMapTileLayer.Cell cell;

    private static AnimatedTiledMapTile coinTile;
    private static final float COIN_FRAME_DURATION = 1/5f;
    
    /**
     * Constructs a coin located at the given row and column.
     * @param column The column number
     * @param row The row number
     * @param cell The Tiled map cell located at the given row and column
     */
    public Coin(int column, int row, TiledMapTileLayer.Cell cell)
    {
        this.row = row;
        this.column = column;
        this.cell = cell;
        hitbox = new Rectangle(column * Block.SIZE,
                               row * Block.SIZE,
                               Block.SIZE,
                               Block.SIZE);
        cell.setTile(coinTile);
    }
    
    /**
     * Sets the coin as collected or not and updates the corresponding map cell.
     */
    public void setCollected(boolean collected)
    {
        this.collected = collected;

        if (collected) {
            cell.setTile(null);
        } else {
            cell.setTile(coinTile);
        }
    }

    public boolean isCollected()
    {
        return collected;
    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }

    public Rectangle getRectangle()
    {
        return hitbox;
    }
    
    public static AnimatedTiledMapTile getCoinTile()
    {
        return coinTile;
    }
    
    /**
     * Initializes the coin animated map tile.
     */
    public static void createCoinTile(TextureAtlas atlas)
    {
        Array<StaticTiledMapTile> frames = new Array<StaticTiledMapTile>();
        frames.add( new StaticTiledMapTile(atlas.findRegion("coin-1")) );
        frames.add( new StaticTiledMapTile(atlas.findRegion("coin-2")) );
        frames.add( new StaticTiledMapTile(atlas.findRegion("coin-3")) );
        frames.add( new StaticTiledMapTile(atlas.findRegion("coin-4")) );
        coinTile = new AnimatedTiledMapTile(COIN_FRAME_DURATION, frames);
    }
    
    /**
     * Creates the coin animation for the on-screen coin count display.
     */
    public static Animation createAnimation(TextureAtlas textureAtlas)
    {
        TextureRegion[] coinFrames = new TextureRegion[] {
                textureAtlas.findRegion("coin-1"),
                textureAtlas.findRegion("coin-2"),
                textureAtlas.findRegion("coin-3"),
                textureAtlas.findRegion("coin-4") };
        return new Animation(COIN_FRAME_DURATION, coinFrames);
    }
}
