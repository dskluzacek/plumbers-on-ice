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
import com.badlogic.gdx.math.MathUtils;

@SuppressWarnings("unused")
public class PlumbersOnIce extends ApplicationAdapter implements InputProcessor {
	private TextureAtlas textureAtlas;
	private GameModel gameModel;
	private Player player1;
	private SpriteBatch batch;
	private OrthogonalTiledMapRenderer mapRenderer;
	private Background background;
	private OrthographicCamera camera;
	
	private float timeAccumulator = 0;
	private float elapsedTime = 0;
	private float cameraPos = 0;
	private int width, height;
	private boolean death = false;
	
	@Override
	public void create () {	
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(true, width, height);
		camera.update();
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		textureAtlas =
				new TextureAtlas(Gdx.files.internal("sprites.atlas"), true);
		
		Coin.createCoinTile(textureAtlas);
		
		Level level = new Level(textureAtlas);
		background = new Background(level.getBackground(), 2, 0.25, width, height);
		mapRenderer = level.getRenderer();
		mapRenderer.setView(camera);
		
		player1 = new Player("hero", textureAtlas);
		player1.setPosition( new Vector(0, 256) );
		gameModel = new GameModel(level, player1);
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		if (death) {
//			Gdx.gl.glClearColor(0, 0, 0, 1);
//			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			if (elapsedTime < 0.75) {
				elapsedTime += Gdx.graphics.getDeltaTime();
				return;
			} else {
				camera.translate(- cameraPos, 0);
				cameraPos = 0;
				camera.update();
				mapRenderer.setView(camera);
				batch.setProjectionMatrix(camera.combined);
				
				gameModel.reset();
				player1.setPosition( new Vector(0, 256) );
				player1.setVelocity( new Vector(0, 0) );
				player1.setAcceleration( new Vector(0, GameModel.GRAVITY) );
				player1.setState(Character.State.FALLING);
				death = false;
			}
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		float deltaTime = Gdx.graphics.getDeltaTime(); 
		elapsedTime += deltaTime;
		timeAccumulator += deltaTime;
		
		boolean died = false;
		int count = 0;	
		
		while (timeAccumulator > 1/60f) {
			died = gameModel.gameTick();
			count++;
			timeAccumulator -= 1/60f;
			
		}
		if (count > 1)
			timeAccumulator = 0;
		
		if (died) {
			death = true;
			elapsedTime = 0;
			timeAccumulator = 0;
			died = false;
		}
		
		Vector position = player1.getPosition();
		
		if ( position.getX() - cameraPos > 350 ) {
			float change = position.getX() - cameraPos - 350;
			camera.translate(change, 0);
			cameraPos += change;
			camera.update();
			mapRenderer.setView(camera);
		}
		
		background.render( batch, MathUtils.round(cameraPos) );
		
		mapRenderer.render();
		
		batch.setProjectionMatrix(camera.combined);
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
