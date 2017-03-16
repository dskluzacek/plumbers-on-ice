package com.plumbers.game;

/**
 * An event indicating that a springboard has been bounced on.
 */
public final class SpringboardEvent implements Event
{
    private static SpringboardEvent instance;
    
    private SpringboardEvent() {}
    
    @Override
    public void applyTo(EventContext context)
    {
        context.apply(this);   
    }
    
    public static SpringboardEvent instance()
    {
        if (instance == null) 
        {
            instance = new SpringboardEvent();
        }
        return instance;
    }
}
