package com.plumbers.game;

/**
 * An event indicating that a coin has been collected.
 */
public final class CoinEvent implements Event
{
    private static CoinEvent instance;

    private CoinEvent() {}

    @Override
    public void applyTo(EventContext context)
    {
        context.apply(this);
    }

    public static CoinEvent instance()
    {
        if (instance == null)
        {
            instance = new CoinEvent();
        }
        return instance;
    }
}
