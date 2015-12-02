package com.plumbers.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class ButtonGroup {
    private final Button[] buttons;

    public ButtonGroup(Button[] buttons) {
        this.buttons = buttons;
    }
    
    public void draw(Batch batch) {
        for (Button b : buttons) {
            b.draw(batch);
        }
    }
    
    public void touchDown(float x, float y) {
        for (Button b : buttons) {
            b.touchDown((int) x, (int) y);
        }
    }
    
    public void touchUp(float x, float y) {
        for (Button b : buttons) {
            b.touchUp((int) x, (int) y);
        }
    }
    
    public boolean wasClicked() {
        for (Button b : buttons) {
            if ( b.testClicked() ) {
                return true;
            }
        }
        return false;
    }
    
    public Object getObject() {
        for (Button b : buttons) {
            if ( b.getClickedAndReset() )
                return b.getObject();
        }
        return null;
    }
}
