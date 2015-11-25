package com.plumbers.game;

public class DamageEvent implements Event {
	private static DamageEvent instance;
	
	private DamageEvent() {}
	
	@Override
	public void applyTo(EventContext context) {
		context.apply(this);
	}
	
	public static DamageEvent instance() {
		if (instance == null) {
			instance = new DamageEvent();
		}
		return instance;
	}
}
