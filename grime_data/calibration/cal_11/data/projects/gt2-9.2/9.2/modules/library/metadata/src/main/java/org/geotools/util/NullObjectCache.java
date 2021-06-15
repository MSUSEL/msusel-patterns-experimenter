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
package org.geotools.util;

import java.util.Collections;
import java.util.Set;


/**
 * Null implementation for the {@link ObjectCache}. Used for cases where
 * caching is <strong>not</strong> desired.
 * 
 * @since 2.5
 * @version $Id$
 * @source $URL$
 * @author Cory Horner (Refractions Research)
 */
final class NullObjectCache implements ObjectCache {
    /**
     * The singleton instance.
     */
    public static final NullObjectCache INSTANCE = new NullObjectCache();

    /**
     * Do not allow instantiation of this class, since a singleton is enough.
     */
    private NullObjectCache() {
    }

    /**
     * Do nothing since this map is already empty.
     */
    public void clear() {
    }

    /**
     * Returns {@code null} since this map is empty.
     */
    public Object get(Object key) {
        return null;
    }

    /**
     * Returns {@code null} since this map is empty.
     */
    public Object peek( Object key ) {
        return null;
    }

    /**
     * Do nothing since this map does not cache anything.
     */
    public void put(Object key, Object object) {
    }

    /**
     * There is no cache, therefore a cache miss is a safe assumption.
     */
    public boolean containsKey(Object key) {
        return false;
    }

    /**
     * Do nothing since there is no write lock.
     */
    public void writeLock(Object key) {
    }

    /**
     * Do nothing since there is no write lock.
     */
    public void writeUnLock(Object key) {
    }
    
    /**
     * Return an empty set.
     */
    public Set<Object> getKeys(){
    	return Collections.emptySet();
    }
    
    /**
     * Do nothing since there is nothing to remove.
     */
    public void remove(Object key){
    	
    }
    
}
