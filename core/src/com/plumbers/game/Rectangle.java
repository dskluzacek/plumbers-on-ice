package com.plumbers.game;
import java.util.Arrays;

public class Rectangle {
	
	private float x;
	private float y;
	private float w;
	private float h;

	public Rectangle(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public Rectangle(Rectangle copied) {
		this.x = copied.x;
		this.y = copied.y;
		this.w = copied.w;
		this.h = copied.h;
	}
 
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getW() {
		return w;
	}
	
	public float getH() {
		return h;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setW(float w) {
		this.w = w;
	}
	
	public void setH(float h) {
		this.h = h;
	}

	public boolean intersects(Rectangle other) {
		return !( this.x + this.w <= other.x || other.x + other.w <= this.x
			    || this.y + this.h < other.y || other.y + other.h < this.y );
	}
	
	private boolean intersectsX(Rectangle other) {
		return !( this.x + this.w <= other.x || other.x + other.w <= this.x );
	}
	
	private boolean intersectsY(Rectangle other) {
		return !( this.y + this.h <= other.y || other.y + other.h <= this.y );
	}
	
	public Collision collisionInfo(Rectangle other, Vector myVelocity) {
		if ( ! this.intersects(other) ) {
			return null;
		}
		Collision coll = staticCollisionInfo(other);
		
		if ( myVelocity.getX() < 2.0f || myVelocity.getY() < 2.0f )
			return coll;
		else
			return resolveCollision(other, this, myVelocity);
	}
	
	public Collision staticCollisionInfo(Rectangle other) {
		if ( ! this.intersects(other) ) {
			return null;
		}
		
		float topDistance = (this.y + this.h) - other.y;
		float bottomDistance = (other.y + other.h) - this.y;
		float leftDistance = (this.x + this.w) - other.x;
		float rightDistance = (other.x + other.w) - this.x;

		float[] arr = new float[] { topDistance, bottomDistance, leftDistance, rightDistance };
		Arrays.sort(arr);
		
		float lowest = 0;
		
		for (float dist : arr)
		{
			if (dist >= 0) {
				lowest = dist;
				break;
			}
		}
		final float distance = lowest;
		final Direction direction;
		
		if (lowest == topDistance) {
			direction = Direction.TOP;
		} else if (lowest == bottomDistance) {
			direction = Direction.BOTTOM;
		} else if (lowest == leftDistance) {
			direction = Direction.LEFT;
		}  else if (lowest == rightDistance) {
			direction = Direction.RIGHT;
		}
		else
			throw new IllegalStateException();
		
		return new Collision() {
			@Override
			public Direction getDirection() {
				return direction;
			}

			@Override
			public float getDistance() {
				return distance;
			}
		};
	}

	public static Collision resolveCollision(Rectangle permanent, Rectangle moving, Vector velocity) {
 		Rectangle solution = new Rectangle(moving);
		boolean xAxis = resolve(permanent, solution, velocity.getX() / 10, velocity.getY() / 10, false);
		
		final Direction direction;
		final float distance;
		
		if (xAxis) {
			if (velocity.getX() > 0) {
				direction = Direction.LEFT;
			} else {
				direction = Direction.RIGHT;
			}
			distance = Math.abs(moving.x - solution.x);
		} else {
			if (velocity.getY() > 0) {
				direction = Direction.TOP;
			} else {
				direction = Direction.BOTTOM;
			}
			distance = Math.abs(moving.y - solution.y);
		}

		return new Collision() {
			@Override
			public Direction getDirection() {
				return direction;
			}

			@Override
			public float getDistance() {
				return distance;
			}
		};
	}
	
	private static boolean resolve(Rectangle permanent, Rectangle moving, float Vx, float Vy, boolean onXAxis) {
		if (onXAxis) {
			moving.x -= Vx;
			
			if ( ! moving.intersectsX(permanent) ) {
				return true;
			} else {
				return resolve(permanent, moving, Vx, Vy, false);
			}
		} else {
			moving.y -= Vy;
			
			if ( ! moving.intersectsY(permanent) ) {
				return false;
			} else {
				return resolve(permanent, moving, Vx, Vy, true);
			}
		}
	}
	
	public interface Collision {
		Direction getDirection();
		float getDistance();
	}
}
