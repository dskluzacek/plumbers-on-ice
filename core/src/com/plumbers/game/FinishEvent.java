package com.plumbers.game;

/**
 * An event indicating that the player has completed the level.
 */
public final class FinishEvent implements Event
{
    private static FinishEvent instance1, instance2;
    private final int player;

    private FinishEvent(int player)
    {
        this.player = player;
    }

    @Override
    public void applyTo(EventContext context)
    {
        context.apply(this);
    }

    public static FinishEvent playerOneInstance()
    {
        if (instance1 == null)
        {
            instance1 = new FinishEvent(1);
        }
        return instance1;
    }

    public static FinishEvent playerTwoInstance()
    {
        if (instance2 == null)
        {
            instance2 = new FinishEvent(2);
        }
        return instance2;
    }

    public int getPlayerNum()
    {
        return player;
    }
}