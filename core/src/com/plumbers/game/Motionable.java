package com.plumbers.game;

public abstract class Motionable {
	private final Vector position = new Vector(0, 0);
	private final Vector velocity = new Vector(0, 0);
	private final Vector acceleration = new Vector(0, 0);
	
	public final Vector getPosition() {
		return new Vector(position);
	}
	
	public final void setPosition(Vector position) {
		this.position.setX( position.getX() );
		this.position.setY( position.getY() );
	}
	
	public final void setPosition(float x, float y) {
		this.position.setX(x);
		this.position.setY(y);
	}
	
	public final Vector getVelocity() {
		return new Vector(velocity);
	}
	
	public final void setVelocity(Vector velocity) {
		this.velocity.setX( velocity.getX() );
		this.velocity.setY( velocity.getY() );
	}
	
	public final void setVelocity(float x, float y) {
		this.velocity.setX(x);
		this.velocity.setY(y);
	}
	
	public final Vector getAcceleration() {
		return new Vector(acceleration);
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
	
	public void preVelocityLogic(int tick) {}
	public void prePositionLogic(int tick) {}
	public void postMotionLogic(int tick) {}
	
	public final void simulate(int tickNumber) {
		preVelocityLogic(tickNumber);
		velocity.add( acceleration );
		prePositionLogic(tickNumber);
		position.add( velocity );
		postMotionLogic(tickNumber);
	}
}
