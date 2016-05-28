package com.plumbers.game.ui;

public class ButtonGroup {
    private final Button[] buttons;

    public ButtonGroup(Button[] buttons) {
        this.buttons = buttons;
    }
    
    public void touchDown(float x, float y) {
        for (Button b : buttons) {
            b.touchDown((int) x, (int) y);
        }
    }
}
