package com.plumbers.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.plumbers.game.MovementAnimation.Action;

/**
 * A character in the game, including player character(s), enemies, and any NPCs.
 */
public abstract class Character extends Motionable implements Drawable
{
    private String characterName;
    private Rectangle hitbox = new Rectangle(0, 0, 0, 0);
    private float hitboxOffsetX, hitboxOffsetY;  // offsets to the position of the hitbox
                                                 // from the character object coordinates
    private MovementAnimation movementAnim;
    private State state;
    private boolean flipped;  // true if the character is facing left instead of right
    
    /* ----
     * Used to store the results of hitboxToColumnsAndRows, allowing us to
     * check only those blocks the Character could be intersecting.
     * Storing this info in these fields, rather than returning an object
     * from the relevant method, is simply an optimization allowing us
     * to avoid having to ever construct such an object.
     */
    private int columnBegin, columnEnd, rowBegin, rowEnd;
    // ----
    
    public Character(String characterName)
    {
        this(characterName, 4, 4, 20, 26);
    }

    public Character(String name, float offsetX, float offsetY, float w, float h)
    {
        init(name, offsetX, offsetY, w, h);
    }
    
    public void init(String name, float offsetX, float offsetY, float w, float h)
    {
        characterName = name;
        hitboxOffsetX = offsetX;
        hitboxOffsetY = offsetY;
        hitbox.setX(0);
        hitbox.setY(0);
        hitbox.setW(w);
        hitbox.setH(h);
        movementAnim = null;
        state = State.RUNNING;
        flipped = false;
    }

    public enum State
    {
        STANDING,
        RUNNING,
        JUMPING, 
        FALLING,
        DYING;
    }
    
    /**
     * Defined by subclasses to customize behavior
     * in response to a collision with a block.
     */
    public abstract void respondToCollision(Block b, Rectangle.Collision info);
    
    /**
     * Defined by subclasses to customize behavior in response
     * to not being held up by a block - not standing on anything.
     */
    public abstract void respondToUnsupported();

    @Override
    public void draw(Batch batch, float time)
    {
        switch (state)
        {
            case STANDING: movementAnim.setAction(Action.IDLE); break;
            case RUNNING: movementAnim.setAction(Action.RUN); break;
            case JUMPING: movementAnim.setAction(Action.JUMP); break;
            case FALLING: movementAnim.setAction(Action.LAND); break;
            case DYING: movementAnim.setAction(Action.KNOCKED_BACK); break;
        }
        TextureRegion frame = movementAnim.getFrame(time);

        // if flipped and isFlipX() are different (XOR)
        if ( flipped ^ frame.isFlipX() )
        {
            frame.flip(true, false);
        }
        batch.draw(frame, getXPosition(), getYPosition(),
                frame.getRegionWidth() * Level.UNIT_SCALE,
                frame.getRegionHeight() * Level.UNIT_SCALE);
    }
    
    // called to update the position of the hitbox after a change in position
    private void updateHitbox()
    {
        hitbox.setX( getXPosition() + hitboxOffsetX );
        hitbox.setY( getYPosition() + hitboxOffsetY );
    }

    public void fallingCheck(Block[][] blocks)
    {
        if (state != State.STANDING && state != State.RUNNING)
        {
            return;
        }
        updateHitbox();
        hitboxToColumnsAndRows(blocks.length, blocks[0].length);
        
        doFallingCheck(blocks);
    }

    private void doFallingCheck(Block[][] blocks)
    {
        if (state != State.STANDING && state != State.RUNNING)
        {
            return;
        }
        boolean flag = false;

        for (int x = columnBegin; x <= columnEnd; x++)
        {
            for (int y = rowBegin; y <= rowEnd; y++)
            {
                Block b = blocks[x][y];

                if (b == null) {
                    continue;
                }
                Rectangle.Collision coll
                = hitbox.staticCollisionInfo( b.getRectangle() );

                if (coll != null && coll.getDirection() == Direction.TOP) {
                    flag = true;
                    break;
                }
                /* ---- */
                Rectangle.disposeOf(coll);
            }
        }	
        if (! flag)
        {
            respondToUnsupported();
        }
    }

    public void collisionCheck(Block[][] blocks)
    {
        if (state == State.DYING)
        {
            return;
        }
        updateHitbox();
        hitboxToColumnsAndRows(blocks.length, blocks[0].length);

        boolean flag = false;

        for (int x = columnBegin; x <= columnEnd; x++)
        {
            for (int y = rowBegin; y <= rowEnd; y++)
            {
                Block b = blocks[x][y];

                if (b == null) {
                    continue;
                }
                Rectangle.Collision coll
                = hitbox.collisionInfo(b.getRectangle(),
                        getPreviousX() + hitboxOffsetX,
                        getPreviousY() + hitboxOffsetY);

                if (coll != null
                    && (coll.getDirection() != Direction.TOP || coll.getDistance() != 0.0f)
                    && (coll.getDirection() != Direction.BOTTOM || blocks[x][y + 1] == null))
                     // ^^ this line ensures a bottom collision with a block that has
                     //    a block below it - i.e., part of a vertical wall, is ignored
                {
                    respondToCollision(blocks[x][y], coll);
                    updateHitbox();
                    flag = true;
                }
                /* ---- */
                Rectangle.disposeOf(coll);
            }
        }
        if (flag)
        {
            fallingCheck(blocks);
        }
    }
    
    // Calculates the first and last row, and column, the Character
    // is intersecting, allowing collision checks against only blocks
    // in those cells.
    private void hitboxToColumnsAndRows(int columns, int rows)
    {
        float x1 = hitbox.getX();
        float y1 = hitbox.getY();
        int x2 = MathUtils.ceil(hitbox.getW() + x1);
        int y2 = MathUtils.ceil(hitbox.getH() + y1);

        columnBegin = ((int) x1) / Block.SIZE;
        rowBegin = ((int) y1) / Block.SIZE;
        columnEnd = x2 / Block.SIZE;
        rowEnd = y2 / Block.SIZE;

        columnBegin = MathUtils.clamp(columnBegin, 0, columns - 1);
        columnEnd = MathUtils.clamp(columnEnd, 0, columns - 1);        
        rowBegin = MathUtils.clamp(rowBegin, 0, rows - 1);
        rowEnd = MathUtils.clamp(rowEnd, 0, rows - 1);
    }

    public State getState()
    {
        return state;
    }

    public void setState(State s)
    {
        state = s;
    }

    public boolean isFlipped()
    {
        return flipped;
    }

    public void setFlipped(boolean flipped)
    {
        this.flipped = flipped;
    }

    public final String getCharacterName()
    {
        return characterName;
    }

    public final void setCharacterName(String characterName)
    {
        this.characterName = characterName;
    }

    public Rectangle getRectangle()
    {
        updateHitbox();
        return hitbox;
    }

    public void setRectangle(Rectangle hitbox)
    {
        this.hitbox = hitbox;
    }

    public final float rectOffsetX()
    {
        return hitboxOffsetX;
    }

    public final float rectOffsetY()
    {
        return hitboxOffsetY;
    }

    public final MovementAnimation getMovementAnim()
    {
        return movementAnim;
    }

    public final void setMovementAnim(MovementAnimation movementAnim)
    {
        this.movementAnim = movementAnim;
    }
}
