package com.plumbers.game;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Controls the view dimensions, zoom level, and positioning and movement of the camera.
 */
public final class GameCamera
{
    private CameraConfig currentConfig;
    private OrthographicCamera camera;
    private Level level;
    private Viewport viewport;
    private float xInitial;
    private float yInitial;
    private float yMax;
    private float yMin = Float.NaN;
    private float xDisplacement = 0;
    private float yLerpTarget = Float.NaN;
    
    private static final float TABLET_DISP_HEIGHT_INCHES = 3.125f;
    
    private static final class CameraConfig
    { 
        public int virtualWidth;
        public int virtualHeight;
        public float heightInTiles;
        public int screenPixelsPerTile;
        public int scrollPositionX;
        public int downScrollPositionY;
        
        private CameraConfig()
        {
        }
      
        public static CameraConfig fromScreenMetrics(int widthInPixels,
                                                     int heightInPixels,
                                                     float heightInGameTiles)
        {
            CameraConfig config = new CameraConfig();
            config.heightInTiles = heightInGameTiles;
            config.virtualHeight = (int) (heightInGameTiles * Block.SIZE);
            config.virtualWidth = (int) ( widthInPixels * config.virtualHeight
                                          / ((float) heightInPixels) );
            config.screenPixelsPerTile = (int) (heightInPixels / heightInGameTiles);
            
            assert(config.virtualHeight == heightInGameTiles * Block.SIZE);
            assert(config.screenPixelsPerTile == heightInPixels / heightInGameTiles);
            
            config.scrollPositionX =
                    (int) (config.virtualWidth - Math.min(0.7 * config.virtualWidth, 640));
            config.downScrollPositionY = (int) (0.4 * config.virtualHeight);
            
            return config;
        }

        @Override
        public String toString()
        {
            return "CameraConfig [virtualWidth=" + virtualWidth
                    + ", virtualHeight=" + virtualHeight + ", heightInTiles="
                    + heightInTiles + ", screenPixelsPerTile="
                    + screenPixelsPerTile + ", scrollPositionX="
                    + scrollPositionX + ", downScrollPositionY="
                    + downScrollPositionY + "]";
        }
    }
    
    public GameCamera(Viewport viewport)
    {
        this.viewport = viewport;
        
        camera = new OrthographicCamera();
        camera.setToOrtho(true);
        viewport.setCamera(camera);
    }
    
    public OrthographicCamera getCamera()
    {
        return camera;
    }
    
    public float getXDisplacement()
    {
        return xDisplacement;
    }
    
    public void reset()
    {
        xDisplacement = 0;
        camera.position.x = xInitial;
        camera.position.y = yInitial;
        camera.update();
    }
    
    public void resize(int width, int height)
    {
        viewport.update(width, height);
    }
    
    public int virtualWidth()
    {
        return currentConfig.virtualWidth;
    }
    
    public int virtualHeight()
    {
        return currentConfig.virtualHeight;
    }
    
    public Matrix4 combined()
    {
        return camera.combined;
    }
    
    /**
     * Returns a projection/view matrix putting (0, 0) at the upper left corner.
     */
    public Matrix4 getOriginMatrix()
    {
        float x = camera.position.x;
        float y = camera.position.y;
        
        camera.position.x = currentConfig.virtualWidth / 2.0f;
        camera.position.y = currentConfig.virtualHeight / 2.0f;
        camera.update();
        Matrix4 result = new Matrix4(camera.combined);
        
        camera.position.x = x;
        camera.position.y = y;
        camera.update();
        
        return result;
    }
    
    public float heightInTiles()
    {
        return currentConfig.heightInTiles;
    }
    
    public int screenPixelsPerTile()
    {
        return currentConfig.screenPixelsPerTile;
    }
    
    public int scrollPositionX()
    {
        return currentConfig.scrollPositionX;
    }
    
    public int downScrollPositionY()
    {
        return currentConfig.downScrollPositionY;
    }
    
    private float getTargetYPosition(float playerY)
    {
        float proposed = playerY - (Block.SIZE * 4.5f) + (currentConfig.virtualHeight / 2.0f);
                
        if (proposed > yMax)
        {
            return yMax;
        }
        else if (level.useCeiling() && proposed < yMin)
        {
            return yMin;
        }
        else
        {
            return proposed;
        }
    }
    
    public void setLevel(Level level)
    {
        this.level = level;
        xInitial = currentConfig.virtualWidth / 2.0f;
        
        float levelHeight = level.getHeightInTiles() * Block.SIZE;
        yMax = levelHeight - (currentConfig.virtualHeight / 2.0f);
        
        if ( level.useCeiling() )
        {
            yMin = currentConfig.virtualHeight / 2.0f;
        }
        
        yInitial = getTargetYPosition( level.getStartPosition().getY() );
        
        camera.position.x = xInitial;
        camera.position.y = yInitial;
    }

