package com.plumbers.game;

import java.util.HashMap;
import java.util.Map;

public enum EnemyType
{
    RED_GUY ("badguy1")
    {
        @Override public Enemy newInstance()
        {
            return new BasicEnemy(BasicEnemy.Type.BADGUY_1);
        }
    },
    BLUE_SPIKE_GUY ("badguy2")
    {
        @Override public Enemy newInstance()
        {
            return new BasicEnemy(BasicEnemy.Type.BADGUY_2);
        }
    };
    
    private final String name;
    private static Map<String, EnemyType> map;
    
    private EnemyType(String name)
    {
        this.name = name;
    }
    
    public abstract Enemy newInstance();
    
    public String getName()
    {
        return name;
    }
    
    public static EnemyType getByName(String typeName)
    {
        return map.get(typeName);
    }

    static
    {
        map = new HashMap<String, EnemyType>();

        for (EnemyType type : EnemyType.values())
        {
            map.put(type.getName(), type);
        }
    }
}
