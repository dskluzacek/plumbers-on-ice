package com.plumbers.game;

import com.badlogic.gdx.InputProcessor;

/**
 * Interface for various types of game input devices (touchscreen, keyboard, etc.)
 */
public interface Controller extends InputProcessor
{
    public abstract boolean pollRunInput();
    public abstract boolean pollJumpInput();
    public abstract boolean pollKillKey();
}
