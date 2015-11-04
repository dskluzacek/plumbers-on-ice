public abstract class Character {
	String name;
	Rectangle hitbox;
	MovementAnimation movementAnim;
	State state;

	public enum State {
		standing, running, jumping, falling, dying
	}

	public State getState() {
		return state;
	}

	public void setState(State s) {
		state = s;
	}

	abstract void respondToCollision(Block b, Side s);

	public void respondToCollision(Enemy e, Side s) {
	//fill in method
	}
}
