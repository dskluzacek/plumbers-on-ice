package com.plumbers.game;

public class Vector {
	private float x;
	private float y;

	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector(Vector copied) {
		this.x = copied.x;
		this.y = copied.y;
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
	
	public Vector scalarProduct(float i) {
		float u = this.x * i;
		float v = this.y * i;
		return new Vector(u, v);
	}

	public Vector sum(Vector r) {
		float a = this.x + r.x;
		float b = this.y + r.y;
		return new Vector(a, b);
	}
}
