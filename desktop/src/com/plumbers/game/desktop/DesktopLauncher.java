package com.plumbers.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.plumbers.game.GameView;
import com.plumbers.game.KeyboardController;

public class DesktopLauncher {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 853;
		config.height = 512;
		config.resizable = false;
		config.vSyncEnabled = false;
		
		if (args.length == 0) {
			System.out.println("No level file argument provided");
			return;
		}
		
		new LwjglApplication( new GameView(args[0], new ScreenViewport(),
				new KeyboardController()), config);
	}
}
