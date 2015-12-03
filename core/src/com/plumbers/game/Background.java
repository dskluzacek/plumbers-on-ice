package com.plumbers.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

public final class Background {
	private final TextureRegion textureRegion;
	private final double scrollRateVsForeground;
	private final int scaledWidth;
	private final int scaledHeight;
	
	private int gameWidth;
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
	
	public void setProjMatrix(Matrix4 projectionMatrix, int worldWidth) {
		this.gameWidth = worldWidth;
		this.projectionMatrix = projectionMatrix;
		
		numIterations = gameWidth / scaledWidth + 2;
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
