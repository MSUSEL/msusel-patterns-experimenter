/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.renderer3d.utils;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * Used to hold removed items that can be re-used later.  A kind of own memory re-cycling.
 * The items are referenced to with weak references, so they can be garbage collected if they are not used from elsewhere.
 * The factory passed to the constructor is used to create new items if there are none in the pool.
 *
 * @author Hans H�ggstr�m
 */
public final class Pool<T>
{

    //======================================================================
    // Private Fields

    private final LinkedList<WeakReference<T>> myPool = new LinkedList<WeakReference<T>>();
    private final PoolItemFactory<T> myFactory;

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    /**
     * Creates a new Pool instance.
     * Will return null with getItem if there are no items in the pool.
     */
    public Pool()
    {
        this( null );
    }


    /**
     * Creates a new Pool instance.
     *
     * @param factory a factory to create items with if the pool is empty.
     *                If null, null will be returned instead from getItem when the pool is empty.
     */
    public Pool( final PoolItemFactory<T> factory )
    {
        myFactory = factory;
    }

    //----------------------------------------------------------------------
    // Other Public Methods

    /**
     * @return an item from the pool, or a new item created with the factory supplied in the constructor.
     */
    public synchronized T getItem()
    {
        T item = null;

        // Find a free item from the pool, removing any garbage collected items on the way
        while ( !myPool.isEmpty() && item == null )
        {
            final WeakReference<T> weakReference = myPool.removeFirst();
            item = weakReference.get();
        }

        if ( item == null && myFactory != null )
        {
            // No free item found in the pool, create one using the factory.
            item = myFactory.create();
        }

        return item;
    }


    /**
     * @param item an item to add to the pool for potential reuse or garbage collection.
     */
    public synchronized void addItem( T item )
    {
        myPool.addFirst( new WeakReference<T>( item ) );

        System.out.println( "myPool.size() = " + myPool.size() );
    }

}
