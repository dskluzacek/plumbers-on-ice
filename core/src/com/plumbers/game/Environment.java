package com.plumbers.game;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public enum Environment
{
    GRASSLAND ("grassland", null)
    {
        @Override
        public Background createBackground()
        {
            return new Background.GrasslandBackground( new Texture("grassland.png") );
        }
    },
    WINTER ("winter", null)
    {
        @Override
        public Background createBackground()
        {
            return new Background.WinterBackground( new Texture("winter.png") );
        }
    },
    TROPICAL ("tropical", null)
    {
        @Override
        public Background createBackground()
        {
            return new Background.TropicalBackground( new Texture("tropical.png") );
        }
    },
    AUTUMN ("autumn", null)
    {
        @Override
        public Background createBackground()
        {
            return new Background.AutumnBackground( new Texture("autumn.png") );
        }
    },
    DUNGEON ("dungeon", new Color(20/255f, 12/255f, 28/255f, 1))
    {   
        @Override
        public Background createBackground()
        {
            return null;
        }
    };
    
    private final String name;
    private final Color defaultBackgroundColor;
    
    private static Map<String, Environment> map;
    
    private Environment(String name, Color defaultBackgroundColor)
    {
        this.name = name;
        this.defaultBackgroundColor = defaultBackgroundColor;
    }
    
    public abstract Background createBackground();
    
    public final String getName()
    {
        return name;
    }
    
    public final Color getDefaultBackgroundColor()
    {
        return defaultBackgroundColor;
    }
    
    public static Environment getByName(String name)
    {
        return map.get(name);
    }
    
    static
    {
        map = new HashMap<String, Environment>();

        for (Environment env : values())
        {
            map.put(env.name, env);
        }
    }
}
