package com.plumbers.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.plumbers.game.server.EventMessage;
import com.plumbers.game.server.GameConnection;
import com.plumbers.game.server.StateMessage;

public class Player extends Character {
    private Controller controller;
    private GameConnection connection;
    private int coinsCollected = 0;
    protected boolean jumped = false;
    protected boolean hurt = false;
    private int jumpStarted;
    private boolean finished;

    /* ---- */
    private boolean twoPlayerMode;
    /* ---- */
    private final List<Event> coinEvents = new ArrayList<Event>();
    /* ---- */

    private static final float ACCELERATION = 1/24f, // 1/24f //1/14f,
                               DECELERATION = -0.3f, //-0.3f,
                               MAX_SPEED = 2.2f, //2.75f,
                               JUMP_POWER = -2.2f, //-2.75f,
                               JUMP_BOOST = -1/20f,
                               JUMP_FWD_ASSIST = 0.75f;
    private static final int JUMP_BOOST_DURATION = 24;

    public Player(String name, TextureAtlas textureAtlas, Controller controller) {
        super(name);
        this.controller = controller;

        Array<TextureRegion> walkFrames = new Array<TextureRegion>();

        for (int i = 0; i < 6; i++) {
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

    public final int getCoinsCollected() {
        return coinsCollected;
    }

    public final void incrementCoins() {
        ++coinsCollected;
    }

    public final void set2PlayerMode(boolean twoPlayer) {
        twoPlayerMode = twoPlayer;
    }

    public final void finished() {
        finished = true;
        setXAccel(DECELERATION);
    }

    public final void setGameConnection(GameConnection connection) {
        this.connection = connection;
    }

    public final void setController(Controller c) {
        controller = c;
    }

    @Override
    public void preVelocityLogic(int tickNumber) {
        if ( getState() == State.DYING || finished ) {
            return;
        }

        if ( getState() == State.JUMPING ) {
            if ( tickNumber <= jumpStarted + JUMP_BOOST_DURATION
                    && controller.pollJumpInput() ) {
                // allow jump acceleration to continue
            } else {
                setYAccel(GameModel.GRAVITY);
            }

            if ( controller.pollRunInput() ) {
                addXVelocityModifier(JUMP_FWD_ASSIST);
            }
        }

        if ( getState() == State.STANDING || getState() == State.RUNNING ) {
            if ( controller.pollJumpInput() ) {
                jumped = true;
                jumpStarted = tickNumber;
                setState( State.JUMPING );
                setYVelocity(JUMP_POWER);
                setYAccel(JUMP_BOOST);
                setXAccel(0);
            } else if ( controller.pollRunInput() ) {
                setXAccel(ACCELERATION);
                setState( State.RUNNING );
            } else {
                setXAccel(DECELERATION);
            }
        }
    }

    @Override
    public void prePositionLogic(int tickNumber) {
        if ( getState() == State.DYING ) {
            return;
        }
        float Vx = getXVelocity();

        if (Vx > MAX_SPEED) {
            setXVelocity(MAX_SPEED);
        } else if (Vx < 0) {
            setXVelocity(0);
        }
    }

    @Override
    public void postMotionLogic(int tickNumber) {
        if ( getState() == State.RUNNING && getXVelocity() == 0 ) {
            setState( State.STANDING );
        }
        if ( getState() != State.DYING && controller.pollKillKey() ) {
            beKilled();
        }

        if (twoPlayerMode && tickNumber % 8 == 0) {
            StateMessage msg = StateMessage.obtain();
            msg.setValues(this, tickNumber);
            connection.enqueue(msg);
        }
    }

    public Event getEvent() {
        boolean jumped = this.jumped;
        boolean hurt = this.hurt;
        this.jumped = false;
        this.hurt = false;

        if (hurt) {
            return DamageEvent.playerOneInstance();
        } else if (jumped && getState() == State.JUMPING) {
            return JumpEvent.playerOneInstance();
        } else {
            return null;
        }
    }

    public void beKilled() {
        setState(State.DYING);
        setAcceleration(0, GameModel.GRAVITY);
        setVelocity(-0.75f, -4.0f);
        hurt = true;
    }

    public void reset(Vector position) {
        if (! twoPlayerMode) {
            coinsCollected = 0;
        }
        setPosition(position);
        setVelocity(0, 0);
        setAcceleration(0, GameModel.GRAVITY);
        setState(Character.State.FALLING);
    }

    @Override
    public void respondToCollision(Block block, Rectangle.Collision info) {
        State state = getState();

        if (info.getDirection() == Direction.TOP) {
            setYAccel(0);
            setYVelocity(0);
            setYPosition( block.getRectangle().getY() - getRectangle().getH() - rectRelPosY() );

            if (state == State.JUMPING || state == State.FALLING) {
                setState(State.RUNNING);
            }     
        } else if (info.getDirection() == Direction.LEFT) {
            setXAccel(0);
            setXVelocity(0);
            setXPosition( getXPosition() - info.getDistance() );

            if (state == State.JUMPING && getYVelocity() > 0) {
                setState(State.FALLING);
            }        
        } else if (info.getDirection() == Direction.BOTTOM) {
            setYVelocity(0);
            setYPosition( getYPosition() + info.getDistance() );

        } else if (info.getDirection() == Direction.RIGHT) {
            setXAccel(0);
            setXVelocity(0);
            setXPosition( getXPosition() + info.getDistance() );
        }
    }

    @Override
    public void respondToUnsupported() {
        setState(State.FALLING);
        setXAccel(0);
        setYAccel(GameModel.GRAVITY);
    }

    public void ceilingCheck() {
        if ( getRectangle().getY() <= 0 ) {
            setYAccel(GameModel.GRAVITY);
            setYVelocity(0);
            setYPosition( - rectRelPosY() );
        }
    }

    public void hazardCollisionCheck(List<? extends Hazard> hazards) {
        if ( getState() == State.DYING ) {
            return;
        }
        Rectangle rect = getRectangle();

        for (int i = 0; i < hazards.size(); i++) {
            if (rect.intersects( hazards.get(i).getRectangle() )) {
                beKilled();
            }
        }
    }

    public List<Event> coinCollectCheck(List<Coin> coins, int tickNum) {
        if ( getState() == State.DYING ) {
            return Collections.emptyList();
        }

        Rectangle rect = getRectangle();
        coinEvents.clear();

        for (int i = 0; i < coins.size(); i++) {
            Coin coin = coins.get(i);

            if (! coin.isCollected() && rect.intersects(coin.getRectangle())) {
                coin.setCollected(true);
                coinEvents.add( CoinEvent.instance() );

                if (twoPlayerMode) {
                    EventMessage msg = EventMessage.obtain();
                    msg.coin(tickNum, true, coin.getColumn(), coin.getRow());
                    connection.enqueue(msg);
                }
            }
        }
        return coinEvents;
    }

    public boolean fallingDeathCheck(float bottom) {
        return (getYPosition() > bottom);
    }

}
