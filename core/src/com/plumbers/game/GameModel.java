package com.plumbers.game;

import java.util.ArrayList;
import java.util.List;

public class GameModel{
    private Player player1; 
//  private Player p2;
	private Level currentLevel;
	private List<Event> occuringEvents = new ArrayList<Event>(); 
//  private List<EnemySpawn> enemies; 
//  private Powerup powerups[]; 
//  private List<Entity> entities;
    private int gameTicks = 0;
	
	public static final float GRAVITY = 0.3f;
	public static final int TILE_SIZE = 32;

    public GameModel(Level level, Player p1) {
        currentLevel = level; 
        player1 = p1; 
    }
    
//  public GameModel(Level level, Player p1, Player p2) {
//      player1 = p1; 
//      player2 = p2; 
//      currentLevel = level; 
//  }

	public List<Event> gameTick() {
		++gameTicks;
		occuringEvents.clear();
		
		player1.simulate();
		occuringEvents.addAll( player1.getEvents() );
		player1.fallingCheck( currentLevel.getBlocks() );
		player1.collisionCheck( currentLevel.getBlocks() );
		occuringEvents.addAll(
		      player1.coinCollectCheck( currentLevel.getCoins() ));
		
		if ( player1.fallingDeathCheck(512) ) {
			occuringEvents.add( new DeathEvent(player1) );
		}
		return occuringEvents;
	}

    public Iterable<Drawable> getDrawables() {
        List<Drawable> list = new ArrayList<Drawable>();
        list.add(player1);
        return list;
    }
	
	public void reset() {
		currentLevel.resetCoins();
		gameTicks = 0;
		player1.reset( new Vector(0, 256) );
	}
	
	public int getTickNumber() {
		return gameTicks;
	}
	
	public int getLevelWidth() {
		return currentLevel.getWidthInTiles() * TILE_SIZE;
	}
	
	@SuppressWarnings("unused")
	private void addEventNotNull(Event e) {
		if (e != null) {
			occuringEvents.add(e);
		}
	}
}
