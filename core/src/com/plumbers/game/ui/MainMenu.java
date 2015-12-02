package com.plumbers.game.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenu extends InputAdapter implements Screen {
    private Viewport viewport;
    private OrthographicCamera camera;
    private Texture background;
    private TextureAtlas buttonsAtlas;
    private Sprite wordmark;
    private Batch batch;
    private BitmapFont font;
    private ButtonGroup buttons;
    private Game theGame;
    private static Screen leveMenu;
    private static Screen multiPlayerLobby;
    private static Screen characterMenu;
    private static Screen accountMenu;
    
    public MainMenu(Game game) {
        theGame = game;
        
        int btnX = 560;
        int btnWidth = 800;
        int btnY = 120;
        int btnHeight = 80;
        int btnDiff = 55 + btnHeight;
        
        Button singlePlayerButton 
         = new Button("Play", btnX, btnY + 4*btnDiff, btnWidth, btnHeight, leveMenu);
        Button multiplayerButton
         = new Button("Play Multiplayer", btnX,  btnY + 3*btnDiff, btnWidth, btnHeight, multiPlayerLobby);
        Button accountButton
         = new Button("Account Log-In", btnX, btnY + 2*btnDiff, btnWidth, btnHeight, accountMenu);
        Button chooseCharButton
         = new Button("Choose Character", btnX,  btnY + btnDiff, btnWidth, btnHeight, characterMenu);
        
        buttons = new ButtonGroup(new Button[] { singlePlayerButton,
                multiplayerButton, accountButton, chooseCharButton });
    }
    
    @Override
    public void show() {        
        batch = new SpriteBatch();
        buttonsAtlas = new TextureAtlas("buttons.png");
        background = new Texture("splash.jpg");
        wordmark = new Sprite( new Texture("wordmark.png") );
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1200);
        viewport = new FillViewport(1920, 1200, camera);
        viewport.apply(true);
        batch.setProjectionMatrix(camera.combined);
        
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        batch.draw(background, 0, 0);
        batch.draw(wordmark, 489, 700);
        batch.end();
        
        batch.begin();
        buttons.drawLabels(batch, font);
        batch.end();
    }
    
    private int touchNumber = -1;
    
    @Override
    public boolean touchDown(int x, int y, int pointer, int btn)
    {
        if (touchNumber != -1) {
            return true;
        }
        Vector3 coords = new Vector3(x, y, 0);
        coords = camera.unproject(coords);
        System.out.println(coords);
        buttons.touchDown(coords.x, coords.y);
        
        touchNumber = pointer;
        return true;
    }
    
    @Override
    public boolean touchUp(int x, int y, int pointer, int btn)
    {
        if (touchNumber == pointer) {
            Vector2 coords = new Vector2(x, y);
            coords = viewport.unproject(coords);
            buttons.touchUp(coords.x, coords.y);
            
            if ( buttons.wasClicked() ) {
                theGame.setScreen((Screen) buttons.getObject());
            }
        }
        
        touchNumber = -1;
        return true;
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
        batch.dispose();
        background.dispose();
        wordmark.getTexture().dispose();
        font.dispose();
    }

    @Override
    public void resize(int width, int height) {
        
    }
}
