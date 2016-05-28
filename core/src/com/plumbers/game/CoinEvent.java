package com.plumbers.game;

public class CoinEvent implements Event {
    private static CoinEvent instance;

    private CoinEvent() {}

    @Override
    public void applyTo(EventContext context) {
        context.apply(this);
    }

    public static CoinEvent instance() {
        if (instance == null) {
            instance = new CoinEvent();
        }
        return instance;
    }
}
