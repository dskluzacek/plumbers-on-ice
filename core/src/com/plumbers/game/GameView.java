package com.plumbers.game;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.viewport.Viewport;
import com.plumbers.game.server.*;

public final class GameView implements Screen {
	/* -- major fields for game simulation and rendering -- */
	private GameModel gameModel;
	private EventContext eventContext;
	private Level level;
	private Player player1;
	private RemotePlayer player2;
    private TextureAtlas textureAtlas;
	private SpriteBatch batch;
	private OrthogonalTiledMapRenderer mapRenderer;
	private Background background;
	private Viewport viewport;
	private Controller controller;
	private OrthographicCamera camera;
	
	/** true if this is a two-player network game */
	private final boolean twoPlayerMode;
	/** connection to the multiplayer server */
	private GameConnection connection;
	
	/** background color that can be set by 
	 *  the level in lieu of an image Background */
	private Color bgColor = Color.BLACK;
	
	/* -- state fields needed by the View -- */
	private float timeAccumulator = 0;
	private float elapsedTime = 0;
	private float cameraPos = 0;
	private List<Event> events = new ArrayList<Event>();
	
	/** true when we are in the pause between player death and reset */
	private boolean death = false;
	
	/* -- objects used for the on-screen timer and coin count displays -- */
	private Matrix4 screenProjMatrix;
	private BitmapFont mainFont;
	private Animation coinAnimation;
	private final StringBuilder builder = new StringBuilder();
	
	/* -- music -- */
	/** the background music track for the level */
	private Music music;
	/** the music volume, from 0 to 1 */
	private final float musicVolume;
	/** time to wait after end of track before starting again, in seconds */
	private int musicDelay;
	/** stores the elapsedTime value when the track ends */
	private float musicEndTime;
	/** whether we are currently in the delay between end and start of music */
	private boolean musicWait = false;
	
	/* -- sound effects -- */
	/** sound of a coin being collected */
	private Sound coinSound;
	/** sound when the player jumps */
	private Sound jumpSound;
	/** sound when the player is hurt by an enemy or hazard */
	private Sound damageSound;
	/** sound after the player falls off the bottom of the screen */
	private Sound deathSound;
	/** frame number at time of last coin sound start,
	 *  used to provide minimum spacing between coin sounds */
	private long coinFrameNumber; 
	
	/** the level file filename or path */
	private String levelFilePath;
	/** the name of the character to use for player1 */
    private String player1CharacterName;
    /** the name of the character to use for player2 */
    private String player2CharacterName;
	
	private static final float GAME_TICK_TIME = 1/120f;
	private static final int TICK_PER_FRAME_RESET_THRESHOLD = 3;
	private static final int COIN_SOUND_MIN_DELAY_IN_FRAMES = 3;
	private static final int CAMERA_PLAYER_X = 300;
	private static final int INFO_PADDING_IN_PIXELS = 5;
	private static final float ON_DEATH_DELAY = 0.75f;
	
	private static final String TEXTURE_ATLAS_FILE = "sprites.atlas";
	private static final String FONT_FILE = "DejaVuSansMono-Bold.ttf";
	private static final String JUMP_SOUND_FILE = "jump.wav";
	private static final String COIN_SOUND_FILE = "coin.wav";
	private static final String DAMAGE_SOUND_FILE = "hurt.wav";
	private static final String DEATH_SOUND_FILE = "death.wav";
	
    /** game viewport dimensions in game units */
	public static final int VIRTUAL_WIDTH = 853,
	                        VIRTUAL_HEIGHT = 512;  
	
	/** Create a single-player GameView */
	public GameView(String levelFilePath,
	                String player1CharacterName,
	                Viewport viewport,
	                Controller controller,
	                float musicVolume)
	{
	    this.levelFilePath = levelFilePath;
	    this.player1CharacterName = player1CharacterName;
	    this.viewport = viewport;
	    this.controller = controller;
	    this.musicVolume = musicVolume;
	    
	    eventContext = new SinglePlayerEventContext();
	    twoPlayerMode = false;
	    player2CharacterName = null;
	}
	
	/** Create a two-player GameView */
	public GameView(GameConnection connection,
	                String player1CharacterName,
	                Viewport viewport,
	                Controller ctrl,
	                float musicVolume)
	{
	    this.player1CharacterName = player1CharacterName;
	    this.viewport = viewport;
	    this.controller = ctrl;
	    this.musicVolume = musicVolume;
	    this.connection = connection;
	    
	    eventContext = new TwoPlayerEventContext();
	    twoPlayerMode = true;
	}
	
