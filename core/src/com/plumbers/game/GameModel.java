package com.plumbers.game;

import java.util.ArrayList;
import java.util.List;

public class GameModel{
	private Player player1; 
//  private Player p2; 
	private Level currentLevel; 
//  private List<EnemySpawn> enemies; 
//  private Powerup powerups[]; 
//  private List<Event> occuringEvents; 
//  private List<Entity> entities;
	private int gameTicks = 0;
	
	public static final float GRAVITY = 0.3f;
	public static final int TILE_SIZE = 32;

	public GameModel(Level level, Player p1) {
		currentLevel = level; 
		player1 = p1; 
	}
	
//	public GameModel(Level level, Player p1, Player p2) {
//		player1 = p1; 
//		player2 = p2; 
//		currentLevel = level; 
//	}

	public boolean gameTick() {
		++gameTicks;
		player1.simulate();
		player1.fallingCheck( currentLevel.getBlocks() );
		player1.collisionCheck( currentLevel.getBlocks() );
		player1.coinCollectCheck( currentLevel.getCoins() );
		return player1.fallingDeathCheck(512);
	}

	public Iterable<Drawable> getDrawables() {
		List<Drawable> list = new ArrayList<Drawable>();
		list.add(player1);
		return list;
	}
	
	public void reset() {
		currentLevel.resetCoins();
	}
	
	public int getTickNumber() {
		return gameTicks;
	}
}
