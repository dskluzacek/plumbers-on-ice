package com.plumbers.game;

public class FixedHazard implements Hazard {
	private final Rectangle rectangle;
	
	public FixedHazard(Rectangle rectangle) {
		this.rectangle = new Rectangle().set(rectangle);
	}
	
	public Rectangle getRectangle() {
		return rectangle;
	}
}
