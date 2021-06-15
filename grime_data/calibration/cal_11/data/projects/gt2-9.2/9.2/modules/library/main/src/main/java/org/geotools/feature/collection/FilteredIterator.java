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

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.filter.Filter;

/**
 * Provides an implementation of Iterator that will filter
 * contents using the provided filter.
 * <p>
 * This is a *Generic* iterator not limited to Feature, this
 * will become more interesting as Filter is able to evaulate
 * itself with more things then just Features.
 * </p>
 * <p>
 * This also explains the use of Collection (where you may
 * have expected a FeatureCollection). However
 * <code>FeatureCollectoin.close( iterator )</code> will be
 * called on the internal delgate.
 * </p>
 *  
 * @author Jody Garnett, Refractions Research, Inc.
 *
 *
 * @source $URL$
 */
public class FilteredIterator<F extends Feature> implements Iterator<F>, FeatureIterator<F> {
	private Iterator<F> delegate;
	private Filter filter;

	private F next;
	
	public FilteredIterator(Iterator<F> iterator, Filter filter) {
		this.delegate = iterator;
		this.filter = filter;
	}
	
	public FilteredIterator(Collection<F> collection, Filter filter) {
		this.delegate = collection.iterator();
		this.filter = filter;
		next = getNext();
	}
	
	/** Package protected, please use SubFeatureCollection.close( iterator ) */
	public void close(){
		if( delegate instanceof FeatureIterator ){
		    ((FeatureIterator<?>)delegate).close();
		}
		delegate = null;
		filter = null;
		next = null;
	}
	
	private F getNext() {
		F item = null;
		while (delegate.hasNext()) {
			item = delegate.next();
			if (filter.evaluate( item )){
				return item;
			}
		}
		return null;
	}

	public boolean hasNext() {
		return next != null;
	}

	public F next() {
		if(next == null){
			throw new NoSuchElementException();
		}
		F current = next;
		next = getNext();
		return current;
	}

	public void remove() {
		if( delegate == null ) throw new IllegalStateException();
		
	    delegate.remove();
	}
}
