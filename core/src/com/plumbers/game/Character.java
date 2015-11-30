package com.plumbers.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pools;
import com.plumbers.game.MovementAnimation.Action;

public abstract class Character extends Motionable implements Drawable {
	private String characterName;
	private Rectangle hitbox;
	private float hitboxRelPosX, hitboxRelPosY;
	private MovementAnimation movementAnim;
	private State state = State.RUNNING;
	private boolean flipped = false;
	
	/* ---- */
	private int columnBegin, columnEnd, rowBegin, rowEnd;
	/* ---- */

	public Character(String characterName) {
		this.characterName = characterName;
		hitbox = new Rectangle(0, 0, 20, 26);
		hitboxRelPosX = 4;
		hitboxRelPosY = 4;
	}
	
	public Character(String name, float relX, float relY, float w, float h) {
		characterName = name;
		hitbox = new Rectangle(0, 0, w, h);
		hitboxRelPosX = relX;
		hitboxRelPosY = relY;
	}

	public enum State {
		STANDING, RUNNING, JUMPING, FALLING, DYING;
	}
	
	public abstract void respondToCollision(Block b, Rectangle.Collision info);
	public abstract void respondToUnsupported();
	
	@Override
	public void draw(Batch batch, float time) {
		switch (state) {
			case STANDING: movementAnim.setAction(Action.IDLE); break;
			case RUNNING: movementAnim.setAction(Action.RUN); break;
			case JUMPING: movementAnim.setAction(Action.JUMP); break;
			case FALLING: movementAnim.setAction(Action.LAND); break;
			case DYING: movementAnim.setAction(Action.KNOCKED_BACK); break;
		}
		
		TextureRegion frame = movementAnim.getFrame(time);
		
		// if flipped and isFlipX() are different (XOR)
		if ( flipped ^ frame.isFlipX() ) {
			frame.flip(true, false);
		}
		
		batch.draw(frame, MathUtils.floor( getXPosition() ), getYPosition(),
				frame.getRegionWidth() * 2, frame.getRegionHeight() * 2);
	}
	
	private void updateHitbox() {
		hitbox.setX( getXPosition() + hitboxRelPosX );
		hitbox.setY( getYPosition() + hitboxRelPosY );
	}
	
	public void fallingCheck(Block[][] blocks) {
	    if (state != State.STANDING && state != State.RUNNING) {
            return;
        }
	    updateHitbox();
        hitboxToColumnsAndRows();
        columnEnd = MathUtils.clamp(columnEnd, 0, blocks.length - 1);
        rowEnd = MathUtils.clamp(rowEnd, 0, blocks[0].length - 1);
        
        doFallingCheck(blocks);
	}
	
	private void doFallingCheck(Block[][] blocks) {
		if (state != State.STANDING && state != State.RUNNING) {
			return;
		}
		boolean flag = false;
		
		for (int x = columnBegin; x <= columnEnd; x++) {
            for (int y = rowBegin; y <= rowEnd; y++) {
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
		if (! flag) {
			respondToUnsupported();
		}
	}
	
	public void collisionCheck(Block[][] blocks) {
		if (state == State.DYING) {
			return;
		}
		updateHitbox();
		hitboxToColumnsAndRows();
		columnEnd = MathUtils.clamp(columnEnd, 0, blocks.length - 1);
        rowEnd = MathUtils.clamp(rowEnd, 0, blocks[0].length - 1);
        
		Vector velocity = getVelocity();
		boolean flag = false;
		
		for (int x = columnBegin; x <= columnEnd; x++) {
            for (int y = rowBegin; y <= rowEnd; y++) {
                Block b = blocks[x][y];
                
                if (b == null) {
                    continue;
                }
    			Rectangle.Collision coll
    			   = hitbox.collisionInfo(b.getRectangle(), velocity);
    			
    			if (coll != null && (coll.getDirection() != Direction.TOP
    			                     || coll.getDistance() != 0.0f) )
    			{
    				respondToCollision(blocks[x][y], coll);
    				updateHitbox();
    				flag = true;
    			}
			/* ---- */
            Rectangle.disposeOf(coll);
            }
		}
		if (flag) {
			fallingCheck(blocks);
		}
		/* ---- */
		Pools.free(velocity);
	}
	
	private void hitboxToColumnsAndRows() {
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
	
	public State getState() {
		return state;
	}

	public void setState(State s) {
		state = s;
	}
	
	public boolean isFlipped() {
		return flipped;
	}
	
	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}

	public final String getCharacterName() {
		return characterName;
	}

	public final void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public Rectangle getRectangle() {
		updateHitbox();
		return hitbox;
	}

	public void setRectangle(Rectangle hitbox) {
		this.hitbox = hitbox;
	}
	
	public float rectRelPosX() {
		return hitboxRelPosX;
	}
	
	public float rectRelPosY() {
		return hitboxRelPosY;
	}	
	
	public MovementAnimation getMovementAnim() {
		return movementAnim;
	}

	public void setMovementAnim(MovementAnimation movementAnim) {
		this.movementAnim = movementAnim;
	}
}
