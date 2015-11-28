package com.plumbers.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
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

	public enum State {
		STANDING, RUNNING, JUMPING, FALLING, DYING;
	}
	
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
	}
	
	private void updateHitbox() {
		Vector position = getPosition();
		hitbox.setX( /*MathUtils.round(*/position.getX()/*)*/ + hitboxRelPosX );
		hitbox.setY( /*MathUtils.round(*/position.getY()/*)*/ + hitboxRelPosY );
	}
	
	public void ceilingCheck() {
		updateHitbox();
		
		if (hitbox.getY() <= 0) {
			setYAccel(GameModel.GRAVITY);
			setYVelocity(0);
			setYPosition(- hitboxRelPosY);
		}
	}
	
	public void fallingCheck(Iterable<Block> blocks) {
		if (state != State.STANDING && state != State.RUNNING) {
			return;
		}
		updateHitbox();		
		boolean flag = false;
		
		for (Block b : blocks) {
			Rectangle.Collision coll = hitbox.staticCollisionInfo( b.getRectangle() );
			
			if (coll != null && coll.getDirection() == Direction.TOP) {
				flag = true;
				break;
			}
		}	
		if (! flag) { 
			state = State.FALLING;
			setXAccel(0);
			setYAccel(GameModel.GRAVITY);
		}
	}
	
	public void collisionCheck(Iterable<Block> blocks) {
		if (state == State.DYING) {
			return;
		}
		
		updateHitbox();
		boolean flag = false;
		
		for (Block b : blocks) {
			Rectangle.Collision coll = hitbox.collisionInfo( b.getRectangle(), getVelocity() );
			
			if (coll != null) {
				respondToCollision(b, coll);
				updateHitbox();
				flag = true;
			}	
		}
		if (flag) {
			fallingCheck(blocks);
		}
	}
	
	public void respondToCollision(Block b, Rectangle.Collision info) {
		if (info.getDirection() == Direction.TOP) {
			setYAccel(0);
			setYVelocity(0);
			setYPosition( getPosition().getY() - info.getDistance() );
			
			if (state == State.JUMPING || state == State.FALLING) {
				state = State.RUNNING;
				
			}
		} else if (info.getDirection() == Direction.LEFT) {
			setXAccel(0);
			setXVelocity(0);
			setXPosition( getPosition().getX() - info.getDistance() );
			
			if (state == State.JUMPING && getVelocity().getY() > 0) {
				state = State.FALLING;
			}
		} else if (info.getDirection() == Direction.BOTTOM) {
			setYVelocity(0);
			setYPosition( getPosition().getY() + info.getDistance() );
			
		} else if (info.getDirection() == Direction.RIGHT) {
			setXAccel(0);
			setXVelocity(0);
			setXPosition( getPosition().getX() + info.getDistance() );
		}
	}
	
	public State getState() {
		return state;
	}

	public void setState(State s) {
		state = s;
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

	public MovementAnimation getMovementAnim() {
		return movementAnim;
	}

	public void setMovementAnim(MovementAnimation movementAnim) {
		this.movementAnim = movementAnim;
	}
}
