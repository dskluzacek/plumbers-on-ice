package com.plumbers.game;

import com.badlogic.gdx.InputProcessor;

public interface Controller extends InputProcessor {
    public abstract boolean pollRunInput();
    public abstract boolean pollJumpInput();
    public abstract boolean pollKillKey();
}
