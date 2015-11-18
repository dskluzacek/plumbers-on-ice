package com.plumbers.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

@SuppressWarnings("unused")
public class PlumbersOnIce extends ApplicationAdapter implements InputProcessor {
	private TextureAtlas textureAtlas;
	private GameModel gameModel;
	private Player player1;
	private SpriteBatch batch;
	private OrthogonalTiledMapRenderer mapRenderer;
	private OrthographicCamera camera;
	
	private float timeAccumulator = 0;
	private float elapsedTime = 0;
	private float cameraPos = 0;
	
	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(true, w, h);
		camera.update();
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		textureAtlas =
				new TextureAtlas(Gdx.files.internal("sprites.atlas"), true);

		Level level = new Level();
		mapRenderer = level.getRenderer();
		mapRenderer.setView(camera);
		
		player1 = new Player("hero", textureAtlas);
		gameModel = new GameModel(level, player1);
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		float deltaTime = Gdx.graphics.getDeltaTime(); 
		elapsedTime += deltaTime;
		timeAccumulator += deltaTime;
		
		while (timeAccumulator > 1/60f) {
			gameModel.gameTick();
			timeAccumulator -= 1/60f;
		}
		
		Vector position = player1.getPosition();
		
		if ( position.getX() - cameraPos > 350 ) {
			float change = position.getX() - cameraPos - 350;
			camera.translate(change, 0);
			cameraPos += change;
			camera.update();
			mapRenderer.setView(camera);
			batch.setProjectionMatrix(camera.combined);
		}
		
		mapRenderer.render();
		batch.begin();
		
		for (Drawable drawable : gameModel.getDrawables()) {
			drawable.draw(batch, elapsedTime);
		}
		
		batch.end();
	}

	@Override
	public boolean keyDown(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
