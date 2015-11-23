package com.plumbers.game;

import java.util.ArrayList;
import java.util.List;

public class GameModel{
    private Player player1; 
//  private Player p2; 
    private Level currentLevel; 
//  private Engine engine; 
//  private List<EnemySpawn> enemies; 
//  private Coin coins[]; 
//  private Powerup powerups[]; 
//  private List<Event> occuringEvents; 
//  private List<Entity> entities; 
    private int gameTicks = 0;

    public GameModel(Level level, Player p1) {
        currentLevel = level; 
        player1 = p1; 
    }
    
//  public GameModel(Level level, Player p1, Player p2) {
//      player1 = p1; 
//      player2 = p2; 
//      currentLevel = level; 
//  }

    public /*List<Event>*/ void gameTick() {
        ++gameTicks;
        player1.simulate();
        player1.fallingCheck( currentLevel.getBlocks() );
        player1.collisionCheck( currentLevel.getBlocks() );
    }

    public Iterable<Drawable> getDrawables() {
        List<Drawable> list = new ArrayList<Drawable>();
        list.add(player1);
        return list;
    }
}
