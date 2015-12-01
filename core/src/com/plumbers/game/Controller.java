package com.plumbers.game;

import com.badlogic.gdx.InputProcessor;
import com.plumbers.game.server.EventMessage;
import com.plumbers.game.server.GameConnection;

public abstract class Controller implements InputProcessor {
    public abstract boolean pollRunInput();
    public abstract boolean pollJumpInput();
    public abstract boolean pollKillKey();
    
    private GameConnection connection;
    private int gameTick;
    
    public final void setGameConnection(GameConnection connection) {
        this.connection = connection;
    }
    
    public final void setTickNumber(int gameTick) {
        this.gameTick = gameTick;
    }
    
    public final void runInputDown() {
        if (connection == null) {
            return;
        }
        EventMessage m = EventMessage.obtain();
        m.runInput(gameTick);
        connection.enqueue(m);
    }
    
    public final void runInputUp() {
        if (connection == null) {
            return;
        }
        EventMessage m = EventMessage.obtain();
        m.runInputEnd(gameTick);
        connection.enqueue(m);
    }
    
    public final void jumpInputDown() {
        if (connection == null) {
            return;
        }
        EventMessage m = EventMessage.obtain();
        m.jumpInput(gameTick);
        connection.enqueue(m);
    }
    
    public final void jumpInputUp() {
        if (connection == null) {
            return;
        }
        EventMessage m = EventMessage.obtain();
        m.jumpInputEnd(gameTick);
        connection.enqueue(m);
    }
}
