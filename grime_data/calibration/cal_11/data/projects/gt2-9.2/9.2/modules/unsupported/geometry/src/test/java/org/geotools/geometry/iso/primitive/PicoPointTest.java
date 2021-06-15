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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.aggregate.AggregateFactoryImpl;
import org.geotools.geometry.iso.complex.ComplexFactoryImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.io.CollectionFactoryMemoryImpl;
import org.geotools.geometry.iso.util.elem2D.Geo2DFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.opengis.geometry.Precision;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class PicoPointTest extends TestCase {
	
	/**
	 * Creates a pico container that knows about all the geom factories
	 * @param crs
	 * @return container
	 */
	protected PicoContainer container( CoordinateReferenceSystem crs ){
		
		DefaultPicoContainer container = new DefaultPicoContainer(); // parent
		
		// Teach Container about Factory Implementations we want to use
		container.registerComponentImplementation(PositionFactoryImpl.class);
		container.registerComponentImplementation(AggregateFactoryImpl.class);
		container.registerComponentImplementation(ComplexFactoryImpl.class);
		container.registerComponentImplementation(GeometryFactoryImpl.class);
		container.registerComponentImplementation(CollectionFactoryMemoryImpl.class);
		container.registerComponentImplementation(PrimitiveFactoryImpl.class);
		container.registerComponentImplementation(Geo2DFactory.class);
		
		// Teach Container about other dependacies needed
		container.registerComponentInstance( crs );
		Precision pr = new PrecisionModel();
		container.registerComponentInstance( pr );
		
		return container;		
	}
	
	protected void createPointAndTest(PicoContainer c, double[] ords) {
		
		// create point and position factories from pico container
		PositionFactoryImpl positionFactory = (PositionFactoryImpl) c.getComponentInstanceOfType( PositionFactory.class );
		PrimitiveFactoryImpl primitiveFactory = (PrimitiveFactoryImpl) c.getComponentInstanceOfType( PrimitiveFactory.class );
		assertSame( positionFactory.getCoordinateReferenceSystem(), primitiveFactory.getCoordinateReferenceSystem() );
		assertNotNull(positionFactory);
		assertNotNull(primitiveFactory);
		
		// create position and point
		DirectPosition here = positionFactory.createDirectPosition(ords);
		Point point = primitiveFactory.createPoint(here); 
		assertNotNull(here.getCoordinateReferenceSystem());
		assertNotNull(point.getCoordinateReferenceSystem());
		assertEquals(here.getCoordinateReferenceSystem(), point.getCoordinateReferenceSystem());
		assertEquals(here, point.getDirectPosition());
		assertEquals(here.hashCode(), point.getDirectPosition().hashCode());
		assertEquals(point.getCentroid(), here);
		
		// get ordinates out of point
		double[] ords2 = point.getCentroid().getCoordinate();
	}
	
	public void testWSG84Point(){
		
		// create 2D point
		PicoContainer c = container( DefaultGeographicCRS.WGS84 );
		assertNotNull(c);
		
		// Do actually test stuff
		double[] ords = { 48.44, -123.37 };
		createPointAndTest(c, ords);
	}
	
	public void testWSG843DPoint(){
		
		// create 3D point
		PicoContainer c = container( DefaultGeographicCRS.WGS84_3D );
		assertNotNull(c);
		
		// Do actually test stuff
		double[] ords = { 48.44, -123.37, 0.0 };
		createPointAndTest(c, ords);
	}
}
