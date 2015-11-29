package com.plumbers.game;

import java.util.Arrays;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public final class Rectangle implements Pool.Poolable {
	
	private float x;
	private float y;
	private float w;
	private float h;
	
	public Rectangle() {}

	public Rectangle(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	@Override
	public void reset() {
	    this.x = 0;
	    this.y = 0;
	    this.w = 0;
	    this.h = 0;
	}
	
	public Rectangle set(Rectangle r) {
	    this.x = r.x;
	    this.y = r.y;
	    this.w = r.w;
	    this.h = r.h;
	    return this;
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
		
//		if ( myVelocity.getX() < 2.0f || myVelocity.getY() < 2.0f ) {
			return coll;
//		} else {
//		    Pools.free(coll);
//		    
//			return resolveCollision(other, this, myVelocity);
//		}
	}
	
	private float[] arr = new float[4];
	
	public Collision staticCollisionInfo(Rectangle other) {
		if ( ! this.intersects(other) ) {
			return null;
		}
		
		float topDistance = (this.y + this.h) - other.y;
		float bottomDistance = (other.y + other.h) - this.y;
		float leftDistance = (this.x + this.w) - other.x;
		float rightDistance = (other.x + other.w) - this.x;

		arr[0] = topDistance;
		arr[1] = bottomDistance;
		arr[2] = leftDistance;
		arr[3] = rightDistance;
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
		
		return Pools.get(CollisionImpl.class).obtain().set(direction, distance);
	}

	public static Collision resolveCollision(Rectangle permanent, Rectangle moving, Vector velocity) {
 		Rectangle solution = Pools.get(Rectangle.class).obtain().set(moving);
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
		
		/* ---- */
		Pools.free(solution);
		/* ---- */

		return Pools.get(CollisionImpl.class).obtain().set(direction, distance);
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
	
	public static interface Collision {
		Direction getDirection();
		float getDistance();
	}
	
	private static class CollisionImpl implements Collision, Pool.Poolable {
	    private Direction direction;
	    private float distance;
	    
	    private CollisionImpl() {}
	    
	    public Collision set(Direction dir, float dist) {
	        direction = dir;
	        distance = dist;
	        return this;
	    }
	    
	    @Override
	    public void reset() {
	        direction = null;
	        distance = 0;
	    }
	    
        @Override
        public Direction getDirection() {
            return direction;
        }

        @Override
        public float getDistance() {
            return distance;
        }
	}
}
