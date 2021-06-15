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
package org.geotools.geometry.iso.coordinate;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.iso.primitive.SurfaceBoundaryImpl;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.coordinate.Triangle;
import org.opengis.geometry.coordinate.TriangulatedSurface;

/**
 * 
 * A Triangle is a planar Polygon defined by 3 corners; that is, a Triangle
 * would be the result of a constructor of the form: Polygon(LineString(<P1,
 * P2, P3, P1>)) where P1, P2, and P3 are three Positions. Triangles have no
 * holes. Triangle shall be used to construct TriangulatedSurfaces.
 * 
 * NOTE The points in a triangle can be located in terms of their corner points
 * by defining a set of barycentric coordinates, three nonnegative numbers c1,
 * c2, and c3 such that c1+ c2 + c3 = 1.0. Then, each point P in the triangle
 * can be expressed for some set of barycentric coordinates as: P = c1 * P1 + c2 *
 * P2 + c3 * P3
 * 
 * @author Jackson Roehrig & Sanjay Jena
 * 
 */
/**
 * @author sanjay
 *
 *
 *
 *
 *
 * @source $URL$
 */
public class TriangleImpl extends PolygonImpl implements Triangle {

	TriangulatedSurface triangulatedSurface;

	/* The 3 Corners of the Triangle */
	private PositionImpl corner0;

	private PositionImpl corner1;

	private PositionImpl corner2;

	public TriangleImpl(SurfaceBoundaryImpl triangleBoundary,
			TriangulatedSurface ts, PositionImpl p1, PositionImpl p2,
			PositionImpl p3) {
		super(triangleBoundary);
		this.triangulatedSurface = ts;
		this.corner0 = p1;
		this.corner1 = p2;
		this.corner2 = p3;
	}

	public String toString() {
		return "[Triangle: " + corner0 + " | " + corner1 + " | " + corner2; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Returns the corners of the Triangle
	 * 
	 * @return Array of Position with three elements
	 */
	public List<Position> getCorners() {
		List<Position> rList = new ArrayList();
		rList.add(this.corner0);
		rList.add(this.corner1);
		rList.add(this.corner2);
		//return new PositionImpl[] { corner0, corner1, corner2 };
		return rList;
	}

	/**
	 * Returns first corner of the Triangle
	 * 
	 * @return first corner of the Triangle
	 */
	public PositionImpl getFirstCorner() {
		return this.corner0;
	}

	/**
	 * Returns second corner of the Triangle
	 * 
	 * @return second corner of the Triangle
	 */
	public PositionImpl getSecondCorner() {
		return this.corner1;
	}

	/**
	 * Returns third corner of the Triangle
	 * 
	 * @return third corner of the Triangle
	 */
	public PositionImpl getThirdCorner() {
		return this.corner2;
	}

    public TriangulatedSurface getSurface() {
        return (TriangulatedSurface) super.getSurface();
    }

	/**
	 * @param tsi
	 */
	public void setAssociatedSurface(TriangulatedSurfaceImpl tsi) {
		this.triangulatedSurface = tsi;
	}

}
