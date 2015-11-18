package com.plumbers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Character {

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

		Animation walkAnimation = new Animation(1/8f, walkFrames);
		Animation idleAnimation = new Animation(1/3f, idleFrames);
		Animation jumpAnimation = new Animation( 1, new TextureRegion[]{ textureAtlas.findRegion(name+"-jump") } );
		Animation landAnimation = new Animation( 1, new TextureRegion[]{ textureAtlas.findRegion(name+"-land") } );
		Animation knockbackAnimation = new Animation( 1, new TextureRegion[]{ textureAtlas.findRegion(name+"-knockback") } );
		
		setMovementAnim( new MovementAnimation(idleAnimation, walkAnimation, 83, 5, jumpAnimation, landAnimation, knockbackAnimation) );
	}

	@Override
	public void simulate() {
		if ( getState() == State.STANDING || getState() == State.RUNNING ) {
			if ( Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) ) {
				setState( State.JUMPING );
				setYVelocity(-6.667f);
				setXAccel(0);
				setYAccel(11);
			} else if ( Gdx.input.isKeyPressed(Input.Keys.SPACE) ) {
    			setXAccel(20);
    			setState( State.RUNNING );
    		} else {
    			setXAccel(-20);
    		}
		}
		Vector velocity = getVelocity();
		velocity.add( getAcceleration() );
		setVelocity(velocity);
		
		if (velocity.getX() > 5) {
			setXVelocity(5);
		} else if (velocity.getX() <= 0) {
			setXVelocity(0);
		}
		Vector position = getPosition();
		position.add( getVelocity() );
		setPosition(position);
		
		if ( getState() == State.RUNNING && getVelocity().getX() == 0 ) {
			setState( State.STANDING );
		}
	}

//	@Override
//	public void respondToCollision(Enemy e, Collision info) {
//		// TODO Auto-generated method stub
//		
//	}
}
