package com.plumbers.game;

public interface EventContext {
	void apply(JumpEvent e);
	void apply(CoinEvent e);
	void apply(DeathEvent e);
}
