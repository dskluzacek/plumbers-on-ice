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
    private Animation walkAnimation;
    private Animation idleAnimation, jumpAnimation;
    private Animation knockbackAnimation;
    private float elapsedTime = 0;
	private SpriteBatch batch;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	private MovementAnimation movementAnim;
	private Vector position = new Vector(0, 258);
	private Vector velocity = new Vector(0, 0);
	private Vector acceleration = new Vector(0, 0);
	private Character.State state = Character.State.STANDING;
	
	
	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(true, w, h);
		camera.update();
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		textureAtlas =
				new TextureAtlas(Gdx.files.internal("sprites.atlas"), true);

		TextureRegion[] walkFrames = new TextureRegion[] {
				textureAtlas.findRegion("hero-walk1"),
				textureAtlas.findRegion("hero-walk2"),
				textureAtlas.findRegion("hero-walk3") };
		
		TextureRegion[] idleFrames = new TextureRegion[] {
				textureAtlas.findRegion("hero-idle1"),
				textureAtlas.findRegion("hero-idle2"),
				textureAtlas.findRegion("hero-idle3") };

		walkAnimation = new Animation(1/8f, walkFrames);
		idleAnimation = new Animation(1/3f, idleFrames);
		jumpAnimation = new Animation( 1, new TextureRegion[]{ textureAtlas.findRegion("hero-jump") } );
		knockbackAnimation = new Animation( 1, new TextureRegion[]{ textureAtlas.findRegion("hero-knockback") } );
		
		movementAnim = new MovementAnimation(idleAnimation, walkAnimation, 5000, 300, jumpAnimation, null, null);

		TmxMapLoader.Parameters mapParams = new TmxMapLoader.Parameters();
		mapParams.flipY = false;

		TiledMap tiledMap = new TmxMapLoader().load("untitled.tmx", mapParams);
		
		for ( TiledMapTileSet tileset : tiledMap.getTileSets() ) {
			for (TiledMapTile tile : tileset) {
				tile.getTextureRegion().flip(false, true);
			}
		}
//		((TiledMapTileLayer) tiledMap.getLayers().get(1)).getCell(1, 1).setTile(null);
		
		mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 2);
		mapRenderer.setView(camera);

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mapRenderer.render();
		float deltaTime = Gdx.graphics.getDeltaTime(); 
		elapsedTime += Gdx.graphics.getDeltaTime();
		
		if ( state == Character.State.STANDING || state == Character.State.RUNNING ) {
			if ( Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) ) {
				state = Character.State.JUMPING;
				movementAnim.setAction(MovementAnimation.Action.JUMP);
				velocity.setY(-400);
				acceleration.setX(0);
				acceleration.setY(640);
			} else if ( Gdx.input.isKeyPressed(Input.Keys.SPACE) ) {
    			acceleration.setX(1200);
    			movementAnim.setAction(MovementAnimation.Action.RUN);
    			state = Character.State.RUNNING;
    		} else {
    			acceleration.setX(-1200);
    		}
		}
		velocity.add( acceleration.scalarProduct(deltaTime) );
		
		if (velocity.getX() > 300) {
			velocity.setX(300);
		} else if (velocity.getX() <= 0) {
			velocity.setX(0);
		}
		position.add( velocity.scalarProduct(deltaTime) );
		
		if (position.getY() > 258) {
			state = Character.State.RUNNING;
			movementAnim.setAction(MovementAnimation.Action.RUN);
			acceleration.setY(0);
			velocity.setY(0);
			position.setY(258);
		}
		
		if (velocity.getX() == 0 && velocity.getY() == 0) {
			movementAnim.setAction(MovementAnimation.Action.IDLE);
			state = Character.State.STANDING;
		}
		movementAnim.setHorizontalSpeed( velocity.getX() );
		
		batch.begin();
		batch.draw(movementAnim.getFrame(elapsedTime), position.getX(), position.getY(), 32, 32);
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
