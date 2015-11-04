
public class Player extends Character {
	
	private String name;
	private Rectangle hitbox;
	private MovementAnimation movementAnim;
	private State state;
	private enum State {STANDING, RUNNING, JUMPING, FALLING, DYING};

	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public void draw(Renderer rend) {
		
	}
	
	public void respondToCollision(Block b, Side s) {
		
	}
	
	public void respondToCollision(Enemy e, Side s) {
		
	}
}
