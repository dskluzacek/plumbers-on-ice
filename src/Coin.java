
public final class Coin implements Drawable {
	private final int x;
	private final int y;
	private boolean collected;
	private static final Animation coinAnimation;
	
	public Coin(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setCollected(boolean collected) {
		this.collected = collected;
	}
	
	public boolean isCollected() {
		return collected;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
