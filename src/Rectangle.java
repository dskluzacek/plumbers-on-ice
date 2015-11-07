import java.util.Arrays;

public class Rectangle {
	
	private int x;
	private int y;
	private int w;
	private int h;

	public Rectangle(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
 
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getW() {
		return w;
	}
	
	public int getH() {
		return h;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setW(int w) {
		this.w = w;
	}
	
	public void setH(int h) {
		this.h = h;
	}

	public boolean intersects(Rectangle other) {
		return !( this.x + this.w < other.x || other.x + other.w < this.x
			    || this.y + this.h < other.y || other.y + other.h < this.y );
	}
	
	public CollisionInfo collisionInfo(Rectangle other) {
		if ( ! this.intersects(other) ) {
			return null;
		}
		
		int topDistance = (this.y + this.h) - other.y;
		int bottomDistance = this.y - (other.y + other.h);
		int leftDistance = (this.x + this.w) - other.x;
		int rightDistance = this.x - (other.x + other.w);
		
		int[] arr = new int[] { topDistance, bottomDistance, leftDistance, rightDistance };
		Arrays.sort(arr);
		
		int lowest = 0;
		
		for (int dist : arr)
		{
			if (dist >= 0) {
				lowest = dist;
				break;
			}
		}
		final int distance = lowest;
		final Direction direction;
		
		if (lowest == topDistance) {
			direction = Direction.TOP;
		} else if (lowest == leftDistance) {
			direction = Direction.LEFT;
		} else if (lowest == bottomDistance) {
			direction = Direction.BOTTOM;
		} else if (lowest == rightDistance) {
			direction = Direction.RIGHT;
		}
		else
			throw new IllegalStateException();
		
		return new CollisionInfo() {
			@Override
			public Direction getDirection() {
				return direction;
			}

			@Override
			public int getDistance() {
				return distance;
			}
		};
	}
	
	public interface CollisionInfo {
		Direction getDirection();
		int getDistance();
	}
}
