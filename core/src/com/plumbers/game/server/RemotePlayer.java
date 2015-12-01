package com.plumbers.game.server;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Pools;
import com.plumbers.game.Coin;
import com.plumbers.game.Controller;
import com.plumbers.game.DamageEvent;
import com.plumbers.game.Event;
import com.plumbers.game.Hazard;
import com.plumbers.game.JumpEvent;
import com.plumbers.game.Player;
import com.plumbers.game.server.EventMessage.MsgType;

public class RemotePlayer extends Player {
    private Map<Integer, StateMessage> stateMsgMap = new ConcurrentHashMap<Integer, StateMessage>();
    private Queue<EventMessage> messageQueue = new ConcurrentLinkedQueue<EventMessage>();
    private NetworkController controller;
    private int internalTickNum;
    private boolean dead = false;
    
    public RemotePlayer(String name, TextureAtlas textureAtlas) {
        super(name, textureAtlas, null);
        controller = new NetworkController();
        setController(controller);
        set2PlayerMode(true);
    }
    
    public void putStateMessage(StateMessage m) {
        stateMsgMap.put(m.getTickNumber(), m);
    }

    public void pushEventMessage(EventMessage m) {
        messageQueue.add(m);
    }

    @Override
    public void preVelocityLogic(int tickNumber) {
        internalTickNum = tickNumber - 36000;
        
        EventMessage message = messageQueue.peek();
        
        while (message != null && message.getTickBegin() == internalTickNum)
        {
            if (message.getType() == MsgType.RUN_INPUT)
            {
                controller.runInputStarted = internalTickNum;
            }
            else if (message.getType() == MsgType.RUN_INPUT_END)
            {
                controller.runInputStarted = -1;
            }
            else if (message.getType() == MsgType.JUMP_INPUT)
            {
                controller.jumpInputStarted = internalTickNum;
            }
            else if (message.getType() == MsgType.JUMP_INPUT_END)
            {
                controller.jumpInputStarted = -1;
            }
            else if (message.getType() == MsgType.COIN)
            {
                // means the coin was not gotten by the local player
                // so it was the other/remote player
                if ( ! message.appliesToLocalPlayer() ) {
                    incrementCoins();
                }
            }
            else if (message.getType() == MsgType.DAMAGED)
            {
                setPosition(message.getX(), message.getY());
                beKilled();
            }
            else if (message.getType() == MsgType.DIED)
            {
                dead = true;
            }
            messageQueue.remove();
            Pools.free(message);
            message = messageQueue.peek();
        }
        
        StateMessage stateMsg = stateMsgMap.remove(internalTickNum);
        
        if (stateMsg != null) {
            stateMsg.updatePosition(this);
            stateMsg.updateVelocity(this);
            stateMsg.updateAcceleration(this);
            setState( stateMsg.getState() );
            
            Pools.free(stateMsg);
        }
        super.preVelocityLogic(internalTickNum);
    }
    
    @Override
    public void prePositionLogic(int n) {
        super.prePositionLogic(internalTickNum);
    }

    @Override
    public void postMotionLogic(int n) {
        super.postMotionLogic(internalTickNum);
    }

    @Override
    public Event getEvent() {
        boolean jumped = this.jumped;
        boolean hurt = this.hurt;
        this.jumped = false;
        this.hurt = false;
        
        if (hurt) {
            return DamageEvent.playerTwoInstance();
        } else if (jumped && getState() == State.JUMPING) {
            return JumpEvent.playerTwoInstance();
        } else {
            return null;
        }
    }

    @Override
    public void hazardCollisionCheck(List<? extends Hazard> hazards) {
    }

    @Override
    public List<Event> coinCollectCheck(List<Coin> coins, int tickNum) {
        return Collections.emptyList();
    }

    @Override
    public boolean fallingDeathCheck(float bottom) {
        boolean died = dead;
        dead = false;
        return died;
    }

    private class NetworkController extends Controller
    {
        int runInputStarted = -1;
        int jumpInputStarted = -1;
        
        @Override
        public boolean pollRunInput() {
            return runInputStarted != -1;
        }

        @Override
        public boolean pollJumpInput() {
            return jumpInputStarted != -1;
        }
        
        @Override
        public boolean pollKillKey() {
            return false;
        }
        /* ------------------------------ */
        
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button)
        {    return false; }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }
}
