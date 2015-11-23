package com.plumbers.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy extends Character {
	
	public Enemy(String name, TextureAtlas textureAtlas) {
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
		
		setMovementAnim( new MovementAnimation(idleAnimation, walkAnimation, jumpAnimation, landAnimation, knockbackAnimation) );
	}
	
	@Override
	public void respondToCollision(Block b, Rectangle.Collision s) {
		
	}
	
}