    /**
     * Repositions the camera if needed based on player position.
     */
    public boolean repositionCamera(float playerX, float playerY, Character.State playerState)
    {   
        int scrollX = currentConfig.scrollPositionX;
        int levelWidth = level.getWidthInTiles() * Block.SIZE;
        boolean flag = false;
        
        // x-axis
        if ( playerX - scrollX < levelWidth - currentConfig.virtualWidth )
        {
            if ( playerX - scrollX > xDisplacement )
            {   
                float change = playerX - xDisplacement - scrollX;
                change = MathUtils.lerp(0, change, 0.125f);
                
                camera.translate(change, 0);
                xDisplacement += change;
                camera.update();
                flag = true;
            }
        }
        else if ( playerX - scrollX > levelWidth - currentConfig.virtualWidth )
        {
            float change = (levelWidth - currentConfig.virtualWidth) - xDisplacement;
            
            camera.translate(change, 0);
            xDisplacement += change;
            camera.update();
            flag = true;
        }
        
        // y-axis
        if ( ! Float.isNaN(yLerpTarget) )
        {
            if ( ! MathUtils.isEqual(camera.position.y, yLerpTarget) )
            {
                camera.position.y = MathUtils.lerp(camera.position.y, yLerpTarget, 0.04f);
                camera.update();
                flag = true;
            }
            else
            {
                camera.position.y = yLerpTarget;
                yLerpTarget = Float.NaN;
            }
        }
        if ( playerState == Character.State.STANDING
             || playerState == Character.State.RUNNING )
        {
            float yTarget = getTargetYPosition(playerY);
            
            if (yTarget != camera.position.y)
            {
                yLerpTarget = yTarget;
            }
        }
        else if ( camera.position.y < yMax && playerY > camera.position.y )
        {
            camera.position.y = MathUtils.lerp(camera.position.y, playerY, 0.08f);
            flag = true;
            yLerpTarget = Float.NaN;
            
            if (camera.position.y > yMax)
            {
                camera.position.y = yMax;
            }
            camera.update();
        }
        
        return flag;
    }
    
    /**
     * Configures the viewport and camera based on device display metrics.
     * @param width The width of the display in pixels
     * @param height The height of the display in pixels
     * @param ppiY The pixels per inch in the y direction
     */
    public void configure(int width, int height, float ppiY)
    {   
        if (height == 320)
        {
            // TODO
        }
        
        boolean isDeviceTablet = (height / ppiY >= TABLET_DISP_HEIGHT_INCHES);
        
        ArrayList<Float> candidates = new ArrayList<Float>();
        boolean fifteen = false;
        float lowest = Float.MAX_VALUE,
              highest = 0,
              closestTo15_25 = 0,
              closestTo18 = 0,
              lowestAbove17 = Float.MAX_VALUE,
              highestBelow14 = 0;
        
        for (int d = 16; d <= 256; d += 16)
        {
            float heightInTiles = height / ((float) d);
            
            if (heightInTiles >= 10 && heightInTiles <= 20)
            {
                candidates.add(heightInTiles);
                
                if (heightInTiles == 15.0f) {
                    fifteen = true;
                }
                    
                if (heightInTiles < lowest) {    
                    lowest = heightInTiles;
                }
                if (heightInTiles > highest) {
                    highest = heightInTiles;
                }
                if ( Math.abs(15.25 - heightInTiles) < Math.abs(15.25 - closestTo15_25) ) {
                    closestTo15_25 = heightInTiles;
                }
                if ( Math.abs(18 - heightInTiles) < Math.abs(18 - closestTo18) ) {
                    closestTo18 = heightInTiles;
                }
                if (heightInTiles < 14 && (14 - heightInTiles) < (14 - highestBelow14)) {
                    highestBelow14 = heightInTiles;
                }
                if (heightInTiles > 17 && (heightInTiles - 17) < (lowestAbove17 - 17)) {
                    lowestAbove17 = heightInTiles;
                }

            }
            else if (heightInTiles < 10)
            {
                break;
            }
        }
        
        assert(lowest != 0);
        assert(highest != Float.MAX_VALUE);
        assert(lowestAbove17 > 17);
        assert(highestBelow14 < 14);
        
        if (fifteen && !isDeviceTablet)
        {
            currentConfig = CameraConfig.fromScreenMetrics(width, height, 15);
        }
        else if (candidates.size() < 1)
        {
            throw new IllegalStateException();
        }
        else if (candidates.size() == 1)
        {
            currentConfig = CameraConfig.fromScreenMetrics(
                                                  width, height, candidates.get(0));
        }
        else if (isDeviceTablet)
        {
            currentConfig = CameraConfig.fromScreenMetrics(
                                                  width, height, closestTo18);
        }
        else
        {
            currentConfig = CameraConfig.fromScreenMetrics(
                                                  width, height, closestTo15_25);
        }
        
        viewport.update(width, height);
                
        printDebugInfo(height, ppiY, isDeviceTablet, candidates, fifteen,
                lowest, highest, closestTo15_25, closestTo18, lowestAbove17,
                highestBelow14);
        
        System.out.println(currentConfig);
    }

//    @SuppressWarnings("unused")
    private static void printDebugInfo(int height, float ppiY,
            boolean isDeviceTablet, ArrayList<Float> candidates,
            boolean fifteen, float lowest, float highest, float closestTo15_25,
            float closestTo18, float lowestAbove17, float highestBelow14)
    {
        for (float f : candidates)
        {
            System.out.println(f);
        }
        System.out.println();
        
        System.out.print("fifteen?= ");
        System.out.println(fifteen);
        System.out.print("highest= ");
        System.out.println(highest);
        System.out.print("lowest= ");
        System.out.println(lowest);
        System.out.print("closest to 15.25= ");
        System.out.println(closestTo15_25);
        System.out.print("closest to 18= ");
        System.out.println(closestTo18);
        System.out.print("highest below 14= ");
        System.out.println(highestBelow14);
        System.out.print("lowest above 17= ");
        System.out.println(lowestAbove17);
        
        System.out.print("screen height in inches= ");
        System.out.println(height / ppiY);
        
        System.out.print("isDeviceTablet?= ");
        System.out.println(isDeviceTablet);
        
        System.out.println();
    }
    
}
