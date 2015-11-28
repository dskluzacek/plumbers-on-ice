package com.plumbers.game;

import java.util.HashMap;
import java.util.Map;

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
	
	public Enemy(Type type, float x, float y) {
		super( type.typeName() );
		setPosition(x, y);
	}

	@Override
	public void respondToCollision(Block b, Rectangle.Collision s) {
		
	}
	
	public enum Type {
		BADGUY_1 ("badguy1", 0, 0, 10, 10),
		BADGUY_2 ("badguy2", 0, 0, 10, 10);

		private String string;
		private static Map<String, Type> map;
		protected float relativeX, relativeY, width, height;
		
		private Type(String typeName, float relativeX, float relativeY,
				     float width, float height)
		{
			this.string = typeName;
			this.relativeX = relativeX;
			this.relativeY = relativeY;
			this.width = width;
			this.height = height;
		}

		public String typeName() {
			return string;
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
