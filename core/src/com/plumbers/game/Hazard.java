package com.plumbers.game;

public class Hazard {
	private final Rectangle rectangle;
	
	public Hazard(Rectangle rectangle) {
		this.rectangle = new Rectangle(rectangle);
	}
	
	public Rectangle getRectangle() {
		return rectangle;
	}
}
