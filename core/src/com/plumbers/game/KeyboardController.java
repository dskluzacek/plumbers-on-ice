package com.plumbers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class KeyboardController extends InputAdapter implements Controller {

    @Override
    public boolean pollRunInput() {
        return ( Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                || Gdx.input.isKeyPressed(Input.Keys.SPACE)
                || Gdx.input.isKeyPressed(Input.Keys.D) );
    }

    @Override
    public boolean pollJumpInput() {
        return ( Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)
                || Gdx.input.isKeyPressed(Input.Keys.UP)
                || Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
                || Gdx.input.isKeyPressed(Input.Keys.W) );
    }

    @Override
    public boolean pollKillKey() {
        return Gdx.input.isKeyPressed(Input.Keys.K);
    }

}
