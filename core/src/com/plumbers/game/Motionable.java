package com.plumbers.game;

public abstract class Motionable {
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

    public final void setPosition(Vector position) {
        this.position.setX( position.getX() );
        this.position.setY( position.getY() );
    }

    public final void setPosition(float x, float y) {
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

    public final void setVelocity(Vector velocity) {
        this.velocity.setX( velocity.getX() );
        this.velocity.setY( velocity.getY() );
    }

    public final void setVelocity(float x, float y) {
        this.velocity.setX(x);
        this.velocity.setY(y);
    }

    public final float getXAcceleration() {
        return velocity.getX();
    }

    public final float getYAcceleration() {
        return acceleration.getY();
    }

    public final void setAcceleration(Vector acceleration) {
        this.acceleration.setX( acceleration.getX() );
        this.acceleration.setY( acceleration.getY() );
    }

    public final void setAcceleration(float x, float y) {
        this.acceleration.setX(x);
        this.acceleration.setY(y);
    }

    public final void setXPosition(float value) {
        position.setX(value);
    }

    public final void setYPosition(float value) {
        position.setY(value);
    }

    public final void setXVelocity(float value) {
        velocity.setX(value);
    }

    public final void setYVelocity(float value) {
        velocity.setY(value);
    }

    public final void setXAccel(float value) {
        acceleration.setX(value);
    }

    public final void setYAccel(float value) {
        acceleration.setY(value);
    }

    public final void addXVelocityModifier(float amount) {
        velocityModifier.setX( velocityModifier.getX() + amount );
    }

    public final void addYVelocityModifier(float amount) {
        velocityModifier.setY( velocityModifier.getY() + amount );
    }

    public float getEffectiveXVelocity() {
        return effectiveVelocity.getX();
    }

    public float getEffectiveYVelocity() {
        return effectiveVelocity.getY();
    }

    public float getPreviousX() {
        return previousPosition.getX();
    }

    public float getPreviousY() {
        return previousPosition.getY();
    }

    public void preVelocityLogic(int tick) {}
    public void prePositionLogic(int tick) {}
    public void postMotionLogic(int tick) {}

    public final void simulate(int tickNumber) {
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
