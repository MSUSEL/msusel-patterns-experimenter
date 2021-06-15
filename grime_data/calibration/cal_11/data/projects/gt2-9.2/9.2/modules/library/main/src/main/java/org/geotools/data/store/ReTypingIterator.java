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
package org.geotools.data.store;

import java.util.Iterator;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * Iterator wrapper which re-types features on the fly based on a target 
 * feature type.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @deprecated Please use {@link ReTypingFeatureIterator}
 * @source $URL$
 */
public class ReTypingIterator implements Iterator<SimpleFeature> {

	/**
	 * The delegate iterator
	 */
	Iterator<SimpleFeature> delegate;
	
	/**
	 * The target feature type
	 */
	SimpleFeatureType target;
	
	/**
	 * The matching types from target 
	 */
	AttributeDescriptor[] types;
	
	SimpleFeatureBuilder builder;
	
	public ReTypingIterator( Iterator<SimpleFeature> delegate, SimpleFeatureType source, SimpleFeatureType target ) {
		this.delegate = delegate;
		this.target = target;
		types = typeAttributes( source, target );
		this.builder = new SimpleFeatureBuilder(target);
	}
	
	public Iterator<SimpleFeature> getDelegate() {
		return delegate;
	}

	public void remove() {
		delegate.remove();
	}

	public boolean hasNext() {
		return delegate.hasNext();
	}

	public SimpleFeature next() {
		SimpleFeature next = (SimpleFeature) delegate.next();
        String id = next.getID();

        try {
			for (int i = 0; i < types.length; i++) {
			    final String xpath = types[i].getLocalName();
			    builder.add(next.getAttribute(xpath));
			}
			
			return builder.buildFeature(id);
		} 
        catch (IllegalAttributeException e) {
        	throw new RuntimeException( e );
		}
	}
	
	 /**
     * Supplies mapping from origional to target FeatureType.
     * 
     * <p>
     * Will also ensure that origional can cover target
     * </p>
     *
     * @param target Desired FeatureType
     * @param origional Origional FeatureType
     *
     * @return Mapping from originoal to target FeatureType
     *
     * @throws IllegalArgumentException if unable to provide a mapping
     */
    protected AttributeDescriptor[] typeAttributes(SimpleFeatureType original,
        SimpleFeatureType target) {
        if (target.equals(original)) {
            throw new IllegalArgumentException(
                "FeatureReader allready produces contents with the correct schema");
        }

        if (target.getAttributeCount() > original.getAttributeCount()) {
            throw new IllegalArgumentException(
                "Unable to retype  FeatureReader<SimpleFeatureType, SimpleFeature> (origional does not cover requested type)");
        }

        String xpath;
        AttributeDescriptor[] types = new AttributeDescriptor[target.getAttributeCount()];

        for (int i = 0; i < target.getAttributeCount(); i++) {
            AttributeDescriptor attrib = target.getDescriptor(i);
            xpath = attrib.getLocalName();
            types[i] = attrib;

            if (!attrib.equals(original.getDescriptor(xpath))) {
                throw new IllegalArgumentException(
                    "Unable to retype  FeatureReader<SimpleFeatureType, SimpleFeature> (origional does not cover "
                    + xpath + ")");
            }
        }

        return types;
    }

}
