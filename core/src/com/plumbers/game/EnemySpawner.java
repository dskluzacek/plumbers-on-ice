package com.plumbers.game;

/**
 * An object that creates an enemy at a certain position when
 * the player gets within a specific distance.
 */
public final class EnemySpawner
{
    private final int x, y;
    private final Enemy myEnemy;
    private final int spawnDistance;
    private boolean spawned = false;
    
    private static final int DEFAULT_DISTANCE = 900;

    public EnemySpawner(int x, int y, Enemy enemy)
    {
        this.x = x;
        this.y = y;
        this.myEnemy = enemy.copy();
        this.spawnDistance = DEFAULT_DISTANCE;
    }

    public EnemySpawner(int x, int y, Enemy enemy, int spawnDistance)
    {
        this.x = x;
        this.y = y;
        this.myEnemy = enemy.copy();
        this.spawnDistance = spawnDistance;
    }
    
    /** 
     * Spawns the enemy if not yet spawned and
     * the given player X position is within the spawn distance.
     */
    public Enemy spawn(float playerXPos)
    {
        if (spawned || playerXPos + spawnDistance < x)
        {
            return null;
        }
        else
        {
            spawned = true;
            
            myEnemy.reset();
            myEnemy.setPosition(x, y);
            return myEnemy;
        }
    }

    public void reset()
    {
        spawned = false;
    }
}
