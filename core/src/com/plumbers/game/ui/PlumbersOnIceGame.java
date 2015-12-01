package com.plumbers.game.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.plumbers.game.Controller;
import com.plumbers.game.GameView;
import com.plumbers.game.KeyboardController;
import com.plumbers.game.TouchscreenController;

public class PlumbersOnIceGame extends Game {
//    private MainMenu mainMenu;
//    private AccountLoginScreen accountScreen;
//    private LevelSelectionScreen levelsScreen;
//    private SettingsScreen settingsScreen;
//    private MultiplayerLobbyScreen lobbyScreen;
    private GameView gameView;
    
    public static PlumbersOnIceGame createAndroidInstance(int displayWidth) {
        Viewport viewport = new FitViewport(GameView.VIRTUAL_WIDTH,
                GameView.VIRTUAL_HEIGHT);
        Controller controller = new TouchscreenController(displayWidth);
        
        return new PlumbersOnIceGame(viewport, controller);
    }
    
    public static PlumbersOnIceGame createDefaultInstance() {
        return new PlumbersOnIceGame( new ScreenViewport(), new KeyboardController() );
    }
    
    private PlumbersOnIceGame(Viewport viewport, Controller controller) {
        gameView = new GameView("grassy.tmx", "hero",
                                viewport, controller, 0.5f);
    }
    
    @Override
    public void create() {
        long time = System.currentTimeMillis();
        gameView.load();
        Gdx.app.log("loading time", "" + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        System.gc();
        Gdx.app.log("GC time", "" + (System.currentTimeMillis() - time));
        setScreen(gameView);
    }

}
