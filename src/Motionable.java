
public abstract class Motionable {
	private Vector position;
	private Vector velocity;
	private Vector acceleration;
	
	public Vector getPosition() {
		return position;
	}
	
	public void setPosition(Vector position) {
		this.position = position;
	}
	
	public Vector getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}
	
	public Vector getAcceleration() {
		return acceleration;
	}
	
	public void setAcceleration(Vector acceleration) {
		this.acceleration = acceleration;
	}

	public void simulate() {
		// to do
	}
}