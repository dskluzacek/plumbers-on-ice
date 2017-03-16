package com.plumbers.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.plumbers.game.server.EventMessage;
import com.plumbers.game.server.GameConnection;
import com.plumbers.game.server.StateMessage;

/**
 * The human-controlled player character. 
 */
public class Player extends Character
{
    private Controller controller;
    private GameConnection connection;
    private int coinsCollected = 0;
    private boolean jumped = false;
    private boolean hurt = false;
    private int jumpStartedTick;   // tick number when the most recent jump started
    private boolean finished;

    /* ---- */
    private boolean twoPlayerMode;
    private final List<Event> coinEvents = new ArrayList<Event>();
    /* ---- */

    private static final float ACCELERATION = 11/75f, //1/14f,
                               DECELERATION = -1.2f, 
                               MAX_SPEED = 4.4f,      //2.75f,
                               JUMP_POWER = -4.4f,    //-2.75f,
                               JUMP_BOOST = -1/5f,
                               JUMP_FWD_ASSIST = 1.5f;
    private static final int JUMP_BOOST_DURATION = 12;
    private static final float LOWER_LEFT_TOLERANCE = 2.0f,
                               DEATH_VX = -1.5f,
                               DEATH_VY = -8.0f;
    
    /**
     * Constructs a Player using the given character name.
     * 
     * @param name The name of the character to use, corresponding to its texture regions
     * @param textureAtlas The texture atlas on which the character is found
     * @param controller The controller to use
     */
    public Player(String name, TextureAtlas textureAtlas, Controller controller)
    {
        super(name);
        this.controller = controller;

        Array<TextureRegion> walkFrames = new Array<TextureRegion>();

        for (int i = 0; i < 6; i++)
        {
            TextureRegion region = textureAtlas.findRegion(name + "-walk" + i);

            if ( region != null ) {
                walkFrames.add(region);
            }
        }
        
        TextureRegion[] idleFrames = new TextureRegion[] {
                textureAtlas.findRegion(name+"-idle1"),
                textureAtlas.findRegion(name+"-idle2"),
                textureAtlas.findRegion(name+"-idle3") };

        Animation walkAnimation = new Animation(1/10f, walkFrames);
        Animation idleAnimation = new Animation(1/3f, idleFrames);
        Animation jumpAnimation =
            new Animation( 1, new TextureRegion[]{ textureAtlas.findRegion(name+"-jump") } );
        Animation landAnimation =
            new Animation( 1, new TextureRegion[]{ textureAtlas.findRegion(name+"-land") } );
        Animation knockbackAnimation =
            new Animation( 1, new TextureRegion[]{ textureAtlas.findRegion(name+"-knockback") } );

        setMovementAnim( new MovementAnimation(idleAnimation, walkAnimation,
                                 jumpAnimation, landAnimation, knockbackAnimation) );
    }

    public final int getCoinsCollected()
    {
        return coinsCollected;
    }

    public final void incrementCoins()
    {
        ++coinsCollected;
    }

    public final void set2PlayerMode(boolean twoPlayer)
    {
        twoPlayerMode = twoPlayer;
    }
    
    public final boolean isLevelCompleted()
    {
        return finished;
    }

    public final void setGameConnection(GameConnection connection)
    {
        this.connection = connection;
    }

    public final void setController(Controller c)
    {
        controller = c;
    }

    @Override
    public void preVelocityLogic(int tickNumber)
    {
        if ( getState() == State.DYING || finished )
        {
            return;
        }

        if ( getState() == State.JUMPING )
        {
            // allows player to control height of jump by holding the input, or not
            if ( tickNumber <= jumpStartedTick + JUMP_BOOST_DURATION
                    && controller.pollJumpInput() )
            {
                // then, allow jump acceleration to continue
            }
            else
            {
                // else, let gravity take over once again
                setYAccel(GameModel.GRAVITY);
            }
            
            // allows player to get a forward boost in mid-air by holding run input
            if ( controller.pollRunInput() )
            {
                addXVelocityModifier(JUMP_FWD_ASSIST);
            }
        }

        if ( getState() == State.STANDING || getState() == State.RUNNING )
        {
            if ( controller.pollJumpInput() )
            {
                startJump(tickNumber);
            }
            else if ( controller.pollRunInput() )
            {
                setXAccel(ACCELERATION);
                setState( State.RUNNING );
            }
            else
            {
                setXAccel(DECELERATION);
            }
        }
    }
    
