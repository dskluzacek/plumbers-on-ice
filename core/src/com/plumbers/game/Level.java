import java.util.List;

public class Level { 
  private List<Block> blocks; 
  private List<Decoration> decorations; 
  private List<EnemySpawn> enemies; 
  private Background bg; 
  private Soundtrack music; 
  
  public List<Block> getBlocks(){
    return blocks; 
  }
  
  public List<Decoration> getDecoration(){
    return decorations; 
  }

  
  public List<EnemySpawn> getEnemies(){
    return enemies; 
  }
  
  public Background getBackground(){
    return bg; 
  }
  
  public Soundtrack getSoundtrack(){
    return music; 
  }
  
}
