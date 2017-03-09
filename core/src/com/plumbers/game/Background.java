package com.plumbers.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;

/**
 * The scrolling background displayed behind the game level.
 */
public abstract class Background
{
    private Matrix4 projectionMatrix;
    private Color clearColor = new Color(0, 0, 0, 1);
    
    private static ShapeRenderer shapes = new ShapeRenderer();
    
    /**
     * Sets the virtual width of the game view.
     */
    public abstract void setGameWidth(int width);
    
    /**
     * Subclasses must override this to render themselves.
     */
    public abstract void renderImpl(Batch batch, int foregroundPosition, float elapsedTime);
    
    /**
     * Subclasses may override this to use a ShapeRenderer before renderImpl() is called
     */
    public void colorFill(ShapeRenderer sr)
    {
    }
    
    public final void setProjectionMatrix(Matrix4 projectionMatrix)
    {
        this.projectionMatrix = projectionMatrix;
    }
    
    public final Matrix4 getProjectionMatrix()
    {
        return projectionMatrix;
    }
    
    public final void setClearColor(Color clearColor)
    {
        this.clearColor = clearColor;
    }
    
    public final Color getClearColor()
    {
        return clearColor;
    }
    
    public final void render(Batch batch, int foregroundPosition, float elapsedTime)
    {
        shapes.setProjectionMatrix(projectionMatrix);
        colorFill(shapes);
        
        batch.setProjectionMatrix( getProjectionMatrix() );
        batch.disableBlending();
        batch.begin();
        renderImpl(batch, foregroundPosition, elapsedTime);
        batch.end();
        batch.enableBlending();
    }
    
// ------------------------------------------------------
    
    public static final class GrasslandBackground extends Background
    {
        private BackgroundComponent landscape;
        private BackgroundComponent clouds;
        private BackgroundComponent hills;
        private BackgroundComponent animatedComponent;
        private int gameWidth;

        private static final float SCROLL_RATE = 0.125f,
                                   CLOUDS_SCROLL_RATE = 0.0625f,
                                   CLOUDS_SCROLL_PER_SEC = 4.0f,
                                   HILLS_SCROLL_RATE = 0.09375f;
        private static final int SCALING = 2,
                                 CLOUDS_DRAW_Y = 67;
        
        public GrasslandBackground(Texture texture)
        {
            setClearColor( new Color(95/255f, 117/255f, 112/255f, 1) );
            
            clouds = new BackgroundComponent(texture, 0, 28, SCALING)
                            .drawAt(CLOUDS_DRAW_Y)
                            .scrollRate(CLOUDS_SCROLL_RATE)
                            .scrollRatePerSecond(CLOUDS_SCROLL_PER_SEC);
            
            hills = new BackgroundComponent(texture, 27, 7, SCALING)
                            .drawAt(CLOUDS_DRAW_Y + 27)
                            .scrollRate(HILLS_SCROLL_RATE);
            
            landscape = new BackgroundComponent(texture, 34, 55, SCALING)
                                .drawAt(CLOUDS_DRAW_Y + 34)
                                .scrollRate(SCROLL_RATE);
            
            animatedComponent = AnimatedComponent.make(texture, 28, SCALING, 0.5f, 89, 117)
                                    .drawAt(CLOUDS_DRAW_Y + 53)
                                    .scrollRate(SCROLL_RATE);
        }
        
        @Override
        public void setGameWidth(int width)
        {
            gameWidth = width;
            
            landscape.setGameWidth(width);
            clouds.setGameWidth(width);
            hills.setGameWidth(width);
            animatedComponent.setGameWidth(width);
        }
        
        @Override
        public void colorFill(ShapeRenderer shapes)
        {
            shapes.begin(ShapeType.Filled);
            shapes.setColor(110/255f, 178/255f, 224/255f, 1);
            shapes.rect(0, 0, gameWidth, CLOUDS_DRAW_Y * SCALING + 1);
            shapes.end();
        }
    
        @Override
        public void renderImpl(Batch batch, int foregroundPosition, float time)
        {   
            landscape.render(batch, foregroundPosition, time);
            animatedComponent.render(batch, foregroundPosition, time);
            hills.render(batch, foregroundPosition, time);
            clouds.render(batch, foregroundPosition, time);
        }
    }
    
    public static final class AutumnBackground extends Background
    {
        private final BackgroundComponent landscape;
        private final BackgroundComponent nightSky;
        private final BackgroundComponent trees1;
        private final BackgroundComponent trees2;
        private final BackgroundComponent trees3;
        private final BackgroundComponent trees4;

        private static final int SCALING = 2;
                                 
