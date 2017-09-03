package com.plumbers.game;

public interface InteractiveMapObject
{
    Rectangle getRectangle();
    void activate(IPlayer p, Rectangle.Collision coll, int tickNumber);
}
