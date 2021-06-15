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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.collection;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * A feature iterator that completely delegates to a normal
 * Iterator, simply allowing Java 1.4 code to escape the caste (sic)
 * system.
 * <p>
 * This implementation checks the iterator to see if it implements
 * {@link Closeable} in order to allow for collections that
 * make use of system resources.
 * </p>
 * @author Jody Garnett, Refractions Research, Inc.
 *
 *
 * @source $URL$
 */
public class DelegateFeatureIterator<F extends Feature> implements FeatureIterator<F> {
	Iterator<F> delegate;
	/**
	 * Wrap the provided iterator up as a FeatureIterator.
	 * 
	 * @param iterator Iterator to be used as a delegate.
	 */
	public DelegateFeatureIterator( Iterator<F> iterator ){
		delegate = iterator;
	}
	
	/**
	 * Wrap the provided iterator up as a FeatureIterator.
	 * 
	 * @param iterator Iterator to be used as a delegate.
	 * @deprecated collection no longer used
	 */
	public DelegateFeatureIterator( FeatureCollection<? extends FeatureType, F> collection, Iterator<F> iterator ){
		delegate = iterator;
	}
	public boolean hasNext() {
		return delegate != null && delegate.hasNext();
	}
	public F next() throws NoSuchElementException {
		if( delegate == null ) throw new NoSuchElementException();
		return  delegate.next();
	}
	public void close() {
	    DataUtilities.close( delegate );
	    delegate = null;		
	}
}
