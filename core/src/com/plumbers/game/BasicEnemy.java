package com.plumbers.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * An enemy character.
 */
public class BasicEnemy extends Character implements Enemy
{
    private Type type;
    private static TextureAtlas atlas = null;
    
    private static final float WALK_FRAME_DURATION = 1/6f;
    
    public BasicEnemy(BasicEnemy.Type type)
    {
        super(type.typeName(), type.offsetX, type.offsetY, type.width, type.height);
        this.type = type;
        setMovementAnim( type.movementAnim() );
        reset();
    }
    
    @Override
    public void reset()
    {
        setState(State.FALLING);
        setYAccel(GameModel.GRAVITY);
        setFlipped(true);
    }
    
    @Override
    public Enemy copy()
    {
        return new BasicEnemy(type);
    }

    @Override
    public void gameTick(int tickNum, Block[][] blockArray)
    {
        simulate(tickNum);
        fallingCheck(blockArray);
        collisionCheck(blockArray);
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
                setXVelocity(- type.speed);
                setFlipped(true);
            }
        }
        else if (info.getDirection() == Direction.RIGHT)
        {
            setXVelocity(type.speed);
            setFlipped(false);
            setXPosition( getXPosition() + info.getDistance() );
        }
        else if (info.getDirection() == Direction.LEFT)
        {
            setXVelocity(- type.speed);
            setFlipped(true);
            setXPosition( getXPosition() - info.getDistance() );
        }
    }

    public static void setTextureAtlas(TextureAtlas atlas)
    {
        BasicEnemy.atlas = atlas;
        
        for (Type type : Type.values())
        {
            type.createMovementAnim();
        }
    }
    
    public enum Type
    {
        BADGUY_1 (EnemyType.RED_GUY, 4, 6, 24, 24, 1.4f),
        BADGUY_2 (EnemyType.BLUE_SPIKE_GUY, 4, 2, 24, 28, 1.1f);

        private EnemyType enemyType;
        private float speed;
        private MovementAnimation animation;
        private float offsetX, offsetY, width, height;

        private Type(EnemyType enemyType, float relativeX, float relativeY,
                     float width, float height, float speed)
        {
            this.enemyType = enemyType;
            this.speed = speed;

            this.offsetX = relativeX;
            this.offsetY = relativeY;
            this.width = width;
            this.height = height;
        }

        public String typeName()
        {
            return enemyType.getName();
        }

        public float speed()
        {
            return speed;
        }
        
        public MovementAnimation movementAnim()
        {
            return animation;
        }
        
        private void createMovementAnim()
        {
            Array<TextureRegion> walkFrames = new Array<TextureRegion>(6);
            
            for (int i = 1; i <= 6; i++)
            {
                TextureRegion region = atlas.findRegion(typeName() + "-walk" + i);

                if ( region != null )
                {
                    walkFrames.add(region);
                }
            }

            Animation walkAnim = new Animation(WALK_FRAME_DURATION, walkFrames);
            Animation jumpAnim = new Animation(1,
                new TextureRegion[]{ atlas.findRegion(typeName() + "-jump") });
            
            // idle anim will never be used, for falling and knockback we reuse jump
            animation = new MovementAnimation(jumpAnim, walkAnim, jumpAnim, jumpAnim, jumpAnim);
        }
    }
}
