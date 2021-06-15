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
package org.geotools.renderer.shape;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

import junit.framework.TestCase;

import org.geotools.data.shapefile.shp.ShapeType;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Tests MultiPointShape class
 * 
 * @author jeichar
 * @since 2.1.x
 *
 *
 *
 * @source $URL$
 */
public class MultiPointShapeTest extends TestCase {

	/**
	 * Class under test for PathIterator getPathIterator(AffineTransform)
	 */
	public void testGetPathIteratorAffineTransform() {
		double[] coord1=new double[]{0.0,0.0};
		double[] coord2=new double[]{0.0,15.0};
		double[] coord3=new double[]{15.0,15.0};
		double[] coord4=new double[]{15.0,0.0};
		double[] coord5=new double[]{0.0,10.0};
		double[] coord6=new double[]{10.0,15.0};
		double[][]coords=new double[][]{coord1,coord2,coord3,coord4,coord5,coord6};

		SimpleGeometry geom=new SimpleGeometry(ShapeType.MULTIPOINT, new double[][]{coord1},new Envelope( 0,15,0,15));
		
		MultiPointShape shape=new MultiPointShape(geom);

		PathIterator i=shape.getPathIterator(new AffineTransform());
		double[] tmp=new double[6];
		int result=i.currentSegment(tmp);
		assertFalse( i.isDone() );
		assertEquals(coord1[0],tmp[0], 0.001);
		assertEquals(coord1[1],tmp[1], 0.001);
		assertEquals(PathIterator.SEG_MOVETO, result);
		
		i.next();

		assertTrue( i.isDone() );
		
		geom=new SimpleGeometry(ShapeType.MULTIPOINT, coords,new Envelope( 0,15,0,15));
		
		shape=new MultiPointShape(geom);
		
		i=shape.getPathIterator(new AffineTransform());
		tmp=new double[6];
		result=i.currentSegment(tmp);
		assertFalse( i.isDone() );
		assertEquals(coord1[0],tmp[0], 0.001);
		assertEquals(coord1[1],tmp[1], 0.001);
		assertEquals(PathIterator.SEG_MOVETO, result);
		
		i.next();

		tmp=new double[6];
		result=i.currentSegment(tmp);
		
		assertFalse( i.isDone() );
		assertEquals(coord2[0],tmp[0], 0.001);
		assertEquals(coord2[1],tmp[1], 0.001);
		assertEquals(PathIterator.SEG_MOVETO, result);

		i.next();

		tmp=new double[6];
		result=i.currentSegment(tmp);
		
		assertFalse( i.isDone() );
		assertEquals(coord3[0],tmp[0], 0.001);
		assertEquals(coord3[1],tmp[1], 0.001);
		assertEquals(PathIterator.SEG_MOVETO, result);

		i.next();

		tmp=new double[6];
		result=i.currentSegment(tmp);
		
		assertFalse( i.isDone() );
		assertEquals(coord4[0],tmp[0], 0.001);
		assertEquals(coord4[1],tmp[1], 0.001);
		assertEquals(PathIterator.SEG_MOVETO, result);

		i.next();

		tmp=new double[6];
		result=i.currentSegment(tmp);
		
		assertFalse( i.isDone() );
		assertEquals(coord5[0],tmp[0], 0.001);
		assertEquals(coord5[1],tmp[1], 0.001);
		assertEquals(PathIterator.SEG_MOVETO, result);

		i.next();

		tmp=new double[6];
		result=i.currentSegment(tmp);
		
		assertFalse( i.isDone() );
		assertEquals(coord6[0],tmp[0], 0.001);
		assertEquals(coord6[1],tmp[1], 0.001);
		assertEquals(PathIterator.SEG_MOVETO, result);

		i.next();
		assertTrue( i.isDone() );
	}
	

}
