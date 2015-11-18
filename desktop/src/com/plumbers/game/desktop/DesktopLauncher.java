package com.plumbers.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.plumbers.game.PlumbersOnIce;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 853;
		config.height = 512;
		config.resizable = false;
		
		new LwjglApplication(new PlumbersOnIce(), config);
	}
}
