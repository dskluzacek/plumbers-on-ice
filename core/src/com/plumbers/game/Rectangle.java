package com.plumbers.game;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public final class Rectangle {

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

    public Rectangle(Rectangle r) {
        this.x = r.x;
        this.y = r.y;
        this.w = r.w;
        this.h = r.h;
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

    private static boolean intersectsX(Rectangle other, float x, float w) {
        return !( x + w <= other.x || other.x + other.w <= x );
    }

    private static boolean intersectsY(Rectangle other, float y, float h) {
        return !( y + h <= other.y || other.y + other.h <= y );
    }

    public Collision collisionInfo(Rectangle other, float prevX, float prevY) {
        if ( ! this.intersects(other) ) {
            return null;
        }
        Collision coll = staticCollisionInfo(other);

        if (coll == TOP_0) {
            return TOP_0;
        } else {
            float distance = Float.NaN;
            Direction direction = resolveCollision(other, prevX, prevY);

            if (direction == null)
                return coll;

            switch (direction) {
                case TOP: distance = topDistance(other); break;
                case BOTTOM: distance = bottomDistance(other); break;
                case LEFT: distance = leftDistance(other); break;
                case RIGHT: distance = rightDistance(other); break;
            }

            ((CollisionImpl) coll).set(direction, distance);
            return coll;
        }
    }

    private float topDistance(Rectangle other) {
        return (this.y + this.h) - other.y;
    }

    private float bottomDistance(Rectangle other) {
        return (other.y + other.h) - this.y;
    }

    private float leftDistance(Rectangle other) {
        return (this.x + this.w) - other.x;
    }

    private float rightDistance(Rectangle other) {
        return (other.x + other.w) - this.x;
    }

    public Collision staticCollisionInfo(Rectangle other) {
        if ( ! this.intersects(other) ) {
            return null;
        }

        float topDistance = topDistance(other);
        float bottomDistance = bottomDistance(other);
        float leftDistance = leftDistance(other);
        float rightDistance = rightDistance(other);

        float lowest = 0;

        if (topDistance > 0) {
            lowest = topDistance;
        }
        if (bottomDistance > 0 && bottomDistance < lowest) {
            lowest = bottomDistance;
        }
        if (leftDistance > 0 && leftDistance < lowest) {
            lowest = leftDistance;
        }
        if (rightDistance > 0 && rightDistance < lowest) {
            lowest = rightDistance;
        }

        final float distance = lowest;
        final Direction direction;

        if (distance == topDistance) {
            direction = Direction.TOP;
        } else if (distance == bottomDistance) {
            direction = Direction.BOTTOM;
        } else if (distance == leftDistance) {
            direction = Direction.LEFT;
        }  else if (distance == rightDistance) {
            direction = Direction.RIGHT;
        }
        else
            throw new IllegalStateException();

        if (direction == Direction.TOP && distance == 0) {
            return TOP_0;
        } else {
            return Pools.get(CollisionImpl.class).obtain().set(direction, distance);
        }
    }

    public Direction resolveCollision(Rectangle other, float prevX, float prevY)
    {
        final Direction direction;

        if ( intersectsX(other, prevX, w) ) {
            if (prevY + h < other.y + other.h) {
                direction = Direction.TOP;
            } else {
                direction = Direction.BOTTOM;
            }
        } else if ( intersectsY(other, prevY, h) ) {
            if (prevX + w <= other.x) {
                direction = Direction.LEFT;
            } else {
                direction = Direction.RIGHT;
            }
        } else {
            direction = null;
        }

        return direction;
    }

    public static void disposeOf(Collision obj) {
        if (obj == null || obj == TOP_0) {
            return;
        }

        if (obj instanceof CollisionImpl) {
            Pools.free(obj);
        }
    }

    public static interface Collision {
        Direction getDirection();
        float getDistance();
    }

    private static final Collision TOP_0 = new Collision() {
        @Override
        public Direction getDirection() {
            return Direction.TOP;
        }

        @Override
        public float getDistance() {
            return 0;
        }
    };

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
