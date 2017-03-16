package com.plumbers.game;

import java.util.Collection;
import java.util.Iterator;
import com.badlogic.gdx.utils.Array;

public final class Util
{
    private Util()
    {
    }
    
    public static <T> void addNotNull(Collection<? super T> c, T obj)
    {
        if (obj != null)
        {
            c.add(obj);
        }
    }
    
    public static <T> void addNotNull(Array<? super T> c, T obj)
    {
        if (obj != null)
        {
            c.add(obj);
        }
    }
    
    public static <T> void addAll(Collection<? super T> collection, Iterator<T> iter)
    {
        while ( iter.hasNext() )
        {
            collection.add( iter.next() );
        }
    }
    
    public static <T> void addAll(Array<? super T> collection, Iterator<T> iter)
    {
        while ( iter.hasNext() )
        {
            collection.add( iter.next() );
        }
    }
}
