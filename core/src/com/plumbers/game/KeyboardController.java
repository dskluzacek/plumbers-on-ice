package com.plumbers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyboardController extends Controller {
    private boolean runInput = false;
    private boolean jumpInput = false;
    
    @Override
    public boolean pollRunInput() {
        boolean now = ( Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                || Gdx.input.isKeyPressed(Input.Keys.SPACE)
                || Gdx.input.isKeyPressed(Input.Keys.D) );
        
        if (now == true && runInput == false) {
            runInputDown();
        }
        else if (now == false && runInput == true) {
            runInputUp();
        }
        runInput = now;
        return now;
    }

    @Override
    public boolean pollJumpInput() {
        boolean now = ( Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)
                || Gdx.input.isKeyPressed(Input.Keys.UP)
                || Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
                || Gdx.input.isKeyPressed(Input.Keys.W) );
        
        if (now == true && jumpInput == false) {
            jumpInputDown();
        }
        else if (now == false && jumpInput == true) {
            jumpInputUp();
        }
        jumpInput = now;
        return now;
    }

    @Override
    public boolean pollKillKey() {
        return Gdx.input.isKeyPressed(Input.Keys.K);
    }

    /*---------------------------------------------------------------------*/
    
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {    return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
