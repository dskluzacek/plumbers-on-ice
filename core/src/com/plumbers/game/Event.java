package com.plumbers.game;

/**
 * Represents an event detected within the game model,
 * to be applied to an EventContext.
 */
public interface Event
{
    void applyTo(EventContext context);
}
