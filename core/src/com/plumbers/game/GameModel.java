package com.plumbers.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GameModel {
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
    private boolean finished = false;

    public static final float GRAVITY = 0.1f;

    public GameModel(Level level, Player p1) {
        currentLevel = level;
        levelBottom = level.getHeightInTiles() * Block.SIZE;
        player1 = p1;
        player2 = null;
        twoPlayer = false;
        spawnStrategy = new SinglePlayerSpawner();
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
        } else {
            this.spawnStrategy = new SinglePlayerSpawner();
        }
    }

    public List<Event> gameTick() {
        ++gameTicks;
        occuringEvents.clear();

        simulate(player1);

        if (twoPlayer) {
            simulate(player2);
        }
        enemies.addAll( spawnStrategy.spawnEnemies() );

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.simulate(gameTicks);
            enemy.fallingCheck( currentLevel.getBlockArray() );
            enemy.collisionCheck( currentLevel.getBlockArray() );
        }

        return occuringEvents;
    }

    private void simulate(Player p) {
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
                p.coinCollectCheck( currentLevel.getCoins(), gameTicks ));

        if ( p.fallingDeathCheck(levelBottom) ) {
            if (p == player1)
                occuringEvents.add( DeathEvent.playerOneInstance() );
            else
                occuringEvents.add( DeathEvent.playerTwoInstance() );
        }

        if (finished) {
            return;
        }

        if ( currentLevel.getFinish() != null
            && p.getRectangle().intersects(currentLevel.getFinish()) ) {
            if (p == player1) {
                player1.finished();
                occuringEvents.add( FinishEvent.playerOneInstance() );
                finished = true;
            }
        }

    }

    public List<Drawable> getDrawables() {
        drawables.clear();
        drawables.add(player1);
        addNotNull(drawables, player2);
        drawables.addAll(enemies);
        return drawables;
    }

    public void reset() {
        if (twoPlayer) {
            throw new IllegalStateException();
        }

        List<EnemySpawner> spawners = currentLevel.getSpawners();

        for (int i = 0; i < spawners.size(); i++) {
            spawners.get(i).reset();
        }

        enemies.clear();
        currentLevel.resetCoins();
        gameTicks = 0;
        player1.reset( currentLevel.getStartPosition() );
    }

    public int getTickNumber() {
        return gameTicks;
    }

    public int getLevelWidth() {
        return currentLevel.getWidthInTiles() * Block.SIZE;
    }

    private static <T> void addNotNull(Collection<T> c, T obj) {
        if (obj != null) {
            c.add(obj);
        }
    }

    private class SinglePlayerSpawner implements SpawnStrategy {
        private List<Enemy> list = new ArrayList<Enemy>();

        @Override
        public List<Enemy> spawnEnemies() {
            list.clear();
            List<EnemySpawner> spawners = currentLevel.getSpawners();
            float playerX = player1.getXPosition();

            for (int i = 0; i < spawners.size(); i++) {
                addNotNull( list, spawners.get(i).spawn(playerX) );
            }
            return list;
        }
    }

}
