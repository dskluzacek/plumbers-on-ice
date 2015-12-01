package com.plumbers.game;

import com.badlogic.gdx.InputAdapter;

public class TouchscreenController extends InputAdapter implements Controller
{
    private final int screenMidpoint;
    
    private int runInputPointerNum = -1;
    private int jumpInputPointerNum = -1;
    
    public TouchscreenController(int screenWidth) {
        screenMidpoint = (int) (screenWidth / 2.0);
    }
    
    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (x < screenMidpoint) {
            if (runInputPointerNum == -1) {
                runInputPointerNum = pointer;
            }
        } else {
            if (jumpInputPointerNum == -1) {
                jumpInputPointerNum = pointer;
            }
        }
        return true;
    }
    
    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (pointer == runInputPointerNum) {
            runInputPointerNum = -1;
        } else if (pointer == jumpInputPointerNum) {
            jumpInputPointerNum = -1;
        }
        return true;
    }
    
    @Override
    public boolean pollRunInput() {
        return runInputPointerNum != -1;
    }

    @Override
    public boolean pollJumpInput() {
        return jumpInputPointerNum != -1;
    }

    @Override
    public boolean pollKillKey() {
        return false;
    }
}
