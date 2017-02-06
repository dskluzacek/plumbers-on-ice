package com.plumbers.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * An enemy character.
 */
public class Enemy extends Character implements Hazard
{
    private final float walkSpeed;
    private static TextureAtlas atlas = null;

    public Enemy(Type type, float x, float y)
    {
        super(type.typeName(), type.offsetX, type.offsetY, type.width, type.height);
        walkSpeed = type.speed;

        Array<TextureRegion> walkFrames = new Array<TextureRegion>();
        
        for (int i = 1; i <= 6; i++)
        {
            TextureRegion region = atlas.findRegion(type.typeName() + "-walk" + i);

            if ( region != null )
            {
                walkFrames.add(region);
            }
        }

        Animation walkAnim = new Animation(1/6f, walkFrames);
        Animation jumpAnim = new Animation(1,
            new TextureRegion[]{ atlas.findRegion(type.typeName() + "-jump") });
        
        // idle anim will never be used, for falling and knockback we reuse jump
        setMovementAnim(
            new MovementAnimation(jumpAnim, walkAnim, jumpAnim, jumpAnim, jumpAnim) );

        setState(State.FALLING);
        setPosition(x, y);
        setYAccel(GameModel.GRAVITY);
        setFlipped(true);
    }

    public static void setTextureAtlas(TextureAtlas atlas)
    {
        Enemy.atlas = atlas;
    }
    
    @Override
    public void fallingCheck(Block[][] blocks)
    {
        // causes the enemy to turn around 
        // when it is one enemy-width away from falling
        
        float x = getXPosition();
        setXPosition( x + Math.copySign(getRectangle().getW(), getXVelocity()) );
        super.fallingCheck(blocks);
        setXPosition(x);
    }
    
    // with the override of fallingCheck, will be called when one width from falling
    @Override
    public void respondToUnsupported()
    {
        float Vx = getXVelocity();
        
        // turn around
        setXVelocity( -Vx );
        setXPosition( getXPosition() - Vx );
        setFlipped( (Vx > 0) );
    }

    @Override
    public void respondToCollision(Block block, Rectangle.Collision info)
    {	    
        if (info.getDirection() == Direction.TOP)
        {
            setYAccel(0);
            setYVelocity(0);
            setYPosition( block.getRectangle().getY() - getRectangle().getH() - rectOffsetY() );

            if ( getState() != State.RUNNING )
            {
                setState(State.RUNNING);
                setXVelocity(- walkSpeed);
                setFlipped(true);
            }
        }
        else if (info.getDirection() == Direction.RIGHT)
        {
            setXVelocity(walkSpeed);
            setFlipped(false);
            setXPosition( getXPosition() + info.getDistance() );
        }
        else if (info.getDirection() == Direction.LEFT)
        {
            setXVelocity(- walkSpeed);
            setFlipped(true);
            setXPosition( getXPosition() - info.getDistance() );
        }
    }

    public enum Type
    {
        BADGUY_1 ("badguy1", 4, 6, 24, 24, 1.4f),
        BADGUY_2 ("badguy2", 4, 2, 24, 28, 1.1f);

        private String string;
        private float speed;
        protected float offsetX, offsetY, width, height;
        
        private static Map<String, Type> map;

        private Type(String typeName, float relativeX, float relativeY,
                     float width, float height, float speed)
        {
            this.string = typeName;
            this.speed = speed;

            this.offsetX = relativeX;
            this.offsetY = relativeY;
            this.width = width;
            this.height = height;
        }

        public String typeName()
        {
            return string;
        }

        public float speed()
        {
            return speed;
        }

        public static Type getByName(String typeName)
        {
            return map.get(typeName);
        }

        static
        {
            map = new HashMap<String, Type>();

            for (Type type : Type.values())
            {
                map.put(type.string, type);
            }
        }
    }

}
