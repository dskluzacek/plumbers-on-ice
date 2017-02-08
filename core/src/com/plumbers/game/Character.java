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
    private String characterName;  // corresponds to names of texture regions
    private Rectangle hitbox;
    private float hitboxOffsetX, hitboxOffsetY;  // offsets to the position of the hitbox
                                                 // from the character object coordinates
    private MovementAnimation movementAnim;
    private State state = State.RUNNING;
    private boolean flipped = false;  // true if the character is facing left instead of right
    
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
        this.characterName = characterName;
        hitbox = new Rectangle(0, 0, 20, 26);
        hitboxOffsetX = 4;
        hitboxOffsetY = 4;
    }

    public Character(String name, float offsetX, float offsetY, float w, float h)
    {
        characterName = name;
        hitbox = new Rectangle(0, 0, w, h);
        hitboxOffsetX = offsetX;
        hitboxOffsetY = offsetY;
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
                frame.getRegionWidth() * 2, frame.getRegionHeight() * 2);
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
        hitboxToColumnsAndRows();
        columnEnd = MathUtils.clamp(columnEnd, 0, blocks.length - 1);
        rowEnd = MathUtils.clamp(rowEnd, 0, blocks[0].length - 1);

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
        hitboxToColumnsAndRows();
        columnEnd = MathUtils.clamp(columnEnd, 0, blocks.length - 1);
        rowEnd = MathUtils.clamp(rowEnd, 0, blocks[0].length - 1);

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
    private void hitboxToColumnsAndRows()
    {
        float x1 = hitbox.getX();
        float y1 = hitbox.getY();
        int x2 = MathUtils.ceil(hitbox.getW() + x1);
        int y2 = MathUtils.ceil(hitbox.getH() + y1);

        columnBegin = ((int) x1) / 32;
        rowBegin = ((int) y1) / 32;
        columnEnd = x2 / 32;
        rowEnd = y2 / 32;

        if (this.columnBegin < 0) {
            this.columnBegin = 0;
        }
        if (this.rowBegin < 0) {
            this.rowBegin = 0;
        }
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

    public float rectOffsetX()
    {
        return hitboxOffsetX;
    }

    public float rectOffsetY()
    {
        return hitboxOffsetY;
    }

    public MovementAnimation getMovementAnim()
    {
        return movementAnim;
    }

    public void setMovementAnim(MovementAnimation movementAnim)
    {
        this.movementAnim = movementAnim;
    }
}
