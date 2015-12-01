package com.plumbers.game.ui;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.plumbers.game.Controller;
import com.plumbers.game.GameView;
import com.plumbers.game.KeyboardController;
import com.plumbers.game.TouchscreenController;
import com.plumbers.game.server.GameConnection;

public class PlumbersOnIceGame extends Game {
//    private MainMenu mainMenu;
//    private AccountLoginScreen accountScreen;
//    private LevelSelectionScreen levelsScreen;
//    private SettingsScreen settingsScreen;
//    private MultiplayerLobbyScreen lobbyScreen;
    private GameView gameView;
    
    private Viewport viewport;
    private Controller controller;
    
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
        this.viewport = viewport;
        this.controller = controller;
    }
    
    @Override
    public void create() {
        try {
            GameConnection connection = new GameConnection("localhost", 7684, "hero");
            gameView = new GameView(connection, "hero", viewport, controller, 0.5f);
            connection.handshake();
            gameView.load();
            System.gc();
            connection.ready();
            System.out.println("starting...");
            setScreen(gameView);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
