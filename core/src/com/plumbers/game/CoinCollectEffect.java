package com.plumbers.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool.Poolable;

public final class CoinCollectEffect implements Effect, Poolable
{
    private int x;
    private int y;
    private float startTime = Float.NaN;
    
    private static Animation animation;
    private static final float FRAME_DURATION = 0.1f;
    private static final float COIN_FADE_DURATION = 0.2f;
    private static final float COIN_RISE_DISTANCE = 16;
    
    public CoinCollectEffect init(int x, int y, float startTime)
    {
        this.x = x;
        this.y = y;
        this.startTime = startTime;
        
        return this;
    }

    @Override
    public void draw(Batch batch, float elapsedTime)
    {
        float time = elapsedTime - startTime;
        batch.draw(animation.getKeyFrame(time), x, y, Block.SIZE, Block.SIZE);
        
        if (time < COIN_FADE_DURATION)
        {
            float progress = (elapsedTime - startTime) / COIN_FADE_DURATION;
            float alpha = MathUtils.lerp(1, 0, progress);
            float coinY = y - MathUtils.lerp(0, COIN_RISE_DISTANCE, progress);
            
            TextureRegion coinFrame = Coin.getCoinTile().getTextureRegion();
            
            batch.setColor(1.0f, 1.0f, 1.0f, alpha);
            batch.draw(coinFrame, x, coinY, Block.SIZE, Block.SIZE);
            batch.setColor(Color.WHITE);
        }
    }

    @Override
    public boolean isComplete(float elapsedTime)
    {
        return animation.isAnimationFinished(elapsedTime - startTime);
    }
    
    public static void setTextureAtlas(TextureAtlas textureAtlas)
    {
        TextureRegion[] frames = new TextureRegion[] {
                textureAtlas.findRegion("coin-collect-1"),
                textureAtlas.findRegion("coin-collect-2"),
                textureAtlas.findRegion("coin-collect-3"),
                textureAtlas.findRegion("coin-collect-4") };
        animation = new Animation(FRAME_DURATION, frames);
    }

    @Override
    public void reset()
    {
        x = 0;
        y = 0;
        startTime = Float.NaN;
    }
}
