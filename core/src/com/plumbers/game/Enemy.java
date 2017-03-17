package com.plumbers.game;

public interface Enemy extends Drawable, Hazard
{
    void gameTick(int tick, Block[][] blocks);
    void setPosition(float x, float y);
    Enemy copy();
    void reset();
}
