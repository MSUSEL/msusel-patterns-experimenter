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
package org.geotools.geometry.iso.complex;

import java.util.List;
import java.util.Set;

import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.SolidImpl;
import org.opengis.geometry.Boundary;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.complex.Complex;

/**
 * A CompositeSolid (Figure 30) shall be a Complex with all the geometric
 * properties of a solid. Essentially, a composite solid is a set of solids that
 * join in pairs on common boundary surfaces to form a single solid.
 * 
 * @author Jackson Roehrig & Sanjay Jena
 * 
 *
 *
 *
 *
 * @source $URL$
 */
public class CompositeSolidImpl extends CompositeImpl<SolidImpl> {
    private static final long serialVersionUID = -1998244006251493858L;

    /**
	 * The association role Composition::generator associates this
	 * CompositeSolid to the primitive Solids in its generating set, that is,
	 * the solids that form the core of this complex. CompositeSolid::generator :
	 * Set<Solid>
	 * 
	 * NOTE To get a full representation of the elements in the Complex, the
	 * Surfaces, Curves and Points on the boundary of the generator set if
	 * Solids would have to be added to the generator list.
	 * 
	 * The Solids generators will be passed through the super constructor and
	 * saved in the element ArrayList of the according Complex
	 * @param generator
	 */
	public CompositeSolidImpl(List<CurveImpl> generator) {
		super(generator);
	}

	/**
	 * The method <code>dimension</code> returns the inherent dimension of
	 * this Object, which is less than or equal to the coordinate dimension. The
	 * dimension of a collection of geometric objects is the largest dimension
	 * of any of its pieces. Points are 0-dimensional, curves are 1-dimensional,
	 * surfaces are 2-dimensional, and solids are 3-dimensional. Locally, the
	 * dimension of a geometric object at a point is the dimension of a local
	 * neighborhood of the point - that is the dimension of any coordinate
	 * neighborhood of the point. Dimension is unambiguously defined only for
	 * DirectPositions interior to this Object. If the passed DirectPosition2D
	 * is NULL, then the method returns the largest possible dimension for any
	 * DirectPosition2D in this Object.
	 * 
	 * @param point
	 *            a <code>DirectPosition2D</code> value
	 * @return an <code>int</code> value
	 */
	public int dimension(@SuppressWarnings("unused")
	final DirectPositionImpl point) {
		return 3;
	}

	@Override
	public Class getGeneratorClass() {
		return CompositeSolidImpl.class;
	}

	@Override
	public Set<Complex> createBoundary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompositeSolidImpl clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSimple() {
		// TODO Auto-generated method stub
		return false;
	}

	public List getGenerators() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boundary getBoundary() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getDimension(DirectPosition point) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Envelope getEnvelope() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectPosition getRepresentativePoint() {
		// TODO Auto-generated method stub
		return null;
	}

}
