package com.plumbers.game;

public class SpringboardEvent implements Event
{
    private static SpringboardEvent instance;
    
    @Override
    public void applyTo(EventContext context)
    {
        context.apply(this);   
    }
    
    public static SpringboardEvent instance() {
        if (instance == null) {
            instance = new SpringboardEvent();
        }
        return instance;
    }
}