        public AutumnBackground(Texture texture)
        {
            setClearColor( new Color(131/255f, 82/255f, 65/255f, 1) );
            
            nightSky = new BackgroundComponent(texture, 0, 94, SCALING);
            
            trees1 = new BackgroundComponent(texture, 94, 4, SCALING)
                            .drawAt(94)
                            .scrollRate(1/16f);
            
            trees2 = new BackgroundComponent(texture, 98, 4, SCALING)
                            .drawAt(98)
                            .scrollRate(3/32f);
            
            trees3 = new BackgroundComponent(texture, 102, 5, SCALING)
                            .drawAt(102)
                            .scrollRate(1/8f);
            
            trees4 = new BackgroundComponent(texture, 107, 4, SCALING)
                            .drawAt(107)
                            .scrollRate(5/32f);
            
            landscape = new BackgroundComponent(texture, 110, 47, SCALING)
                                .drawAt(110)
                                .scrollRate(3/16f);
        }

        @Override
        public void setGameWidth(int width)
        {
            landscape.setGameWidth(width);
            trees1.setGameWidth(width);
            trees2.setGameWidth(width);
            trees3.setGameWidth(width);
            trees4.setGameWidth(width);
            nightSky.setGameWidth(width);
        }

        @Override
        public void renderImpl(Batch batch, int foregroundPosition, float time)
        {
            landscape.render(batch, foregroundPosition, time);
            trees1.render(batch, foregroundPosition, time);
            trees2.render(batch, foregroundPosition, time);
            trees3.render(batch, foregroundPosition, time);
            trees4.render(batch, foregroundPosition, time);
            nightSky.render(batch, foregroundPosition, time);
        }
    }
    
    public static final class TropicalBackground extends Background
    {
        private BackgroundComponent landscape;
        private BackgroundComponent clouds;
        private BackgroundComponent animatedComponent;
        
        private static final float SCROLL_RATE = 0.125f,
                                   CLOUDS_SCROLL_RATE = 0.0625f,
                                   CLOUDS_SCROLL_PER_SEC = 6.0f;
        private static final int SCALING = 2,
                                 CLOUDS_DRAW_Y = 96;
        
        public TropicalBackground(Texture texture)
        {
            setClearColor( new Color(111/255f, 178/255f, 224/255f, 1) );
            
            clouds = new BackgroundComponent(texture, 0, 31, SCALING)
                            .drawAt(CLOUDS_DRAW_Y)
                            .scrollRate(CLOUDS_SCROLL_RATE)
                            .scrollRatePerSecond(CLOUDS_SCROLL_PER_SEC);
            
            landscape = new BackgroundComponent(texture, 30, 21, SCALING)
                                .drawAt(CLOUDS_DRAW_Y + 30)
                                .scrollRate(SCROLL_RATE);
            
            animatedComponent = AnimatedComponent.make(texture, 7, SCALING, 7/12f, 51, 58)
                                    .drawAt(CLOUDS_DRAW_Y + 40)
                                    .scrollRate(SCROLL_RATE);
        }
        
        @Override
        public void setGameWidth(int width)
        {
            landscape.setGameWidth(width);
            clouds.setGameWidth(width);
            animatedComponent.setGameWidth(width);
        }
    
        @Override
        public void renderImpl(Batch batch, int foregroundPosition, float time)
        {   
            landscape.render(batch, foregroundPosition, time);
            animatedComponent.render(batch, foregroundPosition, time);
            clouds.render(batch, foregroundPosition, time);
        }
    }
    
    public static final class WinterBackground extends Background
    {
        private final BackgroundComponent landscape;
        private final BackgroundComponent nightSky;
        private final BackgroundComponent hills;
//        private final AnimatedStar[] stars;

        private static final int SCALING = 2;
//        private int[][] starCoordsA = new int[][] { {1, -3}, {33, 13}, {65, 28}, {17, 69},
//                                            {49, 85}, {81, 53}, {145, 85}, {97, 13}, {129, 29} };
                                 
        public WinterBackground(Texture texture)
        {
            setClearColor( new Color(172/255f, 147/255f, 169/255f, 1) );
            
            nightSky = new BackgroundComponent(texture, 0, 102, SCALING);
            
            hills = new BackgroundComponent(texture, 101, 7, SCALING)
                                .drawAt(101)
                                .scrollRate(3/32f);
            
            landscape = new BackgroundComponent(texture, 107, 49, SCALING)
                                .drawAt(107)
                                .scrollRate(1/8f);
            
//            TextureRegion starRegionA = new TextureRegion(texture, 0, 156, 15, 15);
//            starRegionA.flip(false, true);
//            
//            stars = new AnimatedStar[starCoordsA.length];
//            
//            for (int i = 0; i < starCoordsA.length; i++)
//            {
//                stars[i] = new AnimatedStar(starRegionA,
//                                            starCoordsA[i][0] * SCALING,
//                                            starCoordsA[i][1] * SCALING);
//            }
        }

        @Override
        public void setGameWidth(int width)
        {
            nightSky.setGameWidth(width);
            hills.setGameWidth(width);
            landscape.setGameWidth(width);
        }

