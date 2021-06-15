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
package org.geotools.renderer.lite;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.DataFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.strtree.STRtree;

/**
 * IndexedFeatureReader
 * 
 * @author wolf
 *
 *
 * @source $URL$
 */
public final class IndexedFeatureResults extends DataFeatureCollection {
	STRtree index = new STRtree();
	Envelope bounds;
	int count;
	private Envelope queryBounds;

	public IndexedFeatureResults(SimpleFeatureCollection results) throws IOException,
			IllegalAttributeException {
		// copy results attributes
		super(null,results.getSchema());
		
				
		// load features into the index
		SimpleFeatureIterator reader = null;
		bounds = new Envelope();
		count = 0;
		try {
			reader = results.features();
			SimpleFeature f;
			Envelope env;
			while (reader.hasNext()) {
				f = reader.next();
				env = ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal();
				bounds.expandToInclude(env);
				count++;
				index.insert(env, f);
			}
		} finally {
			if(reader != null)
				reader.close();
		}
	}

	/**
	 * @see org.geotools.data.FeatureResults#reader()
	 */
	public  FeatureReader<SimpleFeatureType, SimpleFeature> reader(Envelope envelope) throws IOException {
		List results = index.query(envelope);
		final Iterator resultsIterator = results.iterator();
		
		return new FeatureReader<SimpleFeatureType, SimpleFeature>() {
			/**
			 * @see org.geotools.data.FeatureReader#getFeatureType()
			 */
			public SimpleFeatureType getFeatureType() {
				return getSchema();
			}

			/**
			 * @see org.geotools.data.FeatureReader#next()
			 */
			public SimpleFeature next() throws IOException,
					IllegalAttributeException, NoSuchElementException {
				return (SimpleFeature) resultsIterator.next();
			}

			/**
			 * @see org.geotools.data.FeatureReader#hasNext()
			 */
			public boolean hasNext() throws IOException {
				return resultsIterator.hasNext();
			}

			/**
			 * @see org.geotools.data.FeatureReader#close()
			 */
			public void close() throws IOException {
			}
		};
	}

	/**
	 * @see org.geotools.data.FeatureResults#getBounds()
	 */
	public ReferencedEnvelope getBounds() {
		return ReferencedEnvelope.reference(bounds);
	}

	/**
	 * @see org.geotools.data.FeatureResults#getCount()
	 */
	public int getCount() throws IOException {
		return count;
	}

	/**
	 * @see org.geotools.data.FeatureResults#collection()
	 */
	public SimpleFeatureCollection collection() throws IOException {
	    DefaultFeatureCollection fc = new DefaultFeatureCollection();
		List<SimpleFeature> results = index.query(bounds);
		for (Iterator<SimpleFeature> it = results.iterator(); it.hasNext();) {
			fc.add(it.next());
		}
		return fc;
	}


	/**
	 * @see org.geotools.data.FeatureResults#reader()
	 */
	public  FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
		if(queryBounds != null)
			return reader(queryBounds);
		else
			return reader(bounds);
	}

	/**
	 * @param queryBounds an Envelope defining the boundary of the query
	 * 
	 */
	public void setQueryBounds(Envelope queryBounds) {
		this.queryBounds = queryBounds;
	}
}
