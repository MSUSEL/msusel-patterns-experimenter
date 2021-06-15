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

import java.util.Collection;
import java.util.Iterator;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryType;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.BoundingBox;

/**
 * Temptative implementation of Feature.
 * <p>
 * NOTE this is work in progress and at this time not really being used throughout the library.
 * </p>
 * @author jdeolive
 * @author jgarnett
 *
 *
 *
 * @source $URL$
 */
public class FeatureImpl extends ComplexAttributeImpl implements Feature {

	/**
	 * Default geometry attribute
	 */
	GeometryAttribute defaultGeometry;

	/**
	 * Create a Feature with the following content.
	 * 
	 * @param properties Collectio of Properties (aka Attributes and/or Associations)
	 * @param desc Nested descriptor
	 * @param id Feature ID
	 */
	public FeatureImpl(Collection<Property> properties, AttributeDescriptor desc, FeatureId id) {
		super(properties, desc, id );
	}
	
	/**
	 * Create a Feature with the following content.
	 * 
	 * @param properties Collectio of Properties (aka Attributes and/or Associations)
	 * @param type Type of feature to be created
	 * @param id Feature ID
	 */
	public FeatureImpl(Collection<Property> properties, FeatureType type, FeatureId id ) {
		super(properties, type, id );
	}

	public FeatureType getType() {
	    return (FeatureType) super.getType();
	}
	public FeatureId getIdentifier() {
		return (FeatureId) this.id;
	}
	/**
	 * Get the total bounds of this feature which is calculated by doing a union
	 * of the bounds of each geometry this feature is associated with.
	 * 
	 * @return An Envelope containing the total bounds of this Feature.
	 * 
	 * @task REVISIT: what to return if there are no geometries in the feature?
	 *       For now we'll return a null envelope, make this part of interface?
	 *       (IanS - by OGC standards, all Feature must have geom)
	 */
	public BoundingBox getBounds() {

		ReferencedEnvelope bounds = new ReferencedEnvelope(getType().getCoordinateReferenceSystem());
		for (Iterator itr = getValue().iterator(); itr.hasNext();) {
			Property property = (Property) itr.next();
			if (property instanceof GeometryAttribute) {
				 bounds.include(((GeometryAttribute)property).getBounds());
			}
		}

		return bounds;
	}

	public GeometryAttribute getDefaultGeometryProperty() {
		if ( defaultGeometry != null ) {
			return defaultGeometry;
		}
		
		synchronized ( this ) {
			if ( defaultGeometry == null ) {
				//look it up from the type
				if (((FeatureType)getType()).getGeometryDescriptor() == null ) {
					return null;
				}
				
				GeometryType geometryType = 
				 	(GeometryType) getType().getGeometryDescriptor().getType();
				
				 if (geometryType != null) {
					 for (Iterator itr = getValue().iterator(); itr.hasNext();) {
						 Property property =  (Property) itr.next();
						 if (property instanceof GeometryAttribute) {
							 if (property.getType().equals(geometryType)) {
								 defaultGeometry = (GeometryAttribute)property;	 
								 break;
							 }
						 }
					 }
				 }
		
			}
		}
		
		return defaultGeometry;
	}

	//TODO: REVISIT
	//this implementation seems really bad to me or I am missing something:
	//1- getValue() shouldn't contain the passed in attribute, but the schema should contain its descriptor
	//2- this.defaultGeometry = defaultGeometry means getValue() will  not contain the argument
	public void setDefaultGeometryProperty(GeometryAttribute defaultGeometry) {
	    if (!getValue().contains(defaultGeometry)) {
	        throw new IllegalArgumentException("specified attribute is not one of: " + getValue());
	    }
	    
	    synchronized (this) {
            this.defaultGeometry = defaultGeometry; 
        }
	}	
	
}
