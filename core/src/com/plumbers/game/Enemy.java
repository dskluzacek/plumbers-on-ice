package com.plumbers.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public class Enemy extends Character implements Hazard {
	private final float walkSpeed;
	private static TextureAtlas atlas = null;
	
	public Enemy(Type type, float x, float y) {
		super(type.typeName(), type.relativeX, type.relativeY, type.width, type.height);
		walkSpeed = type.speed;
		
		Array<TextureRegion> walkFrames = new Array<TextureRegion>();

		for (int i = 0; i < 6; i++) {
			TextureRegion region = atlas.findRegion(type.typeName() + "-walk" + i);
			
			if ( region != null ) {
				walkFrames.add(region);
			}
		}

		Animation walkAnim = new Animation(1/4f, walkFrames);
		Animation jumpAnim = new Animation(1,
		  new TextureRegion[]{ atlas.findRegion(type.typeName() + "-jump") });
		
		setMovementAnim(
		  new MovementAnimation(null, walkAnim, jumpAnim, jumpAnim, null) );
		
		setState(State.FALLING);
		setPosition(x, y);
		setYAccel(GameModel.GRAVITY);
		setFlipped(true);
	}
	
	public static void setTextureAtlas(TextureAtlas atlas) {
		Enemy.atlas = atlas;
	}
    
    @Override
    public void fallingCheck(Iterable<Block> blocks) {
    	Vector position = getPosition();
    	Vector velocity = getVelocity();
    	
    	setXPosition(
    		position.getX()
    		+ Math.copySign( getRectangle().getW(), velocity.getX() )
    	);
    	super.fallingCheck(blocks);
    	
    	setXPosition( position.getX() );
    	
    	/* ---- */
        Pools.free(position);
        Pools.free(velocity);
    }
	
	@Override
	public void respondToUnsupported() {
		Vector v = getVelocity();
		Vector pos = getPosition();
	    float Vx = v.getX();
		
		setXVelocity( -Vx );
		setXPosition( pos.getX() - Vx );
		setFlipped( (Vx > 0) );
		
		/* ---- */
        Pools.free(v);
        Pools.free(pos);
	}

	@Override
	public void respondToCollision(Block block, Rectangle.Collision info) {
		Vector position = getPosition();
	    
	    if (info.getDirection() == Direction.TOP)
		{
			Rectangle rect = block.getRectangle();
	        
	        setYAccel(0);
			setYVelocity(0);
			setYPosition( rect.getY() - getRectangle().getH() - rectRelPosY() );
			
			if ( getState() != State.RUNNING ) {
    			setState(State.RUNNING);
    			setXVelocity(- walkSpeed);
    			setFlipped(true);
			}
			/* ---- */
	        Pools.free(rect);
		}
		else if (info.getDirection() == Direction.RIGHT)
		{
			setXVelocity(walkSpeed);
			setFlipped(false);
			setXPosition( position.getX() + info.getDistance() );
		}
		else if (info.getDirection() == Direction.LEFT)
		{
			setXVelocity(- walkSpeed);
			setFlipped(true);
			setXPosition( position.getX() - info.getDistance() );
		}
	    
	    /* ---- */
        Pools.free(position);
	}
	
	public enum Type {
		BADGUY_1 ("badguy1", 4, 6, 24, 24, 0.7f),
		BADGUY_2 ("badguy2", 4, 2, 24, 28, 0.55f);

		private String string;
		private float speed;
		private static Map<String, Type> map;
		protected float relativeX, relativeY, width, height;
		
		private Type(String typeName, float relativeX, float relativeY,
				     float width, float height, float speed)
		{
			this.string = typeName;
			this.speed = speed;
			
			this.relativeX = relativeX;
			this.relativeY = relativeY;
			this.width = width;
			this.height = height;
		}

		public String typeName() {
			return string;
		}
		
		public float speed() {
			return speed;
		}

		public static Type get(String typeName) {
			return map.get(typeName);
		}

		static {
			map = new HashMap<String, Type>();
			
			for (Type type : Type.values()) {
				map.put(type.string, type);
			}
		}
	}
	
}
