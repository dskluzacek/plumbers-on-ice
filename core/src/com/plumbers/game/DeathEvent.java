package com.plumbers.game;

public final class DeathEvent implements Event {
    private static DeathEvent instance1, instance2;
    private final int player;
    
    private DeathEvent(int player) {
        this.player = player;
    }
    
    @Override
    public void applyTo(EventContext context) {
        context.apply(this);
    }
    
    public static DeathEvent playerOneInstance() {
        if (instance1 == null) {
            instance1 = new DeathEvent(1);
        }
        return instance1;
    }
    
    public static DeathEvent playerTwoInstance() {
        if (instance2 == null) {
            instance2 = new DeathEvent(2);
        }
        return instance2;
    }
    
    public int getPlayerNum() {
        return player;
    }
}
