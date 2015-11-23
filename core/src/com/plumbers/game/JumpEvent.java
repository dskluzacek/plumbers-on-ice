package com.plumbers.game;

public class JumpEvent implements Event {
	private static JumpEvent instance;
	
	private JumpEvent() {}
	
	@Override
	public void applyTo(EventContext context) {
		context.apply(this);
	}
	
	public static JumpEvent instance() {
		if (instance == null) {
			instance = new JumpEvent();
		}
		return instance;
	}
}
