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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import org.opengis.feature.simple.SimpleFeature;

/**
 * An Index is built up around a FeatureCollection, using one of the 
 * attributes in the SimpleFeatureCollection as a comparable reference.
 * <p> 
 * An object in a column can be any object, but must either be a java
 * base-type Object (Integer, String, Character, etc.) or implement
 * Comparable.
 * <p>
 * An Index built on such a column will sort its array of object
 * references using FeatureComparator. Implement this to perform more
 * complex Index building.
 *
 * @author Ray Gallagher
 * @author Ian Schneider
 *
 *
 * @source $URL$
 * @version $Id$
 */
public interface FeatureIndex extends CollectionListener {
    /** Gets an "in order" Iterator of the Features as indexed.
     * @return An Iterator of the Features within this index.
     */
    java.util.Iterator getFeatures();
    
    /** Find all the Features within this index using a key.
     * @return A java.util.Collection containing the matches. May be empty.
     * @throws IllegalArgumentException If the key is incompatable with this index.
     * @param key A key to look up the Features with.
     */    
    java.util.Collection find(Object key) throws IllegalArgumentException;
    
    /** Find the first Feature using the given key.
     * @return A Feature, or null if none is found.
     * @throws IllegalArgumentException If the key is incompatable with this index.
     * @param key A key to look up the Feature with.
     */    
    SimpleFeature findFirst(Object key) throws IllegalArgumentException;
}
