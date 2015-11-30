package com.plumbers.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GameModel {
	private Player player1; 
//  private Player p2; 
	private Level currentLevel;
	private List<Event> occuringEvents = new ArrayList<Event>(); 
	private List<Enemy> enemies = new ArrayList<Enemy>(); 
	private List<Drawable> drawables = new ArrayList<Drawable>();
	private int gameTicks = 0;
	
	public static final float GRAVITY = 0.1f;

	public GameModel(Level level, Player p1) {
		currentLevel = level; 
		player1 = p1; 
	}
	
//	public GameModel(Level level, Player p1, Player p2) {
//		player1 = p1; 
//		player2 = p2; 
//		currentLevel = level; 
//	}

	public List<Event> gameTick() {
		++gameTicks;
		occuringEvents.clear();
		
		player1.simulate(gameTicks);
		
		if ( currentLevel.useCeiling() ) {
			player1.ceilingCheck();
		}
		player1.fallingCheck( currentLevel.getBlockArray() );
		player1.collisionCheck( currentLevel.getBlockArray() );
		player1.hazardCollisionCheck( currentLevel.getHazards() );
		player1.hazardCollisionCheck(enemies);
		occuringEvents.addAll( player1.getEvents() );
		occuringEvents.addAll(
		      player1.coinCollectCheck( currentLevel.getCoins() ));
		
		if ( player1.fallingDeathCheck(512) ) {
			occuringEvents.add( new DeathEvent(player1) );
		}
		float playerX = player1.getXPosition();
		
		List<EnemySpawner> spawners = currentLevel.getSpawners();
		
		for (int i = 0; i < spawners.size(); i++) {
			addNotNull( enemies, spawners.get(i).spawn(playerX) );
		}
		
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
		    enemy.simulate(gameTicks);
			enemy.fallingCheck( currentLevel.getBlockArray() );
			enemy.collisionCheck( currentLevel.getBlockArray() );
		}
		
		return occuringEvents;
	}

	public List<Drawable> getDrawables() {
		drawables.clear();
	    drawables.add(player1);
		drawables.addAll(enemies);
		return drawables;
	}
	
	public void reset() {
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
}
