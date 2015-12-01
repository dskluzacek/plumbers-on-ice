package com.plumbers.game;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Pools;
import com.plumbers.game.server.Message;
import com.plumbers.game.server.Message.MsgType;
import com.plumbers.game.server.StatePacket;

public class RemotePlayer extends Player {
    private Map<Integer, StatePacket> stateMsgMap = new ConcurrentHashMap<Integer, StatePacket>();
    private BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<Message>(20);
    private NetworkController controller;
    private int internalTickNum;
    private boolean dead = false;
    
    public RemotePlayer(String name, TextureAtlas textureAtlas) {
        super(name, textureAtlas, null);
        controller = new NetworkController();
        setController(controller);
    }
    
    public Map<Integer, StatePacket> getStateMsgMap() {
        return stateMsgMap;
    }

    public BlockingQueue<Message> getMessageQueue() {
        return messageQueue;
    }

    @Override
    public void preVelocityLogic(int tickNumber) {
        internalTickNum = tickNumber - 36000;
        
        Message message = messageQueue.peek();
        
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
            else if (message.getType() == MsgType.JUMP)
            {
                controller.jumpInputStarted = internalTickNum;
            }
            else if (message.getType() == MsgType.JUMP_END)
            {
                controller.jumpInputStarted = -1;
            }
            else if (message.getType() == MsgType.COIN)
            {
                // means the coin was not gotten by the local player
                // so it was the remote player
                if ( ! message.isForMyPlayer() ) {
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
        
        StatePacket stateMsg = stateMsgMap.remove(internalTickNum);
        
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
    public Event hazardCollisionCheck(List<? extends Hazard> hazards) {
        return null;
    }

    @Override
    public List<Event> coinCollectCheck(List<Coin> coins) {
        return Collections.emptyList();
    }

    @Override
    public boolean fallingDeathCheck(float bottom) {
        boolean died = dead;
        dead = false;
        return died;
    }

    private class NetworkController extends InputAdapter implements Controller
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
    }
}
