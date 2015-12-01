package com.plumbers.game;

public class TouchscreenController extends Controller
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
                runInputDown();
            }
        } else {
            if (jumpInputPointerNum == -1) {
                jumpInputPointerNum = pointer;
                jumpInputDown();
            }
        }
        return true;
    }
    
    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (pointer == runInputPointerNum) {
            runInputPointerNum = -1;
            runInputUp();
        } else if (pointer == jumpInputPointerNum) {
            jumpInputPointerNum = -1;
            jumpInputUp();
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

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
