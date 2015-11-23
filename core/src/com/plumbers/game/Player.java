package com.plumbers.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Character {
	private int coinsCollected = 0;
	private boolean jumped = false;
	
	private static final float ACCELERATION = 0.25f,
	                           DECELERATION = -0.75f,
	                       	   JUMP_POWER = -10,
	                           JUMP_ASSIST = 1;
	
	public Player(String name, TextureAtlas textureAtlas) {
		super(name);

		TextureRegion[] walkFrames = new TextureRegion[] {
				textureAtlas.findRegion(name+"-walk1"),
				textureAtlas.findRegion(name+"-walk2"),
				textureAtlas.findRegion(name+"-walk3") };
		
		TextureRegion[] idleFrames = new TextureRegion[] {
				textureAtlas.findRegion(name+"-idle1"),
				textureAtlas.findRegion(name+"-idle2"),
				textureAtlas.findRegion(name+"-idle3") };

		Animation walkAnimation = new Animation(1/12f, walkFrames);
		Animation idleAnimation = new Animation(1/3f, idleFrames);
		Animation jumpAnimation = new Animation( 1, new TextureRegion[]{ textureAtlas.findRegion(name+"-jump") } );
		Animation landAnimation = new Animation( 1, new TextureRegion[]{ textureAtlas.findRegion(name+"-land") } );
		Animation knockbackAnimation = new Animation( 1, new TextureRegion[]{ textureAtlas.findRegion(name+"-knockback") } );
		
		setMovementAnim( new MovementAnimation(idleAnimation, walkAnimation, jumpAnimation, landAnimation, knockbackAnimation) );
	}
	
	public int getCoinsCollected() {
		return coinsCollected;
	}
	
	@Override
	public void preVelocityLogic() {
		
		if ( getState() == State.STANDING || getState() == State.RUNNING ) {
			if ( Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) ) {
				jumped = true;
				setState( State.JUMPING );
				setYVelocity(JUMP_POWER);
				setXAccel(0);
				setYAccel(GameModel.GRAVITY);
			} else if ( Gdx.input.isKeyPressed(Input.Keys.SPACE) ) {
    			setXAccel(ACCELERATION);
    			setState( State.RUNNING );
    		} else {
    			setXAccel(DECELERATION);
    		}
		}
		if ( getState() == State.JUMPING && Gdx.input.isKeyPressed(Input.Keys.SPACE) ) {
			setXPosition( getPosition().getX() + JUMP_ASSIST );
		}
	}
	
	@Override
	public void prePositionLogic() {
		float Vx = getVelocity().getX();
		
		if (Vx > 5) {
			setXVelocity(5);
		} else if (Vx < 0) {
			setXVelocity(0);
		}
	}
	
	@Override
	public void postMotionLogic() {
		if ( getState() == State.RUNNING && getVelocity().getX() == 0 ) {
			setState( State.STANDING );
		}
	}
	
	public List<Event> getEvents() {
		boolean jumped = this.jumped;
		this.jumped = false;
		
		if (jumped) {
			return Collections.singletonList((Event) JumpEvent.instance());
		} else {
			return Collections.emptyList();
		}
	}
	
	public void reset(Vector position) {
		coinsCollected = 0;
		setPosition(position);
		setVelocity(0, 0);
		setAcceleration(0, GameModel.GRAVITY);
		setState(Character.State.FALLING);
	}
	
	public List<Event> coinCollectCheck(Iterable<Coin> coins) {
		Rectangle rect = getRectangle();
		List<Event> events = new ArrayList<Event>();
		
		for (Coin coin : coins) {
			if (! coin.isCollected() && rect.intersects(coin.getRectangle())) {
				coin.setCollected(true);
				++coinsCollected;
				events.add( CoinEvent.instance() );
			}
		}
		return events;
	}
	
	public boolean fallingDeathCheck(float bottom) {
		return (getPosition().getY() > bottom);
	}

}
