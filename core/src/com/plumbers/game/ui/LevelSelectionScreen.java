package com.plumbers.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class LevelSelectionScreen extends ScreenAdapter {
    
    private Texture background;
    private ShapeRenderer renderer;
    
    public static final int GRASSY_LEVEL = 1,
                             TROPICAL_LEVEL = 2,
                             AUTUMN_LEVEL = 3,
                             ICY_LEVEL = 4,
                             CASTLE_LEVEL = 5;
    public static final String LEVEL_1_FILE = "grassy.tmx",
                                LEVEL_2_FILE = "tropical.tmx,",
                                LEVEL_3_FILE = "fall.tmx",
                                LEVEL_4_FILE = "icy.tmx",
                                LEVEL_5_FILE = "castle.tmx";
                             
    public LevelSelectionScreen() {

        
    }
    
    @Override
    public void show() {
        Batch batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
