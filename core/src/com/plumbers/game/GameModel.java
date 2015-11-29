package com.plumbers.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.utils.Pools;

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
		player1.fallingCheck( currentLevel.getBlocks() );
		player1.collisionCheck( currentLevel.getBlocks() );
		player1.hazardCollisionCheck( currentLevel.getHazards() );
		player1.hazardCollisionCheck(enemies);
		occuringEvents.addAll( player1.getEvents() );
		occuringEvents.addAll(
		      player1.coinCollectCheck( currentLevel.getCoins() ));
		
		if ( player1.fallingDeathCheck(512) ) {
			occuringEvents.add( new DeathEvent(player1) );
		}
		Vector playerPos = player1.getPosition();
		float playerX = playerPos.getX();
		Pools.free(playerPos);
		
		for (EnemySpawner spawner : currentLevel.getSpawners()) {
			addNotNull( enemies, spawner.spawn(playerX) );
		}
		
		for (Enemy enemy : enemies) {
			enemy.simulate(gameTicks);
			enemy.fallingCheck( currentLevel.getBlocks() );
			enemy.collisionCheck( currentLevel.getBlocks() );
		}
		
		return occuringEvents;
	}

	public Iterable<Drawable> getDrawables() {
		drawables.clear();
	    drawables.add(player1);
		drawables.addAll(enemies);
		return drawables;
	}
	
	public void reset() {
		for (EnemySpawner spawner : currentLevel.getSpawners()) {
			spawner.reset();
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