	/**
	 * Loads needed assets, constructs objects, and initializes fields;
	 * to be called right before GameView becomes the active Screen. 
	 */
	public void load() {
	    if (twoPlayerMode) {
	        levelFilePath = connection.getLevelFileName();
	        player2CharacterName = connection.getOppCharacterName();
	    }
	    
	    FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator( Gdx.files.internal(FONT_FILE) );
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 28;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.BLACK;
        parameter.flip = true;
        mainFont = generator.generateFont(parameter);
        generator.dispose();
        
        textureAtlas =
                new TextureAtlas(Gdx.files.internal(TEXTURE_ATLAS_FILE), true);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        
        coinAnimation = Coin.getAnimation(textureAtlas);
        Coin.createCoinTile(textureAtlas);
        Enemy.setTextureAtlas(textureAtlas);
        
        try {
            level = new Level(levelFilePath, textureAtlas);
        } catch (FileFormatException e) {
            e.printStackTrace();
            return;
        }
        
        if ( level.hasBackground() ) {
            background = level.getBackground();
            background.setWindowDimensions(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        } else if ( level.hasBackgroundColor() ) {
            bgColor = level.getBackgroundColor();
        }
        music = level.getSoundtrack();
        musicDelay = level.getSoundtrackDelay();
        mapRenderer = level.getRenderer();
        
        player1 = new Player(player1CharacterName, textureAtlas, controller);
        player1.setPosition( level.getStartPosition() );
        
        if (twoPlayerMode) {
            player2 = new RemotePlayer(player2CharacterName, textureAtlas);
            player2.setPosition( level.getStartPosition() );
            gameModel = new GameModel(level, player1, player2, null);
        } else {
            gameModel = new GameModel(level, player1);
        }
        connection.setPlayers(player2, player1);
        player1.set2PlayerMode(true);
        player1.setGameConnection(connection);
        player2.setGameConnection(connection);

        coinSound = Gdx.audio.newSound(Gdx.files.internal(COIN_SOUND_FILE));
        jumpSound = Gdx.audio.newSound(Gdx.files.internal(JUMP_SOUND_FILE));
        damageSound = Gdx.audio.newSound(Gdx.files.internal(DAMAGE_SOUND_FILE));
        deathSound = Gdx.audio.newSound(Gdx.files.internal(DEATH_SOUND_FILE));
        
        music.setOnCompletionListener( new MusicListener() );
        music.setVolume(musicVolume);
	}
	
	/**
	 * Called by the Game when GameView becomes the active Screen.
	 */
	@Override
	public void show() {
		camera.setToOrtho(true, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		viewport.setCamera(camera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		screenProjMatrix = new Matrix4(camera.combined);
		mapRenderer.setView(camera);
		
		Gdx.input.setInputProcessor(controller);
		music.play();
	}
	
	/**
	 * Our "game loop",
	 * called by the application automatically to render each frame.
	 */
	@Override
	public void render(float deltaTime) {
		if (death) {
			deathLoop();
			return;
		}
		Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		elapsedTime += deltaTime;
		timeAccumulator += deltaTime;
		int count = 0;	
		
		while (timeAccumulator > GAME_TICK_TIME) {
			events.addAll( gameModel.gameTick() );
			++count;
			timeAccumulator -= GAME_TICK_TIME;
		}
		if (count >= TICK_PER_FRAME_RESET_THRESHOLD) {
			timeAccumulator = 0;
		}
		
		for (int i = 0; i < events.size(); i++) {
			events.get(i).applyTo(eventContext);
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
		
		List<Drawable> drawables = gameModel.getDrawables();
		
		for (int i = 0; i < drawables.size(); i++) {
			drawables.get(i).draw(batch, elapsedTime);
		}
		
		batch.end();
		
		batch.setProjectionMatrix(screenProjMatrix);
		batch.begin();
		renderTimer();
		renderCoinCounter();
		batch.end();
		
//		logger.log();
	}
//	FPSLogger logger = new FPSLogger();
	
	/** contains the behavior for events during a single player game */
	private class SinglePlayerEventContext implements EventContext {
    	@Override
    	public void apply(CoinEvent e) {
    		long frameId = Gdx.graphics.getFrameId();
    		
    		if ( frameId - coinFrameNumber >= COIN_SOUND_MIN_DELAY_IN_FRAMES ) {
    			coinSound.play();
    			coinFrameNumber = frameId;
    		}
    		
    		// this is not ideal but it keeps things simpler
    		if (! twoPlayerMode) {
    		    player1.incrementCoins();
    		}
    	}
    	
    	@Override
    	public void apply(DamageEvent e) {
    		damageSound.play();
    	}
    	
    	@Override
    	public void apply(DeathEvent e) {
    	    if (death) {
    	        return;
    	    }
    	    
    		death = true;
    		elapsedTime = 0;
    		timeAccumulator = 0;
    		deathSound.play();
    		music.stop();
    	}
    
    	@Override
    	public void apply(JumpEvent e) {
    		jumpSound.play();
    	}
	}
	
	/** the EventContext during a two-player game */
	private class TwoPlayerEventContext extends SinglePlayerEventContext {
        @Override
        public void apply(DamageEvent e) { 
            if ( e.getPlayerNum() == 1 ) {
                super.apply(e);
                
                EventMessage m = EventMessage.obtain();
                m.damaged(gameModel.getTickNumber(),
                          player1.getXPosition(), player1.getYPosition());
                connection.enqueue(m);
            }
        }

        @Override
        public void apply(DeathEvent e) {
            if ( e.getPlayerNum() == 1 && ! death ) {
                death = true;
                timeAccumulator = 0;
                deathSound.play(0.8f);
                
                EventMessage m = EventMessage.obtain();
                m.died( gameModel.getTickNumber() );
                connection.enqueue(m);
            }
        }

        @Override
        public void apply(JumpEvent e) {
            if ( e.getPlayerNum() == 1 ) {
                super.apply(e);
                
            } else {
                float dist = Math.abs(player1.getXPosition() - player2.getXPosition());
                float volume = (VIRTUAL_WIDTH - dist) / VIRTUAL_WIDTH;
                
                if (volume > 0.1f) {
                    jumpSound.play(volume);
                }
            }
        }
        
	}
	
	/** render() delegates to this while player is dead */
	private void deathLoop() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if (elapsedTime < ON_DEATH_DELAY) {
            elapsedTime += Gdx.graphics.getDeltaTime();
        } else {
            reset();
            
            if (twoPlayerMode) {
                player1.reset( level.getStartPosition() );
            } else {
                gameModel.reset();
            }
            System.gc();
            death = false;
        }
    }
	
	/** 
	 * Resets the View to the beginning of the level
	 * and starts the music track.
	 */
	private void reset() {
		camera.translate(- cameraPos, 0);
		cameraPos = 0;
		camera.update();
		mapRenderer.setView(camera);
		batch.setProjectionMatrix(camera.combined);
		music.play();
	}
	
	/**
	 * Repositions the camera if needed based on player position.
	 */
	private void positionCamera() {
	    float playerX = player1.getXPosition();
	    
		if ( playerX
		        - CAMERA_PLAYER_X < gameModel.getLevelWidth() - VIRTUAL_WIDTH )
		{
			if ( playerX - CAMERA_PLAYER_X > cameraPos )
			{
				float change = MathUtils.floor(
				    playerX - cameraPos - CAMERA_PLAYER_X );
				
				camera.translate(change, 0);
				cameraPos += change;
				camera.update();
				mapRenderer.setView(camera);
			}
		}
		else if ( playerX
		            - CAMERA_PLAYER_X > gameModel.getLevelWidth() - VIRTUAL_WIDTH )
		{
			float change = (gameModel.getLevelWidth() - VIRTUAL_WIDTH) - cameraPos;
			
			camera.translate(change, 0);
			cameraPos += change;
			camera.update();
			mapRenderer.setView(camera);
		}
	}
	
	/** renders the on-screen timer */
	private void renderTimer() {
		CharSequence str = getTimerString();
		mainFont.draw(
		  batch,
		  str, VIRTUAL_WIDTH - 2 - str.length() * (mainFont.getSpaceWidth() + 2),
		  INFO_PADDING_IN_PIXELS );
	}
	
	/** formats elapsed time as minutes, seconds, and tenths of a second */
	private CharSequence getTimerString() {
	    builder.setLength(0);
	    
	    int minutes = ((int) elapsedTime) / 60;
	    float seconds = elapsedTime % 60.0f;
	    
	    if (minutes > 0) { builder.append(minutes); }
	    builder.append(':');
	    if (seconds < 10) { builder.append('0'); }
	    builder.append(seconds);
	    int index = builder.lastIndexOf("."); // find the decimal point,
	    this.builder.setLength(index + 2); // round down to the nearest tenth
	    return builder;
	}
	
	/** renders the on-screen coin counter */
	private void renderCoinCounter() {
		int p = INFO_PADDING_IN_PIXELS;
		builder.setLength(0);
		
		batch.draw(coinAnimation.getKeyFrame(elapsedTime, true), p, p, 20, 20);
		mainFont.draw(batch, builder.append(player1.getCoinsCollected()), 33, p);
	}
	
	/** start the music again yet? */
	private void musicCheck() {
		if (musicWait && elapsedTime > musicEndTime + musicDelay) {
			music.play();
			musicWait = false;
		}
	}
	
	/** listener class to detect the end of the music track */
	private class MusicListener implements OnCompletionListener {
		@Override
		public void onCompletion(Music music) {
			musicEndTime = elapsedTime;
			musicWait = true;
		}
	}

	/** This is called at the start, and when the window is resized,
	 *  which currently we don't allow to happen.
	 *  (and probably never will on Android)
	 */
    @Override
    public void resize(int width, int height) {
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        screenProjMatrix = new Matrix4(camera.combined);
        background.setWindowDimensions(width, height);
    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void resume() {
        music.play();
    }

    @Override
    public void hide() {
        music.stop();
    }

    /** release resources needing dispose() */
    @Override
    public void dispose() {
        music.dispose();
        coinSound.dispose();
        jumpSound.dispose();
        damageSound.dispose();
        deathSound.dispose();
        mapRenderer.dispose();
        mainFont.dispose();
        batch.dispose();
        textureAtlas.dispose();
    }
	
}
