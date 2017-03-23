package com.plumbers.game;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.ReflectionPool;

/**
 * An event indicating that a coin has been collected.
 */
public final class CoinEvent implements Event, Poolable
{
    private Coin coin;
    
    private static Pool<CoinEvent> pool = new ReflectionPool<CoinEvent>(CoinEvent.class, 4);
    
    private CoinEvent() {}
    
    public CoinEvent init(Coin coin)
    {
        this.coin = coin;
        return this;
    }
    
    public Coin getCoin()
    {
        return coin;
    }
    
    @Override
    public void reset()
    {
        coin = null;
    }
    
    @Override
    public void applyTo(EventContext context)
    {
        context.apply(this);
    }

    public static Pool<CoinEvent> getPool()
    {
        return pool;
    }
}
