package com.plumbers.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.plumbers.game.MovementAnimation.Action;

public abstract class Character extends Motionable implements Drawable {
	private String characterName;
	private Rectangle hitbox;
	private MovementAnimation movementAnim;
	private State state = State.STANDING;

	public Character(String characterName) {
		this.characterName = characterName;
	}

	public enum State {
		STANDING, RUNNING, JUMPING, FALLING, DYING;
	}
	
	@Override
	public void draw(Batch batch, float time) {
		switch (state) {
			case STANDING: movementAnim.setAction(Action.IDLE);
			case RUNNING: movementAnim.setAction(Action.RUN);
			case JUMPING: movementAnim.setAction(Action.JUMP);
			case FALLING: movementAnim.setAction(Action.LAND);
			case DYING: movementAnim.setAction(Action.KNOCKED_BACK);
		}
		
		TextureRegion frame = movementAnim.getFrame(time);
		Vector position = getPosition();
		batch.draw(frame, position.getX(), position.getY(),
				frame.getRegionWidth() * 2, frame.getRegionHeight() * 2);
	}
	
	private void updateHitbox() {
		Vector position = getPosition();
		hitbox.setX( Math.rposition.getX() );
	}
	
	public void fallingCheck(Iterable<Block> blocks) {
		if (state != State.STANDING && state != State.RUNNING)
			return;
		
		
		
		Rectangle box = new Rectangle( hitbox.getX(),
		                               hitbox.getY(),
		                               hitbox.getW(),
		                               hitbox.getH() - 1);
		
		for (Block b : blocks) {
			Rectangle.Collision coll = box.collisionInfo( b.getRectangle() );
			
			if (coll == null || coll.getDirection() != Direction.TOP) {
				state = State.FALLING;
				setYAccel(640);
			}
		}
	}
	
	public void collisionCheck(Iterable<Block> blocks) {
		List<Rectangle.Collision> list = new ArrayList<Rectangle.Collision>();
		
		for (Block b : blocks) {
			Rectangle.Collision coll = hitbox.collisionInfo( b.getRectangle() );
			
			if (coll != null) {
				list.add(coll);
			}	
		}
		
		for (Rectangle.Collision info : list) {
			respondToCollision(null, info);
		}
	}
	
	public void respondToCollision(Block b, Rectangle.Collision info) {
		if (info.getDirection() == Direction.TOP) {
			setYAccel(0);
			setYVelocity(0);
			setYPosition( getPosition().getY() + info.getDistance() );
			
			if ( getVelocity().getY() == 0 ) {
				state = State.STANDING;
			} else {
				state = State.RUNNING;
			}
		} else if (info.getDirection() == Direction.LEFT) {
			setXAccel(0);
			setXVelocity(0);
			setXPosition( getPosition().getX() + info.getDistance() );
			
			if (state == State.JUMPING) {
				state = State.FALLING;
			}
		}
	}
	
	public State getState() {
		return state;
	}

	public void setState(State s) {
		state = s;
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
