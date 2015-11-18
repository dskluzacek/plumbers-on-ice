package com.plumbers.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.plumbers.game.Rectangle.Collision;

public class Player extends Character {

	public Player(String name) {
		super(name);
		textureAtlas =	new TextureAtlas(Gdx.files.internal(name+".atlas"), true);

		TextureRegion[] walkFrames = new TextureRegion[] {
				textureAtlas.findRegion(name+"-walk1"),
				textureAtlas.findRegion(name+"-walk2"),
				textureAtlas.findRegion(name+"-walk3") };
		
		TextureRegion[] idleFrames = new TextureRegion[] {
				textureAtlas.findRegion(name+"-idle1"),
				textureAtlas.findRegion(name+"-idle2"),
				textureAtlas.findRegion(name+"-idle3") };

		walkAnimation = new Animation(1/8f, walkFrames);
		idleAnimation = new Animation(1/3f, idleFrames);
		jumpAnimation = new Animation( 1, new TextureRegion[]{ textureAtlas.findRegion(name+"-jump") } );
		knockbackAnimation = new Animation( 1, new TextureRegion[]{ textureAtlas.findRegion(name+"-knockback") } );
		
		this.movementAnim = new MovementAnimation(idleAnimation, walkAnimation, 5000, 300, jumpAnimation, null, null);
	}

	

	@Override
	public void respondToCollision(Enemy e, Collision info) {
		// TODO Auto-generated method stub
		
	}
}
