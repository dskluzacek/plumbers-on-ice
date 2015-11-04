public class GameModel{
  private Player p1; 
  private Player p2; 
  private Level currentLevel; 
  private Engine engine; 
  private List<EnemySpawn> enemies; 
  private Coin coins[]; 
  private Powerup powerups[]; 
  private List<Event> occuringEvents; 
  private List<Entity> entities; 
  private int gameTicks; 
  
  public GameModel(Level level, Player p1){
    this.currentLevel = level; 
    this.p1 = p; 
  }
  public GameModel(Level level, Player p1, Player p2){
  this.p1 = p1; 
  this.p2 = p2; 
  this.currentLevel = level; 
  }
  
  public List<Event> gameTick(){
  
  }
  
  public List<Drawables> getDrawables(){
  
  }
}
