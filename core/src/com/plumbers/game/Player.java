package com.plumbers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Character {
	private int coinsCollected = 0;

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

	@Override
	public void simulate() {
		if ( getState() == State.STANDING || getState() == State.RUNNING ) {
			if ( Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) ) {
				setState( State.JUMPING );
				setYVelocity(-10);
				setXAccel(0);
				setYAccel(GameModel.GRAVITY);
			} else if ( Gdx.input.isKeyPressed(Input.Keys.SPACE) ) {
    			setXAccel(0.25f);
    			setState( State.RUNNING );
    		} else {
    			setXAccel(-0.75f);
    		}
		}
		if ( getState() == State.JUMPING && Gdx.input.isKeyPressed(Input.Keys.SPACE) ) {
			setXPosition( getPosition().getX() + 1 );
		}
		Vector velocity = getVelocity();
		velocity.add( getAcceleration() );
		setVelocity(velocity);
		
		if (velocity.getX() > 5) {
			setXVelocity(5);
		} else if (velocity.getX() < 0) {
			setXVelocity(0);
		}
		Vector position = getPosition();
		position.add( getVelocity() );
		setPosition(position);
		
		if ( getState() == State.RUNNING && getVelocity().getX() == 0 ) {
			setState( State.STANDING );
		}
	}
	
	public void coinCollectCheck(Iterable<Coin> coins) {
		Rectangle rect = getRectangle();
		
		for (Coin coin : coins) {
			if (! coin.isCollected() && rect.intersects(coin.getRectangle())) {
				coin.setCollected(true);
				++coinsCollected;
			}
		}
	}
	
	public boolean fallingDeathCheck(float bottom) {
		return (getPosition().getY() > bottom);
	}

}
