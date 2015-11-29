package com.plumbers.game;

import java.util.List;
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
		Vector position = getPosition();
		
		// if flipped and isFlipX() are different (XOR)
		if ( flipped ^ frame.isFlipX() ) {
			frame.flip(true, false);
		}
		
		batch.draw(frame, MathUtils.floor( position.getX() ), position.getY(),
				frame.getRegionWidth() * 2, frame.getRegionHeight() * 2);
		
		/* ---- */
		Pools.free(position);
	}
	
	private void updateHitbox() {
		Vector position = getPosition();
		hitbox.setX( /*MathUtils.round(*/position.getX()/*)*/ + hitboxRelPosX );
		hitbox.setY( /*MathUtils.round(*/position.getY()/*)*/ + hitboxRelPosY );
		
		/* ---- */
		Pools.free(position);
	}
	
	public void fallingCheck(List<Block> blocks) {
		if (state != State.STANDING && state != State.RUNNING) {
			return;
		}
		updateHitbox();		
		boolean flag = false;
		
		for (int i = 0; i < blocks.size(); i++) {
			Rectangle.Collision coll
			    = hitbox.staticCollisionInfo( blocks.get(i).getRectangle() );
			
			if (coll != null && coll.getDirection() == Direction.TOP) {
				flag = true;
				break;
			}
			/* ---- */
            Rectangle.disposeOf(coll);
		}	
		if (! flag) {
			respondToUnsupported();
		}
	}
	
	public void collisionCheck(List<Block> blocks) {
		if (state == State.DYING) {
			return;
		}
		
		updateHitbox();
		Vector velocity = getVelocity();
		boolean flag = false;
		
		for (int i = 0; i < blocks.size(); i++) {
			Rectangle.Collision coll
			   = hitbox.collisionInfo(blocks.get(i).getRectangle(), velocity);
			
			if (coll != null) {
				respondToCollision(blocks.get(i), coll);
				updateHitbox();
				flag = true;
			}
			/* ---- */
            Rectangle.disposeOf(coll);
		}
		if (flag) {
			fallingCheck(blocks);
		}
		
		/* ---- */
		Pools.free(velocity);
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
