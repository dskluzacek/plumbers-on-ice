package com.plumbers.game;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;

import sun.print.resources.serviceui;

@SuppressWarnings("unused")
public class PlumbersOnIce extends ApplicationAdapter implements EventContext {
	private TextureAtlas textureAtlas;
	private GameModel gameModel;
	private Player player1;
	private SpriteBatch batch;
	private OrthogonalTiledMapRenderer mapRenderer;
	private Background background;
	private OrthographicCamera camera;
	private int width, height;
	
	private float timeAccumulator = 0;
	private float elapsedTime = 0;
	private float cameraPos = 0;
	private List<Event> events = new ArrayList<Event>();
	private boolean death = false;
	
	private BitmapFont mainFont;
	private DecimalFormat secondsFormat;
	private Animation coinAnimation;
	
	private Music music;
	private float musicEndTime;
	private boolean musicWait = false;
	private Sound coinSound, jumpSound, deathSound;
	private long coinFrameNumber;
	
	private static final int MUSIC_DELAY = 2;
	private static final float MUSIC_VOLUME = 0.5f;
	
	@Override
	public void create () {	
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		secondsFormat = new DecimalFormat("00.0");
		secondsFormat.setRoundingMode(RoundingMode.DOWN);
		FreeTypeFontGenerator generator= new FreeTypeFontGenerator(
		                     Gdx.files.internal("DejaVuSansMono-Bold.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 28;
		parameter.flip = true;
		mainFont = generator.generateFont(parameter);
		generator.dispose();

		camera = new OrthographicCamera();
		camera.setToOrtho(true, width, height);
		camera.update();
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		textureAtlas =
				new TextureAtlas(Gdx.files.internal("sprites.atlas"), true);
		
		coinAnimation = Coin.getAnimation(textureAtlas);
		Coin.createCoinTile(textureAtlas);
		
		Level level = new Level(textureAtlas);
		background = new Background(level.getBackground(), 2, 0.125, width, height);
		mapRenderer = level.getRenderer();
		mapRenderer.setView(camera);
		
		player1 = new Player("hero", textureAtlas);
		player1.setPosition( new Vector(0, 256) );
		gameModel = new GameModel(level, player1);

		coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
		deathSound = Gdx.audio.newSound(Gdx.files.internal("death.wav"));
		music = Gdx.audio.newMusic(Gdx.files.internal("Grasslands Theme.mp3"));
		music.setOnCompletionListener( new MusicListener() );
		music.setVolume(MUSIC_VOLUME);
//		music.play();
//		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (death) {
			if (elapsedTime < 0.75) {
				elapsedTime += Gdx.graphics.getDeltaTime();
				return;
			} else {
				reset();
				gameModel.reset();
				death = false;
			}
		}
		
		float deltaTime = Gdx.graphics.getDeltaTime(); 
		elapsedTime += deltaTime;
		timeAccumulator += deltaTime;
		int count = 0;	
		
		while (timeAccumulator > 1/60f) {
			events.addAll( gameModel.gameTick() );
			++count;
			timeAccumulator -= 1/60f;
		}
		if (count > 1) {
			timeAccumulator = 0;
		}
		
		for (Event e : events) {
			e.applyTo(this);
		}
		events.clear();
		
		musicCheck();
		positionCamera();
		
		background.render( batch, MathUtils.round(cameraPos) );
		renderTimer();
		renderCoinCounter();
		mapRenderer.render();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		for (Drawable drawable : gameModel.getDrawables()) {
			drawable.draw(batch, elapsedTime);
		}
		batch.end();
	}

	@Override
	public void apply(CoinEvent e) {
		long frameId = Gdx.graphics.getFrameId();
		
		if ( frameId - coinFrameNumber >= 3 ) {
			coinSound.play();
			coinFrameNumber = frameId;
		}
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
	
	private void reset() {
		camera.translate(- cameraPos, 0);
		cameraPos = 0;
		camera.update();
		mapRenderer.setView(camera);
		batch.setProjectionMatrix(camera.combined);
//		music.play();
	}
	
	private void positionCamera() {
		Vector position = player1.getPosition();
		
		if ( position.getX() - 350 < gameModel.getLevelWidth() - width ) {
			if ( position.getX() - 350 > cameraPos ) {
				float change = position.getX() - cameraPos - 350;
				
				camera.translate(change, 0);
				cameraPos += change;
				camera.update();
				mapRenderer.setView(camera);
			}
		} else if ( position.getX() - 350 > gameModel.getLevelWidth() - width ) {
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
		mainFont.draw(batch, str, width - 5 - str.length() * mainFont.getSpaceWidth(), 5);
		batch.end();
	}
	
	private String getTimerString() {
		int minutes = ((int) elapsedTime) / 60;
		return (minutes == 0 ? "" : minutes) + ":" + secondsFormat.format(elapsedTime % 60);
	}
	
	private void renderCoinCounter() {
		batch.begin();
		batch.draw( coinAnimation.getKeyFrame(elapsedTime, true), 5, 5, 20, 20 );
		mainFont.draw(batch, "" + player1.getCoinsCollected(), 33, 5);
		batch.end();
	}
	
	private void musicCheck() {
		if (musicWait && elapsedTime > musicEndTime + MUSIC_DELAY) {
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
