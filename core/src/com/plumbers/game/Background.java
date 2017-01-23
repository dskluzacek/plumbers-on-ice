package com.plumbers.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

public abstract class Background
{
    private Matrix4 projectionMatrix;
    
    public abstract void setGameWidth(int width);
    public abstract void render(Batch batch, int foregroundPosition, float elapsedTime);
    
    public void setProjectionMatrix(Matrix4 projectionMatrix)
    {
        this.projectionMatrix = projectionMatrix;
    }
    
    public Matrix4 getProjectionMatrix()
    {
        return projectionMatrix;
    }
    
// ------------------------------------------------------
    
    public static class GrasslandBackground extends Background
    {
        private BackgroundComponent landscape;
        private BackgroundComponent clouds;
        private static final float SCROLL_RATE = 0.125f,
                                   CLOUDS_SCROLL_RATE = 0.0625f;
        
        public GrasslandBackground(TextureRegion landscapeTexture,
                                   TextureRegion skyTexture)
        {
            landscape = new BackgroundComponent(landscapeTexture,
                                                landscapeTexture.getRegionWidth() * 2,
                                                landscapeTexture.getRegionHeight() * 2,
                                                SCROLL_RATE);
            
            clouds =  new BackgroundComponent(skyTexture, 
                                              skyTexture.getRegionWidth() * 2,
                                              skyTexture.getRegionHeight() * 2,
                                              CLOUDS_SCROLL_RATE);
        }
        
        @Override
        public void setGameWidth(int width)
        {
            landscape.setGameWidth(width);
            clouds.setGameWidth(width);
        }
    
        @Override
        public void render(Batch batch, int foregroundPosition, float time)
        {
            batch.setProjectionMatrix( getProjectionMatrix() );
            batch.disableBlending();
            batch.begin();
            landscape.render(batch, foregroundPosition);
            clouds.render(batch, foregroundPosition);
            batch.end();
            batch.enableBlending();
        }
            
    }
    
    public static class AutumnBackground extends Background
    {
        private BackgroundComponent landscape;
        private BackgroundComponent nightSky;
        private static final float SCROLL_RATE = 0.125f;
        
        public AutumnBackground(TextureRegion landscapeTexture,
                                TextureRegion skyTexture)
        {
            landscape = new BackgroundComponent(landscapeTexture,
                                                landscapeTexture.getRegionWidth() * 2,
                                                landscapeTexture.getRegionHeight() * 2,
                                                SCROLL_RATE);
            
            nightSky = new BackgroundComponent(skyTexture, 
                                               skyTexture.getRegionWidth() * 2,
                                               skyTexture.getRegionHeight() * 2,
                                               0.0f);
        }

        @Override
        public void setGameWidth(int width)
        {
            landscape.setGameWidth(width);
            nightSky.setGameWidth(width);
        }

        @Override
        public void render(Batch batch, int foregroundPosition, float time)
        {
            batch.setProjectionMatrix( getProjectionMatrix() );
            batch.disableBlending();
            batch.begin();
            landscape.render(batch, foregroundPosition);
            nightSky.render(batch, foregroundPosition);
            batch.end();
            batch.enableBlending();
        }
    }
    
    public static class ImageBackground extends Background
    {
        private final TextureRegion textureRegion;
        private final double scrollRateVsForeground;
        private final int scaledWidth;
        private final int scaledHeight;
        private int numIterations;
    
        public ImageBackground(TextureRegion textureRegion,
                               int scale,
                               double scrollRateVsForeground)
        {
            this.textureRegion = textureRegion;
            this.scrollRateVsForeground = scrollRateVsForeground;
    
            scaledWidth = scale * textureRegion.getRegionWidth();
            scaledHeight = scale * textureRegion.getRegionHeight();
        }
        
        @Override
        public void setGameWidth(int gameWidth)
        {
            numIterations = gameWidth / scaledWidth + 2;
        }
        
        @Override
        public void render(Batch batch, int foregroundPosition, float time)
        {
            int position = ((int) (foregroundPosition * scrollRateVsForeground)) % scaledWidth;
    
            batch.setProjectionMatrix( getProjectionMatrix() );
            batch.disableBlending();
            batch.begin();
    
            for (int n = 0; n < numIterations; n++)
            {
                batch.draw( textureRegion,
                            (n * scaledWidth - position),
                            0,
                            scaledWidth, 
                            scaledHeight );
            }
            batch.end();
            batch.enableBlending();
        }
    }
    
    private static class BackgroundComponent
    {
        private final TextureRegion textureRegion;
        private final double scrollRateVsForeground;
        private final int scaledWidth;
        private final int scaledHeight;
        private int numberOfIterations;
        
        BackgroundComponent(TextureRegion textureRegion,
                            int scaledWidth,
                            int scaledHeight,
                            double scrollRateVsForeground)
        {
            this.textureRegion = textureRegion;
            this.scrollRateVsForeground = scrollRateVsForeground;
            this.scaledWidth = scaledWidth;
            this.scaledHeight = scaledHeight;
        }
        
        void setGameWidth(int gameWidth)
        {
            numberOfIterations = gameWidth / scaledWidth + 2;
        }
        
        void render(Batch batch, int foregroundPosition)
        {
            int position = ((int) (foregroundPosition * scrollRateVsForeground)) % scaledWidth;
            
            for (int n = 0; n < numberOfIterations; n++)
            {
                batch.draw( textureRegion,
                            (n * scaledWidth - position),
                            0,
                            scaledWidth, 
                            scaledHeight );
            }
        }
    }
    
}
