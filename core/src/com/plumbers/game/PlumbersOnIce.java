package com.plumbers.game;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;

public final class PlumbersOnIce
            extends ApplicationAdapter implements EventContext
{
	// major fields for game simulation and rendering
	private TextureAtlas textureAtlas;
	private GameModel gameModel;
	private Player player1;
	private SpriteBatch batch;
	private OrthogonalTiledMapRenderer mapRenderer;
	private Background background;
	private OrthographicCamera camera;
	
	// game viewport width and height
	private int width, height;
	
	// background color to be set by the level in lieu of an image Background
	private Color bgColor = Color.BLACK;
	
	// state fields needed by the View
	private float timeAccumulator = 0;
	private float elapsedTime = 0;
	private float cameraPos = 0;
	private List<Event> events = new ArrayList<Event>();
	private boolean death = false;
	
	// objects used for the on-screen timer and coin count displays
	private Matrix4 screenProjMatrix;
	private BitmapFont mainFont;
	private DecimalFormat secondsFormat;
	private Animation coinAnimation;
	
	/* -- music and sounds -- */
	// the background music track for the level
	private Music music;
	// time to wait after end of track before starting again
	private float musicDelay;
	// stores the elapsed time value at end of track
	private float musicEndTime;
	// whether we are currently in the delay between end and start of music
	private boolean musicWait = false;
	
	// sound effects
	private Sound coinSound, jumpSound, damageSound, deathSound;
	// frame number at time of last coin sound start,
	// used to provide minimum spacing between coin sounds
	private long coinFrameNumber; 
	
	private static final float GAME_TICK_TIME = 1/120f;
	private static final int TICK_PER_FRAME_RESET_THRESHOLD = 2,
	                         COIN_SOUND_MIN_DELAY_IN_FRAMES = 3,
	                         CAMERA_PLAYER_X = 300,
	                         INFO_PADDING_IN_PIXELS = 5;
	private static final float ON_DEATH_DELAY = 0.75f,
	                           MUSIC_VOLUME = 0.5f;
	
	private static final String TEXTURE_ATLAS_FILE = "sprites.atlas",
	                            FONT_FILE = "DejaVuSansMono-Bold.ttf";
	
	@Override
	public void create () {	
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		secondsFormat = new DecimalFormat("00.0");
		secondsFormat.setRoundingMode(RoundingMode.DOWN);
		FreeTypeFontGenerator generator =
		        new FreeTypeFontGenerator( Gdx.files.internal(FONT_FILE) );
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 28;
		parameter.borderWidth = 2;
		parameter.borderColor = Color.BLACK;
		parameter.flip = true;
		mainFont = generator.generateFont(parameter);
		generator.dispose();

		camera = new OrthographicCamera();
		camera.setToOrtho(true, width, height);
		camera.update();
		
		OrthographicCamera staticCamera = new OrthographicCamera();
		staticCamera.setToOrtho(true, width, height);
		staticCamera.update();
		screenProjMatrix = staticCamera.combined;
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		textureAtlas =
		    new TextureAtlas(Gdx.files.internal(TEXTURE_ATLAS_FILE), true);
		
		coinAnimation = Coin.getAnimation(textureAtlas);
		Coin.createCoinTile(textureAtlas);
		
		Level level;
		try {
			level = new Level("castle.tmx", textureAtlas);
		} catch (FileFormatException e) {
			e.printStackTrace();
			return;
		}
		
		
		if ( level.hasBackground() ) {
			background = level.getBackground();
			background.setWindowDimensions(width, height);
		} else if ( level.hasBackgroundColor() ) {
			bgColor = level.getBackgroundColor();
		}
		music = level.getSoundtrack();
		mapRenderer = level.getRenderer();
		mapRenderer.setView(camera);
		
		player1 = new Player("hero", textureAtlas);
		player1.setPosition( level.getStartPosition() );
		gameModel = new GameModel(level, player1);

		coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
		damageSound = Gdx.audio.newSound(Gdx.files.internal("hurt.wav"));
		deathSound = Gdx.audio.newSound(Gdx.files.internal("death.wav"));
		
		music.setOnCompletionListener( new MusicListener() );
		music.setVolume(MUSIC_VOLUME);
		music.play();
	}

	@Override
	public void render () {
		if (death) {
			deathLoop();
			return;
		}
		Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		float deltaTime = Gdx.graphics.getDeltaTime(); 
		elapsedTime += deltaTime;
		timeAccumulator += deltaTime;
		int count = 0;	
		
		while (timeAccumulator > GAME_TICK_TIME) {
			events.addAll( gameModel.gameTick() );
			++count;
			timeAccumulator -= GAME_TICK_TIME;
		}
		if (count > TICK_PER_FRAME_RESET_THRESHOLD) {
			timeAccumulator = 0;
		}
		
		for (Event e : events) {
			e.applyTo(this);
		}
		events.clear();
		
		musicCheck();
		positionCamera();
		
		if (background != null) {
			background.render( batch, MathUtils.round(cameraPos) );
		}
		mapRenderer.render();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		for (Drawable drawable : gameModel.getDrawables()) {
			drawable.draw(batch, elapsedTime);
		}
		
		batch.end();
		
		batch.setProjectionMatrix(screenProjMatrix);

		renderTimer();
		renderCoinCounter();
	}

	@Override
	public void apply(CoinEvent e) {
		long frameId = Gdx.graphics.getFrameId();
		
		if ( frameId - coinFrameNumber >= COIN_SOUND_MIN_DELAY_IN_FRAMES ) {
			coinSound.play();
			coinFrameNumber = frameId;
		}
	}
	
	@Override
	public void apply(DamageEvent e) {
		damageSound.play();
	}
	
	@Override
	public void apply(DeathEvent e) {
		death = true;
		elapsedTime = 0;
		timeAccumulator = 0;
		deathSound.play();
		music.stop();
	}

	@Override
	public void apply(JumpEvent e) {
		jumpSound.play(1);
	}
	
	private void deathLoop() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (elapsedTime < ON_DEATH_DELAY) {
			elapsedTime += Gdx.graphics.getDeltaTime();
		} else {
			reset();
			gameModel.reset();
			death = false;
		}
	}
	
	private void reset() {
		camera.translate(- cameraPos, 0);
		cameraPos = 0;
		camera.update();
		mapRenderer.setView(camera);
		batch.setProjectionMatrix(camera.combined);
		music.play();
	}
	
	private void positionCamera() {
		Vector position = player1.getPosition();
		
		if ( position.getX()
		        - CAMERA_PLAYER_X < gameModel.getLevelWidth() - width )
		{
			if ( position.getX() - CAMERA_PLAYER_X > cameraPos )
			{
				float change = MathUtils.floor(
				        position.getX() - cameraPos - CAMERA_PLAYER_X );
				
				camera.translate(change, 0);
				cameraPos += change;
				camera.update();
				mapRenderer.setView(camera);
			}
		}
		else if ( position.getX()
		            - CAMERA_PLAYER_X > gameModel.getLevelWidth() - width )
		{
			float change = (gameModel.getLevelWidth() - width) - cameraPos;
			
			camera.translate(change, 0);
			cameraPos += change;
			camera.update();
			mapRenderer.setView(camera);
		}
	}
	
	private void renderTimer() {
		batch.begin();
		String str = getTimerString();
		mainFont.draw(
		  batch,
		  str, width - 2 - str.length() * (mainFont.getSpaceWidth() + 2),
		  INFO_PADDING_IN_PIXELS );
		batch.end();
	}
	
	private String getTimerString() {
		int minutes = ((int) elapsedTime) / 60;
		return (minutes == 0 ? "" : minutes) + ":" + secondsFormat.format(elapsedTime % 60);
	}
	
	private void renderCoinCounter() {
		int p = INFO_PADDING_IN_PIXELS;
		
		batch.begin();
		batch.draw(coinAnimation.getKeyFrame(elapsedTime, true), p, p, 20, 20);
		mainFont.draw(batch, "" + player1.getCoinsCollected(), 33, p);
		batch.end();
	}
	
	private void musicCheck() {
		if (musicWait && elapsedTime > musicEndTime + musicDelay) {
			music.play();
			musicWait = false;
		}
	}
	
	private class MusicListener implements OnCompletionListener {
		@Override
		public void onCompletion(Music music) {
			musicEndTime = elapsedTime;
			musicWait = true;
		}
	}
	
}
