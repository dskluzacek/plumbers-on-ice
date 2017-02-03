package com.plumbers.game;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Interface for any object needing to be drawn that is
 * not drawn by the map renderer or Background.
 */
public interface Drawable 
{
    void draw(Batch batch, float elapsedTime);
}
