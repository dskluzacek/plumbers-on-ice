package com.plumbers.game;

/**
 * Represents an object located in the level tile grid (such as a Block)
 * that has a collision box and can affect the game characters.
 * The collision rectangle must be within the grid cell in which the object
 * is located, but does not have to completely fill it.
 */
public interface TileObject
{
    Rectangle getRectangle();
    boolean isSolidTo(CharacterType ct);
    void onCollision(IPlayer p, Rectangle.Collision coll);
}
