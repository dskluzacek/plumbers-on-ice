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
    private MainMenu mainMenu;
//    private AccountLoginScreen accountScreen;
//    private LevelSelectionScreen levelsScreen;
//    private CharacterSelectionScreen settingsScreen;
//    private MultiplayerLobbyScreen lobbyScreen;
    private GameView gameView;
    
    private Viewport viewport;
    private Controller controller;
    private static String hostname;
    
    public static PlumbersOnIceGame createAndroidInstance(int displayWidth) {
        Viewport viewport = new FitViewport(GameView.VIRTUAL_WIDTH,
                GameView.VIRTUAL_HEIGHT);
        Controller controller = new TouchscreenController(displayWidth);
        
        hostname = "10.10.73.167";
        return new PlumbersOnIceGame(viewport, controller);
    }
    
    public static PlumbersOnIceGame createDefaultInstance() {
        hostname = "localhost";
        return new PlumbersOnIceGame( new ScreenViewport(), new KeyboardController() );
    }
    
    private PlumbersOnIceGame(Viewport viewport, Controller controller) {
        this.viewport = viewport;
        this.controller = controller;
    }
    
    @Override
    public void create() {
//        try {
//            GameConnection connection = new GameConnection(hostname, 7684, "hero");
//            gameView = new GameView(connection, "hero", viewport, controller, 0.5f);
//            connection.handshake();
//            gameView.load();
//            System.gc();
//            connection.ready();
//            setScreen(gameView);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
        
//        gameView = new GameView("castle.tmx", "hero", viewport, controller, 0.5f);
//        gameView.load();
//        System.gc();
//        setScreen(gameView);
//        
        mainMenu = new MainMenu(this);
        setScreen(mainMenu);
    }
    
    

}
