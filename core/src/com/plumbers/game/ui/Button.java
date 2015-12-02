package com.plumbers.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Button {
    protected final int xPos, yPos, width, height;
    private Label label;
    private boolean isTouched;
    private boolean clicked;
    private TextureRegion unpressed, pressed;
    private Object object;
    
    public Button(TextureRegion reg, TextureRegion touch, int x, int y, int width, int height, Object object) {
        this.unpressed = reg;
        this.pressed = touch;
        this.xPos = x;
        this.yPos = y;
        this.width = width;
        this.height = height;
        this.object = object;
    }
    
    public void draw(Batch batch) {
        if (isTouched) {
            batch.draw(pressed, xPos, yPos, width, height);
        }
        else {
            batch.draw(unpressed, xPos, yPos, width, height);
        }
    }
    
    public void touchDown(int x, int y) {
        System.out.println("touch event: " + x + ", " + y);
        if (! isTouched && this.containsPoint(x, y)) {
            isTouched = true;
        }
    }
    
    public void touchUp(int x, int y) {
        if (isTouched && this.containsPoint(x, y)) {
            clicked = true;
        }
        isTouched = false;
    }
    
    public boolean testClicked() {
        return clicked;
    }
    
    public boolean getClickedAndReset() {
        boolean click = clicked;
        clicked = false;
        return click;
    }
    
    public Object getObject() {
        return object;
    }

    public boolean containsPoint(int x, int y) {
        return ( x >= xPos && x <= xPos + width
                && y >= yPos && y <= yPos + height );
    }
}
