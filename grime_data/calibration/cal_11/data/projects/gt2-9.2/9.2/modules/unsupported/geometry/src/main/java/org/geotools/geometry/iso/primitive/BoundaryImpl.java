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
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.primitive;

import org.geotools.geometry.iso.complex.ComplexImpl;
import org.opengis.geometry.Boundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 * The abstract root data type for all the data types used to represent the
 * boundary of geometric objects is Boundary (Figure 7). Any subclass of Object
 * will use a subclass of Boundary to represent its boundary through the
 * operation Object::boundary. By the nature of geometry, boundary objects are
 * cycles. Boundary: {isCycle() = TRUE}
 * 
 * @author Jackson Roehrig & Sanjay Jena
 *
 *
 *
 *
 * @source $URL$
 */
public abstract class BoundaryImpl extends ComplexImpl implements Boundary {

	/**
	 * @param crs
	 */
	public BoundaryImpl(CoordinateReferenceSystem crs) {
		super(crs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengis.geometry.coordinate.root.Geometry#isCycle()
	 */
	public boolean isCycle() {
		// implementation ok
		// Boundaries are always a cycle, because their boundary is empty
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.geotools.geometry.featgeom.root.GeometryImpl#getBoundary()
	 */
	public Boundary getBoundary() {
		// Wie telefonisch besprochen 4.Okt.2006 geben wir hier NULL zurueck
		// A boundary does not have a boundary. Thus, the Boundary of a Boundary is NULL.
		return null;
	}	
	
	
}
