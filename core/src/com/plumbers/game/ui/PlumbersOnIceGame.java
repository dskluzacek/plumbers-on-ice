package com.plumbers.game.ui;

import com.badlogic.gdx.Game;
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
    
    public PlumbersOnIceGame(Platform platform, int displayWidth) {
        Viewport viewport;
        Controller controller;
        
        switch (platform) {
         case ANDROID:
            viewport = new FitViewport(GameView.VIRTUAL_WIDTH,
                                       GameView.VIRTUAL_HEIGHT);
            controller = new TouchscreenController(displayWidth);
            break;
         case DESKTOP:
         default:
            viewport = new ScreenViewport();
            controller = new KeyboardController();
            break;
        }
        gameView = new GameView("grassy.tmx", "hero",
                                viewport, controller, 0.5f);
    }
    
    @Override
    public void create() {
        setScreen(gameView);
    }

}
