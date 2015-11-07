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
		
		final Direction direction;
		final int distance;
		
		if (arr[0] == topDistance && topDistance >= 0) {
			direction = Direction.TOP;
			distance = topDistance;
		} else if (arr[0] == leftDistance && leftDistance >= 0) {
			direction = Direction.LEFT;
			distance = leftDistance;
		} else if (arr[0] == bottomDistance && bottomDistance >= 0) {
			direction = Direction.BOTTOM;
			distance = bottomDistance;
		} else if (arr[0] == rightDistance && rightDistance >= 0) {
			direction = Direction.RIGHT;
			distance = rightDistance;
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
