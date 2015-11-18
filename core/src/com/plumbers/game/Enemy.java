package com.plumbers.game;
public class Enemy extends Character {
	
	public Enemy(String name) {
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

		Animation walkAnimation = new Animation(1/8f, walkFrames);
		Animation idleAnimation = new Animation(1/3f, idleFrames);
		Animation jumpAnimation = new Animation( 1, new TextureRegion[]{ textureAtlas.findRegion(name+"-jump") } );
		Animation knockbackAnimation = new Animation( 1, new TextureRegion[]{ textureAtlas.findRegion(name+"-knockback") } );
		
		this.movementAnim = new MovementAnimation(idleAnimation, walkAnimation, 5000, 300, jumpAnimation, null, null);
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
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
	
	public void respondToCollision(Block b, Side s) {
		
	}
	
	public void respondToCollision(Player p, Side s) {
		
	}
}