    @Override
    public void prePositionLogic(int tickNumber)
    {
        if ( getState() == State.DYING )
        {
            return;
        }
        float Vx = getXVelocity();

        if (Vx > MAX_SPEED)
        {
            setXVelocity(MAX_SPEED);
        }
        else if (Vx < 0)
        {
            setXVelocity(0);
        }
    }

    @Override
    public void postMotionLogic(int tickNumber)
    {
        if ( getState() == State.RUNNING && getXVelocity() == 0 )
        {
            setState( State.STANDING );
        }
        if ( getState() != State.DYING && controller.pollKillKey() )
        {
            beKilled();
        }

        if (twoPlayerMode && tickNumber % 8 == 0)
        {
            StateMessage msg = StateMessage.obtain();
            msg.setValues(this, tickNumber);
            connection.enqueue(msg);
        }
    }
    
    /**
     * Returns any outstanding events resulting from simulation logic.
     */
    public Event getEvent()
    {
        boolean jumped = this.jumped;
        boolean hurt = this.hurt;
        this.jumped = false;
        this.hurt = false;

        if (hurt)
        {
            return DamageEvent.playerOneInstance();
        }
        else if (jumped && getState() == State.JUMPING)
        {
            return JumpEvent.playerOneInstance();
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public void draw(Batch batch, float elapsedTime)
    {
        float xPos = getXPosition();
        setXPosition( MathUtils.floor(xPos) );
        super.draw(batch, elapsedTime);
        setXPosition(xPos);
    }
    
    public void reset(Vector position) 
    {
        if (! twoPlayerMode)
        {
            coinsCollected = 0;
        }
        setPosition(position);
        setVelocity(0, 0);
        setAcceleration(0, GameModel.GRAVITY);
        setState(Character.State.FALLING);
    }

    @Override
    public void respondToCollision(Block block, Rectangle.Collision info)
    {
        State state = getState();
        
        if (info.getDirection() == Direction.TOP)
        {
            setYAccel(0);
            setYVelocity(0);
            setYPosition( block.getRectangle().getY() - getRectangle().getH() - rectOffsetY() );

            if (state == State.JUMPING || state == State.FALLING)
            {
                setState(State.RUNNING);
            }     
        }
        else if (info.getDirection() == Direction.LEFT)
        {
            Rectangle blockRect = block.getRectangle();
            float blockBottom = blockRect.getY() + blockRect.getH();
            
            // don't make player fall from hitting bottom left corner
            if (getRectangle().getY() > blockBottom - LOWER_LEFT_TOLERANCE)
            {
                setYPosition( blockBottom - rectOffsetY() );
            }
            else
            {
                leftCollision(state, info);
            }
        }
        else if (info.getDirection() == Direction.BOTTOM)
        {
            setYVelocity(0);
            setYPosition( getYPosition() + info.getDistance() );
        }
        else if (info.getDirection() == Direction.RIGHT)
        {
            setXAccel(0);
            setXVelocity(0);
            setXPosition( getXPosition() + info.getDistance() );
        }
    }
    
    @Override
    public void respondToUnsupported()
    {
        setState(State.FALLING);
        setXAccel(0);
        setYAccel(GameModel.GRAVITY);
    }

    public void ceilingCheck()
    {
        if ( getRectangle().getY() <= 0 )
        {
            setYAccel(GameModel.GRAVITY);
            setYVelocity(0);
            setYPosition( - rectOffsetY() );
        }
    }

    public void hazardCollisionCheck(Iterator<? extends Hazard> hazards)
    {
        if ( getState() == State.DYING )
        {
            return;
        }
        Rectangle rect = getRectangle();

        while ( hazards.hasNext() )
        {
            if ( rect.intersects(hazards.next().getRectangle()) )
            {
                beKilled();
            }
        }
    }

    public List<Event> coinCollectCheck(Iterator<Coin> coins, int tickNum)
    {
        if ( getState() == State.DYING )
        {
            return Collections.emptyList();
        }

        Rectangle rect = getRectangle();
        coinEvents.clear();

        while ( coins.hasNext() )
        {
            Coin coin = coins.next();

            if ( ! coin.isCollected() && rect.intersects(coin.getRectangle()) )
            {
                coin.setCollected(true);
                coinEvents.add( CoinEvent.instance() );

                if (twoPlayerMode)
                {
                    EventMessage msg = EventMessage.obtain();
                    msg.coin(tickNum, true, coin.getColumn(), coin.getRow());
                    connection.enqueue(msg);
                }
            }
        }
        return coinEvents;
    }
    
    public Event springboardCheck(Iterator<Springboard> springboards, int tickNumber)
    {
        if ( getState() == State.DYING ) {
            return null;
        }
        Rectangle rect = getRectangle();
        boolean eventOccurred = false;
        
        while ( springboards.hasNext() )
        {
            Springboard sb = springboards.next();
            
            if ( rect.intersects(sb.getRectangle()) )
            {
                Rectangle.Collision coll = rect.collisionInfo(
                        sb.getRectangle(),
                        getPreviousX() + rectOffsetX(),
                        getPreviousY() + rectOffsetY() );
                if (sb.isReady(tickNumber) && coll.getDirection() == Direction.TOP)
                {
                    if ( controller.pollJumpInput() )
                    {
                        startJump(tickNumber);
                        setYVelocity( sb.getPlayerVelocityWithJump() );
                        setYAccel( sb.getPlayerJumpBoost() );
                    }
                    else
                    {
                        setState(State.JUMPING);
                        setYVelocity( sb.getPlayerVelocity() );
                    }
                    setYPosition(sb.getRectangle().getY() - rect.getH() - rectOffsetY());
                    setXVelocity(0);
                    eventOccurred = true;
                    sb.activate(tickNumber);
                }
                else if (coll.getDirection() == Direction.LEFT)
                {
                    leftCollision(getState(), coll);
                }
                /* --- */
                Rectangle.disposeOf(coll);
            }
        }
        
        if (eventOccurred) {
            return SpringboardEvent.instance();
        }
        else {
            return null;
        }
    }

    public DeathEvent fallingDeathCheck(float bottom)
    {
        if (getYPosition() > bottom) {
            return DeathEvent.playerOneInstance();
        }
        else {
            return null;
        }
    }
    
    public FinishEvent finishedLevelCheck(Rectangle goal)
    {
        if ( getRectangle().intersects(goal) )
        {
            setXAccel(DECELERATION);            
            finished = true;
            return FinishEvent.playerOneInstance();
        }
        else
        {
            return null;
        }
    }
    
    protected void beKilled()
    {
        setState(State.DYING);
        setAcceleration(0, GameModel.GRAVITY);
        setVelocity(DEATH_VX, DEATH_VY);
        hurt = true;
    }
    
    private void startJump(int tickNumber)
    {
        jumped = true;
        jumpStartedTick = tickNumber;
        setState( State.JUMPING );
        setYVelocity(JUMP_POWER);
        setYAccel(JUMP_BOOST);
        setXAccel(0);
    }
    
    // respond to collision with an object's left edge, whether a block or something else
    private void leftCollision(State state, Rectangle.Collision info)
    {
        setXAccel(0);
        setXVelocity(0);
        setXPosition( getXPosition() - info.getDistance() );

        if ((state == State.JUMPING || state == State.FALLING) && getYVelocity() > 0)
        {
            setState(State.FALLING);
            setYVelocity(0);
        }
    }
}
