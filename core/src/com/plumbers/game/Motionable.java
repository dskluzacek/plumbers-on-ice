package com.plumbers.game;

/**
 * An object that takes part in the game's simulation of physical motion.
 * Has a position, velocity, and acceleration.
 */
public abstract class Motionable
{
    private final Vector position = new Vector(0, 0);
    private final Vector velocity = new Vector(0, 0);
    private final Vector acceleration = new Vector(0, 0);

    private final Vector velocityModifier = new Vector(0, 0);
    private final Vector effectiveVelocity = new Vector(0, 0);

    private final Vector previousPosition = new Vector(0, 0);

    public final float getXPosition()
    {
        return position.getX();
    }

    public final float getYPosition()
    {
        return position.getY();
    }
    
    /** 
     * Sets this object's position to the value of the given vector.
     */
    public final void setPosition(Vector position)
    {
        this.position.setX( position.getX() );
        this.position.setY( position.getY() );
    }
    
    public final void setPosition(float x, float y)
    {
        this.position.setX(x);
        this.position.setY(y);
    }

    public final float getXVelocity()
    {
        return velocity.getX();
    }

    public final float getYVelocity()
    {
        return velocity.getY();
    }
    
    /** 
     * Sets this object's velocity to the value of the given vector.
     */
    public final void setVelocity(Vector velocity)
    {
        this.velocity.setX( velocity.getX() );
        this.velocity.setY( velocity.getY() );
    }

    public final void setVelocity(float x, float y)
    {
        this.velocity.setX(x);
        this.velocity.setY(y);
    }

    public final float getXAcceleration()
    {
        return velocity.getX();
    }

    public final float getYAcceleration()
    {
        return acceleration.getY();
    }
    
    /** 
     * Sets this object's acceleration to the value of the given vector.
     */
    public final void setAcceleration(Vector acceleration)
    {
        this.acceleration.setX( acceleration.getX() );
        this.acceleration.setY( acceleration.getY() );
    }

    public final void setAcceleration(float x, float y)
    {
        this.acceleration.setX(x);
        this.acceleration.setY(y);
    }

    public final void setXPosition(float value)
    {
        position.setX(value);
    }

    public final void setYPosition(float value)
    {
        position.setY(value);
    }

    public final void setXVelocity(float value)
    {
        velocity.setX(value);
    }

    public final void setYVelocity(float value)
    {
        velocity.setY(value);
    }

    public final void setXAccel(float value)
    {
        acceleration.setX(value);
    }

    public final void setYAccel(float value)
    {
        acceleration.setY(value);
    }
    
    /**
     * Adds the given amount to the velocity modifier in the X direction.
     */
    public final void addXVelocityModifier(float amount)
    {
        velocityModifier.setX( velocityModifier.getX() + amount );
    }

    /**
     * Adds the given amount to the velocity modifier in the Y direction.
     */
    public final void addYVelocityModifier(float amount)
    {
        velocityModifier.setY( velocityModifier.getY() + amount );
    }
    
    /**
     * Gets the X component of the effective velocity from the last call to simulate()
     */
    public float getEffectiveXVelocity()
    {
        return effectiveVelocity.getX();
    }
    
    /**
     * Gets the Y component of the effective velocity from the last call to simulate()
     */
    public float getEffectiveYVelocity()
    {
        return effectiveVelocity.getY();
    }

    public float getPreviousX()
    {
        return previousPosition.getX();
    }

    public float getPreviousY()
    {
        return previousPosition.getY();
    }
    
    /** 
     * Subclasses may override this to do custom logic
     * just before the motion calculations are performed.
     */
    public void preVelocityLogic(int tick) {}
    
    /** 
     * Subclasses may override this to do custom logic
     * after the new velocity has been calculated and before
     * the position calculation.
     */
    public void prePositionLogic(int tick) {}
    
    /** 
     * Subclasses may override this to do custom logic
     * just after the motion calculations.
     */
    public void postMotionLogic(int tick) {}
    
    /**
     * Simulates one tick of motion for this object
     * by doing the following, in order:
     * 
     * 1. Call preVelocityLogic()
     * 2. Use the acceleration to calculate the new velocity
     * 3. Call prePositionLogic()
     * 4. Use the velocity and velocity modifier to calculate effective velocity
     * 5. Use the effective velocity to calculate the new position
     * 6. Call postMotionLogic()
     * 
     * After the call, the current position has become the previous position
     * and the velocity modifier has been reset to (0, 0)
     * 
     * @param tickNumber Tick number of the current simulation tick 
     */
    public final void simulate(int tickNumber)
    {
        preVelocityLogic(tickNumber);

        velocity.add( acceleration );

        prePositionLogic(tickNumber);
        
        effectiveVelocity.set(velocity);
        effectiveVelocity.add(velocityModifier);
        velocityModifier.set(0, 0);
        previousPosition.set(position);
        position.add( effectiveVelocity );
        
        postMotionLogic(tickNumber);
    }
}
