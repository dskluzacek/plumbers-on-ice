package com.plumbers.game;

public class JumpEvent implements Event {
	private static JumpEvent instance1, instance2;
	private final int player;
	
	private JumpEvent(int player) {
	    this.player = player;
	}
	
	@Override
	public void applyTo(EventContext context) {
		context.apply(this);
	}
	
	public static JumpEvent playerOneInstance() {
		if (instance1 == null) {
			instance1 = new JumpEvent(1);
		}
		return instance1;
	}
	
	public static JumpEvent playerTwoInstance() {
        if (instance2 == null) {
            instance2 = new JumpEvent(2);
        }
        return instance2;
    }
	
	public int getPlayerNum() {
	    return player;
	}
}
