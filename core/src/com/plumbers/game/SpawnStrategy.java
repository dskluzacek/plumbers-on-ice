package com.plumbers.game;

import java.util.Iterator;

public interface SpawnStrategy
{
    Iterator<Enemy> spawnEnemies();
}
