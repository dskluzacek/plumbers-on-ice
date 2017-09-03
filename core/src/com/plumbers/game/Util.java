package com.plumbers.game;

import java.util.Collection;
import java.util.Iterator;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

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
    
    public static <T> void addAll(Array<? super T> array, Iterator<T> iter)
    {
        while ( iter.hasNext() )
        {
            array.add( iter.next() );
        }
    }
    
    public static <T> void preFillPool(Pool<T> objectPool, int count)
    {
        Array<T> array = new Array<T>(count);
        
        for (int n = 0; n < count; n++)
        {
            array.add( objectPool.obtain() );
        }
        objectPool.freeAll(array);
    }
}
