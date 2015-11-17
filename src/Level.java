import java.util.List;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;
import java.io.*;

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
