package com.plumbers.game;

import com.badlogic.gdx.InputProcessor;

public interface Controller extends InputProcessor {
    boolean pollRunInput();
    boolean pollJumpInput();
    boolean pollKillKey();
}