        @Override
        public void renderImpl(Batch batch, int foregroundPosition, float time)
        {
            nightSky.render(batch, foregroundPosition, time);
            hills.render(batch, foregroundPosition, time);
            landscape.render(batch, foregroundPosition, time);
            
//            for (AnimatedStar star : stars)
//            {
//                star.render(batch);
//            }
        }
        
//        private final class AnimatedStar
//        {
//            private final TextureRegion textureRegion;
//            private final int x;
//            private final int y;
//            
//            public AnimatedStar(TextureRegion textureRegion, int x, int y)
//            {
//                this.textureRegion = textureRegion;
//                this.x = x;
//                this.y = y;
//            }
//
//            void render(Batch batch)
//            {
//                if (Math.random() < 0.05)//0.002)
//                {
//                    batch.draw(textureRegion, x, y, 30, 30);
//                }    
//            }
//        }
    }
    
    // A simple scrolling image background.
    // Can be removed once a specific background for all environments exist.
//    public static class ImageBackground extends Background
//    {
//        private final TextureRegion textureRegion;
//        private final double scrollRateVsForeground;
//        private final int scaledWidth;
//        private final int scaledHeight;
//
//        private int numIterations;
//    
//        public ImageBackground(TextureRegion textureRegion,
//                               int scale,
//                               double scrollRateVsForeground)
//        {
//            this.textureRegion = textureRegion;
//            this.scrollRateVsForeground = scrollRateVsForeground;
//    
//            scaledWidth = scale * textureRegion.getRegionWidth();
//            scaledHeight = scale * textureRegion.getRegionHeight();
//        }
//        
//        @Override
//        public void setGameWidth(int gameWidth)
//        {
//            numIterations = gameWidth / scaledWidth + 2;
//        }
//        
//        @Override
//        public void renderImpl(Batch batch, int foregroundPosition, float time)
//        {
//            int position = ((int) (foregroundPosition * scrollRateVsForeground)) % scaledWidth;
//    
//            for (int n = 0; n < numIterations; n++)
//            {
//                batch.draw( textureRegion,
//                            (n * scaledWidth - position),
//                            0,
//                            scaledWidth, 
//                            scaledHeight );
//            }
//        }
//    }
    
    // one component of a composed background,
    // with its own TextureRegion and scroll rate
    private static class BackgroundComponent
    {
        private TextureRegion textureRegion;
        private double scrollRateVsForeground;
        private double scrollRatePerSecond;
        private int yPosition;
        private int numberOfIterations;
        private int scaling;
        private final int scaledWidth;
        private final int scaledHeight;
        
        BackgroundComponent(TextureRegion textureRegion, int scaling)
        {
            this.textureRegion = textureRegion;
            this.scaling = scaling;
            this.scaledWidth = textureRegion.getRegionWidth() * scaling;
            this.scaledHeight = textureRegion.getRegionHeight() * scaling;
        }
        
        BackgroundComponent(Texture texture, int regionY, int regionHeight, int scaling)
        {   
            textureRegion = new TextureRegion(texture,
                                0, regionY, texture.getWidth(), regionHeight);
            textureRegion.flip(false, true);
            
            this.scaling = scaling;
            this.scaledWidth = textureRegion.getRegionWidth() * scaling;
            this.scaledHeight = textureRegion.getRegionHeight() * scaling;
        }
        
        final void setGameWidth(int gameWidth)
        {
            numberOfIterations = gameWidth / scaledWidth + 2;
        }
        
        final void setTextureRegion(TextureRegion region)
        {
            textureRegion = region;
        }
        
        void render(Batch batch, int foregroundPosition, float time)
        {
            int position = ((int) (foregroundPosition * scrollRateVsForeground
                                   + time * scrollRatePerSecond)) % scaledWidth;
            
            for (int n = 0; n < numberOfIterations; n++)
            {
                batch.draw( textureRegion,
                            (n * scaledWidth - position),
                            yPosition,
                            scaledWidth, 
                            scaledHeight );
            }
        }
        
        final BackgroundComponent drawAt(int yPosition)
        {
            this.yPosition = yPosition * scaling;
            return this;
        }
        
        final BackgroundComponent scrollRate(float r)
        {
            scrollRateVsForeground = r;
            return this;
        }
        
        final BackgroundComponent scrollRatePerSecond(float r)
        {
            scrollRatePerSecond = r;
            return this;
        }
    }
    
    private static final class AnimatedComponent extends BackgroundComponent
    {
        private final Animation animation;
        
        AnimatedComponent(Animation anim, int scaling)
        {
            super(anim.getKeyFrames()[0], scaling);
            
            animation = anim;
            animation.setPlayMode(PlayMode.LOOP);
        }
        
        @Override
        void render(Batch batch, int foregroundPosition, float time)
        {
            setTextureRegion( animation.getKeyFrame(time) );
            super.render(batch, foregroundPosition, time);
        }
        
        static BackgroundComponent make(Texture texture, int frameHeight,
                int scaling, float frameDuration, int... frameYPositions)
        {
            TextureRegion[] frames = new TextureRegion[frameYPositions.length];
            
            for (int i = 0; i < frameYPositions.length; i++)
            {
                frames[i] = new TextureRegion(texture,
                        0, frameYPositions[i], texture.getWidth(), frameHeight);
                frames[i].flip(false, true);
            }
            Animation anim = new Animation(frameDuration, frames);
            
            return new AnimatedComponent(anim, scaling);
        }
    }
    
}
