package com.plumbers.game;

public abstract class Motionable {
	private Vector position = new Vector(0, 0);
	private Vector velocity = new Vector(0, 0);
	private Vector acceleration = new Vector(0, 0);
	
	public final Vector getPosition() {
		return new Vector(position);
	}
	
	public final void setPosition(Vector position) {
		this.position = position;
	}
	
	public final Vector getVelocity() {
		return new Vector(velocity);
	}
	
	public final void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}
	
	public final Vector getAcceleration() {
		return new Vector(acceleration);
	}
	
	public final void setAcceleration(Vector acceleration) {
		this.acceleration = acceleration;
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
	
	public void simulate() {
		velocity.add( acceleration );
		position.add( velocity );
	}
}