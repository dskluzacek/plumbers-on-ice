package com.plumbers.game;

import com.badlogic.gdx.utils.Pool;
import com.plumbers.game.Enemy.Type;

/**
 * An object that creates an enemy at a certain position when
 * the player gets within a specific distance.
 */
public final class EnemySpawner
{
    private final int x, y;
    private final Enemy.Type type;
    private final int spawnDistance;
    private boolean spawned = false;
    private static final int DEFAULT_DISTANCE = 900;

    public EnemySpawner(int x, int y, Type type)
    {
        this.x = x;
        this.y = y;
        this.type = type;

        spawnDistance = DEFAULT_DISTANCE;
    }

    public EnemySpawner(int x, int y, Type type, int spawnDistance)
    {
        this.x = x;
        this.y = y;
        this.type = type;
        this.spawnDistance = spawnDistance;
    }
    
    /** 
     * Spawns the enemy if not yet spawned and
     * the given player X position is within the spawn distance.
     */
    public Enemy spawn(float playerXPos, Pool<Enemy> enemyPool)
    {
        if (spawned || playerXPos + spawnDistance < x)
        {
            return null;
        }
        spawned = true;
        return enemyPool.obtain().init(type, x, y);
    }

    public void reset()
    {
        spawned = false;
    }
}
