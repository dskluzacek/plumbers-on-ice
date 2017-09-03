package com.plumbers.game.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public final class MainMenu extends InputAdapter implements Screen
{
    private Viewport viewport;
    private OrthographicCamera camera;
    private Sprite background;
    private Sprite levelsBackground;
    private Batch batch;
    private ButtonGroup currentGroup;
    private ButtonGroup mainButtons;
    private ButtonGroup levelButtons;
    private Game theGame;
    private LevelsMenu levelMenu = new LevelsMenu();

    private static String characterChoice = "hero";

    public static final String LEVEL_1_FILE = "grassy1.tmx",
                               LEVEL_2_FILE = "grassy2.tmx",
                               LEVEL_3_FILE = "winter1.tmx",
                               LEVEL_4_FILE = "tropical1.tmx",
                               LEVEL_5_FILE = "castle1.tmx";

    public MainMenu(Game game)
    {
        theGame = game;
    }

    @Override
    public void show()
    {
        if (batch != null)
        {
            currentGroup = mainButtons;
            return;
        }

        batch = new SpriteBatch();
        background = new Sprite( new Texture("menu.jpg") );
        levelsBackground = new Sprite( new Texture("levels.jpg") );
        background.setSize(1920, 1200);
        levelsBackground.setSize(1920, 1200);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1200);
        viewport = new FillViewport(1920, 1200, camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        viewport.apply(true);
        batch.setProjectionMatrix(camera.combined);

        final int btnX = 560;
        final int btnWidth = 800;
        final int btnY = 212;
        final int btnHeight = 80;
        final int btnDiff = 27 + btnHeight;

        Button singlePlayerButton = new Button(btnX, btnY + 3 * btnDiff, btnWidth, btnHeight,
                new Button.Listener()
                {
                    public void action()
                    {
                        System.out.println("play");
                        theGame.setScreen(levelMenu);
                    }
                });
        Button multiplayerButton = new Button(btnX, btnY + 2 * btnDiff, btnWidth, btnHeight,
                new Button.Listener()
                {
                    public void action()
                    {
                    }
                });
        Button accountButton = new Button(btnX, btnY + 1 * btnDiff, btnWidth, btnHeight,
                new Button.Listener()
                {
                    public void action()
                    {
                        System.out.println("accounts");
                    }
                });
        Button chooseCharButton = new Button(btnX, btnY, btnWidth, btnHeight,
                new Button.Listener()
                {
                    public void action()
                    {
                    }
                });

        mainButtons = new ButtonGroup(new Button[] { singlePlayerButton,
                multiplayerButton, accountButton, chooseCharButton });

        currentGroup = mainButtons;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        background.draw(batch);
        batch.end();
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int btn)
    {
        Vector3 coords = new Vector3(x, y, 0);
        coords = camera.unproject(coords);
        currentGroup.touchDown(coords.x, coords.y);

        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int btn)
    {
        return true;
    }

    private class LevelsMenu extends ScreenAdapter
    {

        public LevelsMenu()
        {
            Button[] buttons = new Button[5];

            buttons[0] = new Button(510, 360, 340, 330, new Button.Listener()
            {
                public void action()
                {
                    PlumbersOnIceGame.startSinglePlayer(LEVEL_1_FILE,
                            characterChoice);
                }
            });

            buttons[1] = new Button(790, 360, 340, 330, new Button.Listener()
            {
                public void action()
                {
                    PlumbersOnIceGame.startSinglePlayer(LEVEL_2_FILE,
                            characterChoice);
                }
            });

            buttons[2] = new Button(1210, 360, 340, 330, new Button.Listener()
            {
                public void action()
                {
                    PlumbersOnIceGame.startSinglePlayer(LEVEL_3_FILE,
                            characterChoice);
                }
            });
            buttons[3] = new Button(570, 130, 340, 330, new Button.Listener()
            {
                public void action()
                {
                    PlumbersOnIceGame.startSinglePlayer(LEVEL_4_FILE,
                            characterChoice);
                }
            });
            buttons[4] = new Button(1010, 130, 340, 330, new Button.Listener()
            {
                public void action()
                {
                    PlumbersOnIceGame.startSinglePlayer(LEVEL_5_FILE,
                            characterChoice);
                }
            });

            levelButtons = new ButtonGroup(buttons);
        }

        @Override
        public void show()
        {
            currentGroup = levelButtons;
        }

        @Override
        public void render(float delta)
        {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            levelsBackground.draw(batch);
            batch.end();
        }
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {
        currentGroup = mainButtons;
        PlumbersOnIceGame.disposeGameView();
    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        batch.dispose();
        background.getTexture().dispose();
        levelsBackground.getTexture().dispose();
    }

    @Override
    public void resize(int width, int height)
    {

    }
}
