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
	private boolean hurt = false;
	private int jumpStarted;
	
	private static final float ACCELERATION = 1/14f,
	                           DECELERATION = -0.3f,
	                           MAX_SPEED = 2.8f,
	                           JUMP_POWER = -2.75f,
	                       	   JUMP_BOOST = -1/20f,
	                           JUMP_FWD_ASSIST = 0.5f;
	private static final int JUMP_BOOST_DURATION = 24;
	
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
	public void preVelocityLogic(int tickNumber) {
		if ( getState() == State.DYING ) {
			return;
		}
		
		if ( getState() == State.JUMPING ) {
			if ( tickNumber <= jumpStarted + JUMP_BOOST_DURATION
			      && Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) ) {
				// allow jump acceleration to continue
			} else {
				setYAccel(GameModel.GRAVITY);
			}
			
			if ( Gdx.input.isKeyPressed(Input.Keys.SPACE) ) {
				setXPosition( getPosition().getX() + JUMP_FWD_ASSIST );
			}
		}
		
		if ( getState() == State.STANDING || getState() == State.RUNNING ) {
			if ( Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) ) {
				jumped = true;
				jumpStarted = tickNumber;
				setState( State.JUMPING );
				setYVelocity(JUMP_POWER);
				setYAccel(JUMP_BOOST);
				setXAccel(0);
			} else if ( Gdx.input.isKeyPressed(Input.Keys.SPACE) ) {
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
		
		float Vx = getVelocity().getX();
		
		if (Vx > MAX_SPEED) {
			setXVelocity(MAX_SPEED);
		} else if (Vx < 0) {
			setXVelocity(0);
		}
	}
	
	@Override
	public void postMotionLogic(int tickNumber) {
		if ( getState() == State.RUNNING && getVelocity().getX() == 0 ) {
			setState( State.STANDING );
		}
		if ( getState() != State.DYING && Gdx.input.isKeyPressed(Input.Keys.K)) {
			beKilled();
		}
	}
	
	public List<Event> getEvents() {
		boolean jumped = this.jumped;
		boolean hurt = this.hurt;
		this.jumped = false;
		this.hurt = false;
		
		if (hurt) {
			return Collections.singletonList((Event) DamageEvent.instance());
		} else if (jumped && getState() == State.JUMPING ) {
			return Collections.singletonList((Event) JumpEvent.instance());
		} else {
			return Collections.emptyList();
		}
	}
	
	public void beKilled() {
		setState( State.DYING );
		setAcceleration(0, GameModel.GRAVITY);
		setVelocity(-0.75f, -4.0f);
		hurt = true;
	}
	
	public void reset(Vector position) {
		coinsCollected = 0;
		setPosition(position);
		setVelocity(0, 0);
		setAcceleration(0, GameModel.GRAVITY);
		setState(Character.State.FALLING);
	}
	
	public Event hazardCollisionCheck(Iterable<Hazard> hazards) {
		if ( getState() == State.DYING ) {
			return null;
		}
		Rectangle rect = getRectangle();
		
		for (Hazard h : hazards) {
			if ( rect.intersects(h.getRectangle()) ) {
				beKilled();
				return DamageEvent.instance();
			}
		}
		return null;
	}
	
	public List<Event> coinCollectCheck(Iterable<Coin> coins) {
		if ( getState() == State.DYING ) {
			return Collections.emptyList();
		}
		
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
