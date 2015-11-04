
public final class Coin implements Drawable {
	private final int x;
	private final int y;
	private final Rectangle hitbox;
	private boolean collected;
	private static final Animation coinAnimation;
	private static final int COIN_SIZE = 25;
	
	public Coin(int x, int y) {
		this.x = x;
		this.y = y;
		hitbox = new Rectangle(x, y, x + COIN_SIZE, y + COIN_SIZE);
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
	
	public Rectangle getRectangle() {
		return hitbox;
	}
}
