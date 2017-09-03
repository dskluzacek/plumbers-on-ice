package com.plumbers.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.plumbers.game.Rectangle.Collision;

/**
 * A "springboard" or "trampoline" in the game, which the player can bounce on
 * to jump higher.
 */
public final class Springboard implements InteractiveMapObject, Drawable
{
    private final int column;
    private final int row;
    private final Rectangle hitbox;
    private State state;
    private float currentStateStartTime;
    private int lastActivationTickNumber;
    
    private static TextureRegion frame1;
    private static TextureRegion frame2;
    private static TextureRegion frame3;
    
    private static final float PLAYER_VELOCITY = -13.0f,
                               PLAYER_VELOCITY_WITH_JUMP = -13.325f,
                               PLAYER_JUMP_BOOST = -0.04f;
                              
    private static final float FRAME_2_DOWN_TIME  = 0.05f,
                               FRAME_3_TIME       = 0.075f,
                               FRAME_2_UP_TIME    = 0.05f;
    private static final int   REACTIVATION_TICKS = 10,
                               HITBOX_HEIGHT      = 24;
    
    private enum State
    {
        IDLE,
        FRAME_2_DOWN,
        FRAME_3,
        FRAME_2_UP,
    }
    
    public Springboard(int column, int row)
    {
        this.column = column;
        this.row = row;
        
        hitbox = new Rectangle(column * Block.SIZE,
                        row * Block.SIZE + (Block.SIZE - HITBOX_HEIGHT),
                        Block.SIZE,
                        HITBOX_HEIGHT);
        reset();
    }
    
    public Rectangle getRectangle()
    {
        return hitbox;
    }
    
    public float getPlayerVelocity()
    {
        return PLAYER_VELOCITY;
    }
    
    public float getPlayerVelocityWithJump()
    {
        return PLAYER_VELOCITY_WITH_JUMP;
    }
    
    public float getPlayerJumpBoost()
    {
        return PLAYER_JUMP_BOOST;
    }
    
    public boolean isReady(int tickNumber)
    {
        return (tickNumber > lastActivationTickNumber + REACTIVATION_TICKS);
    }
    
    public void activate(int tickNumber)
    {
        state = State.FRAME_2_DOWN;
        lastActivationTickNumber = tickNumber;
        currentStateStartTime = Float.NaN;  // will be set upon next call to draw() 
    }
    
    /** Call this when the game model is being reset. */
    public void reset()
    {
        state = State.IDLE;
        lastActivationTickNumber = - REACTIVATION_TICKS;
    }

    @Override
    public void draw(Batch batch, float elapsedTime)
    {
        TextureRegion frame;
        
        switch (state)
        {
        case FRAME_2_DOWN:
            frame = frame2;
            if ( Float.isNaN(currentStateStartTime) ) {
                currentStateStartTime = elapsedTime;
            }
            else if (elapsedTime - currentStateStartTime >= FRAME_2_DOWN_TIME) {
                state = State.FRAME_3;
                currentStateStartTime = elapsedTime;
            }
            break;
        case FRAME_3:
            frame = frame3;
            if (elapsedTime - currentStateStartTime >= FRAME_3_TIME) {
                state = State.FRAME_2_UP;
                currentStateStartTime = elapsedTime;
            }
            break;
        case FRAME_2_UP:
            frame = frame2;
            if (elapsedTime - currentStateStartTime >= FRAME_2_UP_TIME) {
                state = State.IDLE;
            }
            break;
        default:
            frame = frame1;
            break;
        }
        batch.draw(frame, column * Block.SIZE, row * Block.SIZE, Block.SIZE, Block.SIZE);
    }
    
    public static void setTextureAtlas(TextureAtlas atlas)
    {
        frame1 = atlas.findRegion("springboard-1");
        frame2 = atlas.findRegion("springboard-2");
        frame3 = atlas.findRegion("springboard-3");
    }

    @Override
    public void activate(IPlayer p, Collision coll, int tickNumber)
    {
        if (tickNumber <= lastActivationTickNumber + REACTIVATION_TICKS)
        {
            return;
        }
        
        state = State.FRAME_2_DOWN;
        lastActivationTickNumber = tickNumber;
        currentStateStartTime = Float.NaN;  // will be set upon next call to draw() 
        
        
    }

}
