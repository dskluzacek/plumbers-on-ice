package com.plumbers.game;

public final class DeathEvent implements Event {
	private final Player player;
	
	public DeathEvent(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

	@Override
	public void applyTo(EventContext context) {
		context.apply(this);
	}

}
