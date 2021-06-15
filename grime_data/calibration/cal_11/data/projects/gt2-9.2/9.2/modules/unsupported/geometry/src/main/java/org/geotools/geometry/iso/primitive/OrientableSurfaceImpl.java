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

import java.util.Set;

import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.primitive.OrientablePrimitive;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 * OrientableSurface consists of a surface and an orientation inherited from
 * OrientablePrimitive. If the orientation is "+", then the OrientableSurface is
 * a Surface. If the orientation is "-", then the OrientableSurface is a
 * reference to a Surface with an upNormal that reverses the direction for this
 * OrientableSurface, the sense of "the top of the surface" (see 6.4.33.2).
 * 
 * OrientableSurface: {Orientation = "+" implies primitive = self};
 * {(Orientation = "-" and TransfiniteSet::contains(p : DirectPosition2D))
 * implies (primitive.upNormal(p) = - self.upNormal(p))};
 * 
 * @author Jackson Roehrig & Sanjay Jena
 *
 *
 *
 *
 * @source $URL$
 */
public abstract class OrientableSurfaceImpl extends OrientablePrimitiveImpl
		implements OrientablePrimitive {

	/**
	 * Constructor
	 * 
	 * @param crs
	 * 
	 */
	protected OrientableSurfaceImpl(CoordinateReferenceSystem crs) {
		super(crs);
	}

	/**
	 * @param crs
	 * @param containedPrimitive
	 * @param containingPrimitive
	 * @param complex
	 */
	protected OrientableSurfaceImpl(CoordinateReferenceSystem crs,
			Set<Primitive> containedPrimitive,
			Set<Primitive> containingPrimitive, Set<Complex> complex) {
		super(crs, containedPrimitive, containingPrimitive, complex);
	}

	// /**
	// * Sets the primitive
	// * (influences the orientation directly).
	// * @param primitive
	// */
	// protected void setPrimitive(Primitive primitive) {
	// super.setPrimitive(primitive);
	// if (this.equals(primitive)) {
	// super.setOrientation(OrientablePrimitive.POSITIVE);
	// } else {
	// super.setOrientation(OrientablePrimitive.NEGATIVE);
	// }
	// }

	// /**
	// * Sets the Boundary of the Orientable Surface
	// * @param Boundary
	// */
	// protected void setBoundary(SurfaceBoundary boundary) {
	// this.m_boundary = boundary;
	// }
	//	

	// /**
	// * The operation "boundary" specializes the boundary operation defined at
	// Object with the appropriate return
	// * type for OrientableSurface. It shall return the set of circular
	// sequences of OrientableCurve that limit the
	// * globelExtent of this Surface. These curves shall be organized into one
	// circular sequence of curves for each
	// * boundary component of the Surface.
	// *
	// * OrientableSurface::boundary(): SurfaceBoundary;
	// *
	// * In cases where "exterior" boundary is not well defined, all the rings
	// of the SurfaceBoundary shall be listed as
	// * "interior".
	// *
	// * NOTE The concept of exterior boundary for a surface is really only
	// valid in a 2-dimensional plane. A bounded cylinder has
	// * two boundary components, neither of which can logically be classified
	// as its exterior. Thus, in 3 dimensions, there is no valid
	// * definition of exterior that covers all cases.
	// *
	// * @return SurfaceBoundary
	// */
	// public Boundary boundary() {
	// return null;
	// }

}
