package com.plumbers.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.badlogic.gdx.utils.Array;

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
    private final List<Drawable> drawables = new ArrayList<Drawable>();
    private final Array<Enemy> enemies = new Array<Enemy>(); 
    private final int levelBottom;
    private final boolean twoPlayer;
    private int gameTicks = 0;
    
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
        
        Util.addAll( enemies, spawnStrategy.spawnEnemies() );

        for (Enemy enemy : enemies)
        {
            simulate(enemy);
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
        p.hazardCollisionCheck( enemies.iterator() );
        Util.addNotNull( occuringEvents, p.getEvent() );
        occuringEvents.addAll(
                p.coinCollectCheck(currentLevel.getCoins(), gameTicks) );
        Util.addNotNull( occuringEvents,
                p.springboardCheck(currentLevel.getSpringboards(), gameTicks) );
        Util.addNotNull( occuringEvents,
                p.fallingDeathCheck(levelBottom) );

        if (! p.isLevelCompleted() && currentLevel.getFinish() != null)
        {
            Util.addNotNull( occuringEvents, p.finishedLevelCheck(currentLevel.getFinish()) );
        }
    }
    
    private void simulate(Enemy enemy)
    {
        enemy.gameTick( gameTicks, currentLevel.getBlockArray() );
    }

    public List<Drawable> getDrawables()
    {
        drawables.clear();
        Util.addAll( drawables, currentLevel.getSpringboards() );
        drawables.add(player1);
        Util.addNotNull(drawables, player2);
        Util.addAll( drawables, enemies.iterator() );
        return drawables;
    }

    public void reset()
    {
        if (twoPlayer)
        {
            throw new IllegalStateException();
        }

        enemies.clear();
        currentLevel.resetEnemySpawners();
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

    private final class SinglePlayerSpawnStrategy implements SpawnStrategy
    {
        private Array<Enemy> list = new Array<Enemy>();

        @Override
        public Iterator<Enemy> spawnEnemies()
        {
            list.clear();
            Iterator<EnemySpawner> spawners = currentLevel.getSpawners();
            float playerX = player1.getXPosition();

            while ( spawners.hasNext() )
            {
                Util.addNotNull( list, spawners.next().spawn(playerX) );
            }
            return list.iterator();
        }
    }

}
