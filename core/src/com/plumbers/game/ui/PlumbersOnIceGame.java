package com.plumbers.game.ui;

import com.badlogic.gdx.Game;
import com.plumbers.game.Controller;
import com.plumbers.game.GameView;
import com.plumbers.game.KeyboardController;
import com.plumbers.game.TouchscreenController;

public final class PlumbersOnIceGame extends Game
{
    private static PlumbersOnIceGame instance;
    private MainMenu mainMenu;
    private GameView gameView;
    private Controller controller;

    public static PlumbersOnIceGame createAndroidInstance(int displayWidth)
    {
        if (instance != null) {
            throw new IllegalStateException();
        }
        
        Controller controller = new TouchscreenController(displayWidth);
        instance = new PlumbersOnIceGame(controller);
        return instance;
    }
    
    public static PlumbersOnIceGame createDefaultInstance()
    {
        if (instance != null) {
            throw new IllegalStateException();
        }
        
        instance = new PlumbersOnIceGame( new KeyboardController() );
        return instance;
    }
    
    public static void startSinglePlayer(String level, String character)
    {
        instance.gameView = new GameView(level,
                                         character,
                                         instance.controller,
                                         0.5f);
        instance.gameView.load();
        System.gc();
        instance.setScreen(instance.gameView);
    }
    
    public static void returnToMenu()
    {
        instance.setScreen(instance.mainMenu);
    }
    
    private PlumbersOnIceGame(Controller controller) 
    {
        this.controller = controller;
    }
    
    @Override
    public void create()
    {
        mainMenu = new MainMenu(this);
        setScreen(mainMenu);
    }

    public static void disposeGameView()
    {
        if (instance.gameView != null)
        {
            instance.gameView.dispose();
            instance.gameView = null;
        }
    }
    
}
