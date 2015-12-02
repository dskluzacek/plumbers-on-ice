package com.plumbers.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;

public final class Label {
    private String string;
    private final boolean centerAlign;
    private final int alignWidth;
    private final int x, y;
    
    public Label(int x, int y, String string, int alignWidth, boolean centerAlign) {
        this.x = x;
        this.y = y;
        this.string = string;
        this.alignWidth = alignWidth;
        this.centerAlign = centerAlign;
    }
    
    public void draw(Batch batch, BitmapFont font) {
        if (! centerAlign) {
            font.draw(batch, string, x, y);
        } else {
            font.draw(batch, string, x, y, alignWidth, Align.center, false);
        }
    }
}
