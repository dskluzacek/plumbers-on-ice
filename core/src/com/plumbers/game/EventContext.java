package com.plumbers.game;

public interface EventContext {
    void apply(JumpEvent e);
    void apply(CoinEvent e);
    void apply(DeathEvent e);
    void apply(DamageEvent e);
    void apply(FinishEvent e);
}
