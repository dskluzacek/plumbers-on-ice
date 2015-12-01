package com.plumbers.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.plumbers.game.ui.PlumbersOnIceGame;

public class DesktopLauncher {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 853;
		config.height = 512;
		config.resizable = false;
		config.vSyncEnabled = true;
		config.samples = 2;
		
		new LwjglApplication(PlumbersOnIceGame.createDefaultInstance(), config);
	}
}
