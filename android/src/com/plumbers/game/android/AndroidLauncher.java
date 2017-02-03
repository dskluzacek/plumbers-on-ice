package com.plumbers.game.android;

import android.os.Bundle;
import android.util.DisplayMetrics;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.plumbers.game.ui.PlumbersOnIceGame;

/**
 * Entry point for the Android app.
 */
public class AndroidLauncher extends AndroidApplication
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        initialize( PlumbersOnIceGame.createAndroidInstance(metrics.widthPixels), config );
    }
}
