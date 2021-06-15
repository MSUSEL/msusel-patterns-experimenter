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

import org.opengis.geometry.complex.CompositeCurve;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.CurveBoundary;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.PrimitiveBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 * OrientableCurve consists of a curve and an orientation inherited from
 * OrientablePrimitive. If the orientation is "+", then the OrientableCurve is a
 * Curve. If the orientation is "-", then the OrientableCurve is related to
 * another Curve with a parameterization that reverses the sense of the curve
 * traversal.
 * 
 * OrientableCurve: {Orientation = "+" implies primitive = self}; {Orientation =
 * "-" implies primitive.parameterization(length()-s) = parameterization(s)};
 * 
 * @author Jackson Roehrig & Sanjay Jena
 *
 *
 *
 *
 * @source $URL$
 */
public abstract class OrientableCurveImpl extends OrientablePrimitiveImpl
		implements OrientableCurve {

    public abstract CurveBoundary getBoundary();
    
	/**
	 * Constructor
	 * 
	 * @param crs
	 */
	protected OrientableCurveImpl(CoordinateReferenceSystem crs) {
		super(crs);
	}

	/*
	 * TODO fuer was brauchen wir diesen constructor ? (SJ) Curves enthalten
	 * keine primitives, und sind auch in keien enthalten; verwechslung mit
	 * complexes?!?!
	 * 
	 * @param factory
	 * @param containedPrimitive
	 * @param containingPrimitive
	 * @param complex
	 */
	// protected OrientableCurveImpl(FeatGeomFactoryImpl factory, Set<Primitive>
	// containedPrimitive,
	// Set<Primitive> containingPrimitive, Set<Complex> complex) {
	// super(factory, containedPrimitive,containingPrimitive,complex);
	// }

    public Curve getPrimitive() {
        return (Curve) super.getPrimitive();
    }

    public CompositeCurve getComposite() {
        return (CompositeCurve) super.getComposite();
    }

	/**
	 * Returns an array with two orientable primitives, whereas the first one is
	 * "this" object and the second one the field proxy
	 * 
	 * @return an array OrientablePrimitive[2] with the positive and the
	 *         negative orientable primitive
	 */
	public OrientableCurve[] getProxy() {
		return new OrientableCurve[] { this, (OrientableCurve) this.proxy };
	}

}
