package com.plumbers.game;

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
		movementAnim.setHorizontalSpeed( getVelocity().getX() );
		
		TextureRegion frame = movementAnim.getFrame(time);
		Vector position = getPosition();
		batch.draw(frame, position.getX(), position.getY(),
				frame.getRegionWidth() * 2, frame.getRegionHeight() * 2);
	}
	
	@Override
	public void simulate() {
		super.simulate();

		if (state == State.RUNNING && getVelocity().getX() <= 0) {
			state = Character.State.STANDING;
			setXVelocity(0);
		}
	}
	
	public void fallingCheck(Iterable<Block> blocks) {
		
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

	public abstract void respondToCollision(Enemy e, Rectangle.Collision info);
	
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
