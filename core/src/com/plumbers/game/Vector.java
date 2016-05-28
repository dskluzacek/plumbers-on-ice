package com.plumbers.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;

public class Vector implements Pool.Poolable {
    private float x;
    private float y;

    public Vector() {}

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public boolean isZero() {
        return MathUtils.isZero(x) && MathUtils.isZero(y);
    }

    public boolean isExactlyZero() {
        return x == 0.0f && y == 0.0f;
    }

    public void add(Vector r) {
        this.x = this.x + r.x;
        this.y = this.y + r.y;
    }

    public void subtract(Vector r) {
        this.x = this.x - r.x;
        this.y = this.y - r.y;
    }

    public void multiply(float i) {
        this.x = this.x * i;
        this.y = this.y * i;
    }

    public Vector multipliedBy(float i) {
        float u = this.x * i;
        float v = this.y * i;
        return new Vector(u, v);
    }

    public Vector sum(Vector r) {
        float a = this.x + r.x;
        float b = this.y + r.y;
        return new Vector(a, b);
    }

    public Vector set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector set(Vector v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    @Override
    public void reset() {
        x = 0;
        y = 0;
    }
}
