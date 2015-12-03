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
    private static PlumbersOnIceGame instance;
    private MainMenu mainMenu;
    private GameView gameView;
    
    private Viewport viewport;
    private Controller controller;
    private static String hostname;
    
    public static PlumbersOnIceGame createAndroidInstance(int displayWidth) {
        if (instance != null)
            throw new IllegalStateException();
        
        Viewport viewport = new FitViewport(GameView.VIRTUAL_WIDTH,
                GameView.VIRTUAL_HEIGHT);
        Controller controller = new TouchscreenController(displayWidth);
        
        hostname = "192.168.0.3";
        instance =  new PlumbersOnIceGame(viewport, controller);
        return instance;
    }
    
    public static PlumbersOnIceGame createDefaultInstance() {
        if (instance != null)
            throw new IllegalStateException();
        
        hostname = "localhost";
        instance = new PlumbersOnIceGame( new ScreenViewport(), new KeyboardController() );
        return instance;
    }
    
    public static void startSinglePlayer(String level, String character) {
      instance.gameView = new GameView(level, character, instance.viewport, instance.controller, 0.5f);
      instance.gameView.load();
      System.gc();
      instance.setScreen(instance.gameView);
    }
    
    public static void returnToMenu() {
        instance.setScreen(instance.mainMenu);
      }
    
    private PlumbersOnIceGame(Viewport viewport, Controller controller) {
        this.viewport = viewport;
        this.controller = controller;
    }
    
    @Override
    public void create() {
        mainMenu = new MainMenu(this);
        setScreen(mainMenu);
    }

    public static void disposeGameView() {
        if (instance.gameView != null)
            instance.gameView.dispose(); 
    }
    
}
