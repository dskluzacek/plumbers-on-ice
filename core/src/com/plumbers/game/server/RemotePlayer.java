package com.plumbers.game.server;


import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Pools;
import com.plumbers.game.DeathEvent;
import com.plumbers.game.Hazard;
import com.plumbers.game.Player;
import com.plumbers.game.server.EventMessage.MsgType;

public class RemotePlayer extends Player {
    private BlockingQueue<StateMessage> stateQueue = new ArrayBlockingQueue<StateMessage>(100);
    private BlockingQueue<EventMessage> messageQueue = new ArrayBlockingQueue<EventMessage>(25);
    private boolean dead = false;
    
    public RemotePlayer(String name, TextureAtlas textureAtlas) {
        super(name, textureAtlas, null);
//        set2PlayerMode(true);
    }
    
    public void putStateMessage(StateMessage m) {
        stateQueue.offer(m);
    }

    public void pushEventMessage(EventMessage m) {
        messageQueue.offer(m);
    }

    @Override
    public void preVelocityLogic(int tickNumber) {
        
        EventMessage message = messageQueue.poll();
        
        if (message != null) {
            if (message.getType() == MsgType.COIN)
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
            Pools.free(message);
        }

        StateMessage stateMsg = stateQueue.poll();
        
        if (stateMsg != null) {
            setXPosition( stateMsg.getXPos() );
            setYPosition( stateMsg.getYPos() );
            setXVelocity( stateMsg.getXVel() );
            setYVelocity( stateMsg.getYVel() );
            
            setState( stateMsg.getState() );
            
            Pools.free(stateMsg);
        }
        if (stateQueue.remainingCapacity() == 0)
            stateQueue.clear();
    }
    
    @Override
    public void prePositionLogic(int n) {
        super.prePositionLogic(n);
    }

    @Override
    public void postMotionLogic(int n) {
        if ( getState() == State.RUNNING && getXVelocity() == 0 ) {
            setState( State.STANDING );
        }
    }

//    @Override
//    public Event getEvent()
//    {
//        Event e = super.getEvent();
//        
//        if (e instanceof DamageEvent)
//        {
//            return DamageEvent.playerTwoInstance();
//        } 
//        else if (e instanceof JumpEvent)
//        {
//            return JumpEvent.playerTwoInstance();
//        }
//        else
//        {
//            return null;
//        }
//    }

    @Override
    public void hazardCollisionCheck(Iterator<? extends Hazard> hazards) {
    }

//    @Override
//    public List<Event> coinCollectCheck(Iterator<Coin> coins, int tickNum) {
//        return Collections.emptyList();
//    }

    @Override
    public DeathEvent fallingDeathCheck(float bottom) {
        boolean died = dead;
        dead = false;
        
        if (died) {
            return DeathEvent.playerTwoInstance();
        }
        else {
            return null;
        }
    }
}
