package com.plumbers.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;

/**
 * The model. Represents the state of the game simulation. 
 */
public final class GameModel
{
    private final Player player1; 
    private final Player player2; 
    private final Level currentLevel;
    private final SpawnStrategy spawnStrategy;
    private final List<Event> occuringEvents = new ArrayList<Event>(); 
    private final List<Enemy> enemies = new ArrayList<Enemy>(); 
    private final List<Drawable> drawables = new ArrayList<Drawable>();
    private final int levelBottom;
    private final boolean twoPlayer;
    private int gameTicks = 0;
    private Pool<Enemy> enemyPool = new ReflectionPool<Enemy>(Enemy.class, 20);
    
    public static final float GRAVITY = 0.4f;

    public GameModel(Level level, Player p1)
    {
        currentLevel = level;
        levelBottom = level.getHeightInTiles() * Block.SIZE;
        player1 = p1;
        player2 = null;
        twoPlayer = false;
        spawnStrategy = new SinglePlayerSpawnStrategy();
    }

    public GameModel(Level level,
                     Player p1,
                     Player p2,
                     SpawnStrategy spawnStrategy)
    {
        currentLevel = level;
        levelBottom = level.getHeightInTiles() * Block.SIZE;
        player1 = p1; 
        player2 = p2; 
        twoPlayer = true;

        if (spawnStrategy != null) {
            this.spawnStrategy = spawnStrategy;
        }
        else {
            this.spawnStrategy = new SinglePlayerSpawnStrategy();
        }
    }

    public List<Event> gameTick()
    {
        ++gameTicks;
        occuringEvents.clear();

        simulate(player1);

        if (twoPlayer)
        {
            simulate(player2);
        }
        
        enemies.addAll( spawnStrategy.spawnEnemies() );

        for (int i = 0; i < enemies.size(); i++)
        {
            simulate( enemies.get(i) );
        }

        return occuringEvents;
    }

    private void simulate(Player p)
    {
        p.simulate(gameTicks);

        if ( currentLevel.useCeiling() ) {
            p.ceilingCheck();
        }
        p.fallingCheck( currentLevel.getBlockArray() );
        p.collisionCheck( currentLevel.getBlockArray() );
        p.hazardCollisionCheck( currentLevel.getHazards() );
        p.hazardCollisionCheck(enemies);
        addNotNull( occuringEvents, p.getEvent() );
        occuringEvents.addAll(
                p.coinCollectCheck(currentLevel.getCoins(), gameTicks) );
        addNotNull( occuringEvents,
                p.springboardCheck(currentLevel.getSpringboards(), gameTicks) );
        addNotNull( occuringEvents,
                p.fallingDeathCheck(levelBottom) );

        if (! p.isLevelCompleted() && currentLevel.getFinish() != null)
        {
            addNotNull( occuringEvents, p.finishedLevelCheck(currentLevel.getFinish()) );
        }
    }
    
    private void simulate(Enemy enemy)
    {
        enemy.simulate(gameTicks);
        enemy.fallingCheck( currentLevel.getBlockArray() );
        enemy.collisionCheck( currentLevel.getBlockArray() );
    }

    public List<Drawable> getDrawables()
    {
        drawables.clear();
        drawables.addAll(currentLevel.getSpringboards());
        drawables.add(player1);
        addNotNull(drawables, player2);
        drawables.addAll(enemies);
        return drawables;
    }

    public void reset()
    {
        if (twoPlayer)
        {
            throw new IllegalStateException();
        }

        List<EnemySpawner> spawners = currentLevel.getSpawners();

        for (int i = 0; i < spawners.size(); i++) 
        {
            spawners.get(i).reset();
        }

        enemies.clear();
        currentLevel.resetCoins();
        currentLevel.resetSpringboards();
        gameTicks = 0;
        player1.reset( currentLevel.getStartPosition() );
    }

    public int getTickNumber()
    {
        return gameTicks;
    }
    
    public boolean isLevelCompleted()
    {
        return player1.isLevelCompleted();
    }

    public int getLevelWidth()
    {
        return currentLevel.getWidthInTiles() * Block.SIZE;
    }
    
    public int getLevelHeight()
    {
        return currentLevel.getHeightInTiles() * Block.SIZE;
    }

    private static <T> void addNotNull(Collection<T> c, T obj)
    {
        if (obj != null)
        {
            c.add(obj);
        }
    }

    private class SinglePlayerSpawnStrategy implements SpawnStrategy
    {
        private List<Enemy> list = new ArrayList<Enemy>();

        @Override
        public List<Enemy> spawnEnemies()
        {
            list.clear();
            List<EnemySpawner> spawners = currentLevel.getSpawners();
            float playerX = player1.getXPosition();

            for (int i = 0; i < spawners.size(); i++)
            {
                addNotNull( list, spawners.get(i).spawn(playerX, enemyPool) );
            }
            return list;
        }
    }

}
