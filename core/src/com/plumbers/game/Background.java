package com.plumbers.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

public final class Background {
	private final TextureRegion textureRegion;
	private final double scrollRateVsForeground;
	private final int scaledWidth;
	private final int scaledHeight;
	
	private int numIterations;
	private Matrix4 projectionMatrix;
	
	public Background(TextureRegion textureRegion,
	                  int scale,
	                  double scrollRateVsForeground)
	{
		this.textureRegion = textureRegion;
		this.scrollRateVsForeground = scrollRateVsForeground;

		scaledWidth = scale * textureRegion.getRegionWidth();
		scaledHeight = scale * textureRegion.getRegionHeight();
	}
	
	public void setProjectionMatrix(Matrix4 matrix) {
		projectionMatrix = matrix;
	}
	
	public void render(Batch batch, int foregroundPosition) {
		int position = ((int)(foregroundPosition * scrollRateVsForeground)) % scaledWidth;
		
		batch.setProjectionMatrix(projectionMatrix);
		batch.disableBlending();
		batch.begin();
		
		for (int n = 0; n < numIterations; n++) {
			batch.draw(textureRegion,
			           (n * scaledWidth - position),
			           0,
			           scaledWidth,
			           scaledHeight);
		}
		batch.end();
		batch.enableBlending();
	}
}
