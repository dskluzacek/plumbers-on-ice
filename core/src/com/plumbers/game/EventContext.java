package com.plumbers.game;

/**
 * A context defining view behavior for the various types of events.
 */
public interface EventContext
{
    void apply(JumpEvent e);
    void apply(CoinEvent e);
    void apply(SpringboardEvent e);
    void apply(DeathEvent e);
    void apply(DamageEvent e);
    void apply(FinishEvent e);
}
