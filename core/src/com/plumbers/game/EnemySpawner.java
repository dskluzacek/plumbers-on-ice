package com.plumbers.game;

import com.plumbers.game.Enemy.Type;

public class EnemySpawner {
	private final int x, y;
	private final Enemy.Type type;
	private final int spawnDistance;
	private boolean spawned = false;
	private static final int DEFAULT_DISTANCE = 900;

	public EnemySpawner(int x, int y, Type type) {
		this.x = x;
		this.y = y;
		this.type = type;

		spawnDistance = DEFAULT_DISTANCE; 
	}

	public EnemySpawner(int x, int y, Type type, int spawnDistance) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.spawnDistance = spawnDistance;
	}

	public Enemy spawn(float playerXPos) {
		if (spawned || playerXPos + spawnDistance < x) {
			return null;
		}
		spawned = true;
		return new Enemy(type, x, y);
	}
	
	public void reset() {
		spawned = false;
	}
}
