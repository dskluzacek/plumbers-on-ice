package com.plumbers.game.ui;

public class Button {
    protected final int xPos, yPos, width, height;
    private Listener listener;
    
    public Button(int x, int y, int width, int height, Listener listener) {
        this.xPos = x;
        this.yPos = y;
        this.width = width;
        this.height = height;
        this.listener = listener;
    }
    
    public void touchDown(int x, int y) {
        if ( this.containsPoint(x, y) ) {
            listener.action();
        }
    }

    public boolean containsPoint(int x, int y) {
        return ( x >= xPos && x <= xPos + width
                && y >= yPos && y <= yPos + height );
    }
    
    interface Listener {
        void action();
    }
}
