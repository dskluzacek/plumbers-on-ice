package com.plumbers.game.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.plumbers.game.GameView;
import com.plumbers.game.TouchscreenController;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useWakelock = true;
		config.numSamples = 2;
		
		initialize( new GameView("castle.tmx", new FitViewport(853, 512),
				new TouchscreenController(1280) ), config);
	}
}
