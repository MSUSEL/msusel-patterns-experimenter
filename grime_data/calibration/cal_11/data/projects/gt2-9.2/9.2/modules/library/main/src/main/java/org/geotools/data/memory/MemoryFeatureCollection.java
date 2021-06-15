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
package org.geotools.data.memory;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.collection.AbstractFeatureCollection;
import org.geotools.feature.collection.RandomFeatureAccess;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Implement a SimpleFeatureCollection by burning memory!
 * <p>
 * Contents are maintained in a sorted TreeMap by FID, this serves as a reference implementation
 * when exploring the SimpleFeatureCollection api.
 * </p>
 * <p>
 * This is similar to DefaultFeatureCollection, although additional methods are supported and test
 * cases have been written. Unlike DefaultFeatureCollection the type information must be known at
 * construction time.
 * </p>
 * 
 * @author Jody Garnett, Refractions Research
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/library/main/src/main/java/org/geotools/
 *         data/memory/MemoryFeatureCollection.java $
 */
public class MemoryFeatureCollection extends AbstractFeatureCollection implements
        RandomFeatureAccess, Collection<SimpleFeature> {
    TreeMap<String,SimpleFeature> contents = new TreeMap<String,SimpleFeature>();

    public MemoryFeatureCollection(SimpleFeatureType schema) {
        super(schema);
    }

    public boolean add(SimpleFeature o) {
        SimpleFeature feature = (SimpleFeature) o;
        contents.put(feature.getID(), feature);
        return true;
    }

    public int size() {
        return contents.size();
    }

    public MemoryIterator openIterator() {
        return new MemoryIterator(contents.values().iterator());
    }

    class MemoryIterator implements Iterator<SimpleFeature>, SimpleFeatureIterator {
        Iterator<SimpleFeature> it;

        MemoryIterator(Iterator<SimpleFeature> iterator) {
            it = iterator;
        }

        public void close() {
            it = null;
        }

        public boolean hasNext() {
            if (it == null) {
                throw new IllegalStateException();
            }
            return it.hasNext();
        }

        public SimpleFeature next() {
            if (it == null) {
                throw new IllegalStateException();
            }
            return it.next();
        }

        public void remove() {
            it.remove();
        }
    }

    //
    // RandomFeatureAccess
    //
    public SimpleFeature getFeatureMember(String id) throws NoSuchElementException {
        if (contents.containsKey(id)) {
            return (SimpleFeature) contents.get(id);
        }
        throw new NoSuchElementException(id);
    }

    public SimpleFeature removeFeatureMember(String id) {
        if (contents.containsKey(id)) {
            SimpleFeature old = (SimpleFeature) contents.get(id);
            contents.remove(id);
            return old;
        }
        return null;
    };

    /**
     * Calculates the bounds of the features without caching.
     */
    @Override
    public ReferencedEnvelope getBounds() {
        return DataUtilities.bounds( features() );
    }

	@Override
	public boolean remove(Object o) {
		return contents.values().remove(o);
	}

	@Override
	public boolean addAll(Collection<? extends SimpleFeature> c) {
		boolean changed = false;
		for( SimpleFeature feature : c ){
			boolean added = add( feature );
			if( !changed && added ){
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return contents.values().removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return contents.values().retainAll(c);
	}

	@Override
	public void clear() {
		contents.clear();
	}

}
