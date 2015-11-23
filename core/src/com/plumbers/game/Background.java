package com.plumbers.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

public final class Background {
	private final TextureRegion textureRegion;
	private final double scrollRateVsForeground;
//	private final int scale;
	private final int gameWidth;
	
	private final int scaledWidth;
	private final int scaledHeight;
	private final int numIterations;
	private final Matrix4 projectionMatrix;
	
	public Background(TextureRegion textureRegion,
	                  int scale,
	                  double scrollRateVsForeground,
	                  int gameGraphicalWidth,
	                  int gameGraphicalHeight)
	{
		this.textureRegion = textureRegion;
		this.scrollRateVsForeground = scrollRateVsForeground;
//		this.scale = scale;
		this.gameWidth = gameGraphicalWidth;
		
		scaledWidth = scale * textureRegion.getRegionWidth();
		scaledHeight = scale * textureRegion.getRegionHeight();
		numIterations = gameWidth / scaledWidth + 2;
		
		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(true, gameGraphicalWidth, gameGraphicalHeight);
		camera.update();
		projectionMatrix = camera.combined;
		System.out.println(projectionMatrix);
	}
	
	public void render(Batch batch, int foregroundPosition) {
		int position = ((int)(foregroundPosition * scrollRateVsForeground)) % scaledWidth;
		
		batch.setProjectionMatrix(projectionMatrix);
		batch.begin();
		
		for (int n = 0; n < numIterations; n++) {
			batch.draw(textureRegion,
			           (n * scaledWidth - position),
			           0,
			           scaledWidth,
			           scaledHeight);
		}
		batch.end();
	}
}
