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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.opengis.feature.simple.SimpleFeature;

/**
 * This iterator is used to indicate that contents could not be aquired.
 * <p>
 * The normal Collection.iterator() method does not let us return an error
 * (we always have to return an iterator). However Iterator.next() can
 * be used to return an NoSuchElementException.
 * </p>
 * <p>
 * So we are basically going to lie, we are going to pretend their is content
 * *once*, and when they ask for it we are going to hit them with
 * a NoSuchElementExcetion. This is a mean trick, but it does convey the idea
 * of asking for content that is supposed to be there and failing to aquire it.
 * </p>
 * @author jgarnett
 * @since 2.1.RC0
 *
 *
 * @source $URL$
 */
public class NoContentIterator implements Iterator<SimpleFeature> {
    Throwable origionalProblem;
    public NoContentIterator( Throwable t ){
        origionalProblem = t;
    }
    public boolean hasNext() {
        return origionalProblem != null;
    }
    public SimpleFeature next() {
        if( origionalProblem == null ){
            // you only get the real error on the first offense
            // (after that you are just silly)
            //
            throw new NoSuchElementException();            
        }
        NoSuchElementException cantFind = new NoSuchElementException( "Could not aquire feature:" + origionalProblem );
        cantFind.initCause( origionalProblem );
        origionalProblem = null;
        throw cantFind;
    }

    public void remove() {
        if( origionalProblem == null ){
            // user did not call next first
            throw new UnsupportedOperationException();
        }
        // User did not call next first
        throw new IllegalStateException();        
    }
}
