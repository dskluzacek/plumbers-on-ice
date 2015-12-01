package com.plumbers.game;

public final class DamageEvent implements Event {
    private static DamageEvent instance1, instance2;
    private final int player;
    
    private DamageEvent(int player) {
        this.player = player;
    }
    
    @Override
    public void applyTo(EventContext context) {
        context.apply(this);
    }
    
    public static DamageEvent playerOneInstance() {
        if (instance1 == null) {
            instance1 = new DamageEvent(1);
        }
        return instance1;
    }
    
    public static DamageEvent playerTwoInstance() {
        if (instance2 == null) {
            instance2 = new DamageEvent(2);
        }
        return instance2;
    }
    
    public int getPlayerNum() {
        return player;
    }
}
