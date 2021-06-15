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
package org.geotools.feature.simple;

import java.util.Collections;

import junit.framework.TestCase;

import org.geotools.feature.NameImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.feature.type.SchemaImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Schema;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Point;

/**
 * 
 *
 * @source $URL$
 */
public class SimpleTypeBuilderTest extends TestCase {

	static final String URI = "gopher://localhost/test";
	
	SimpleFeatureTypeBuilder builder;
	
	protected void setUp() throws Exception {
		Schema schema = new SchemaImpl( "test" );
		
		FeatureTypeFactoryImpl typeFactory = new FeatureTypeFactoryImpl();
		AttributeType pointType = 
			typeFactory.createGeometryType( new NameImpl( "test", "pointType" ), Point.class, null, false, false, Collections.EMPTY_LIST, null, null);		
		schema.put( new NameImpl( "test", "pointType" ), pointType );
		
		AttributeType intType = 
			typeFactory.createAttributeType( new NameImpl( "test", "intType" ), Integer.class, false, false, Collections.EMPTY_LIST, null, null);
		schema.put( new NameImpl( "test", "intType" ), intType );
		
		builder = new SimpleFeatureTypeBuilder( new FeatureTypeFactoryImpl() );
		builder.setBindings(schema);
	}
	
	public void testSanity() {
		builder.setName( "testName" );
		builder.setNamespaceURI( "testNamespaceURI" );
		builder.add( "point", Point.class, (CoordinateReferenceSystem) null );
		builder.add( "integer", Integer.class );
		
		SimpleFeatureType type = builder.buildFeatureType();
		assertNotNull( type );
		
		assertEquals( 2, type.getAttributeCount() );
		
		AttributeType t = type.getType( "point" );
		assertNotNull( t );
		assertEquals( Point.class, t.getBinding() );
		
		t = type.getType( "integer" );
		assertNotNull( t );
		assertEquals( Integer.class, t.getBinding() );
		
		t = type.getGeometryDescriptor().getType();
		assertNotNull( t );
		assertEquals( Point.class, t.getBinding() );
	}
	
	public void testCRS() {
		builder.setName( "testName" );
		builder.setNamespaceURI( "testNamespaceURI" );
		
		builder.setCRS(DefaultGeographicCRS.WGS84);
		builder.crs(null).add( "point", Point.class );
		builder.add( "point2", Point.class, DefaultGeographicCRS.WGS84 );
		builder.setDefaultGeometry("point");
		SimpleFeatureType type = builder.buildFeatureType();
		assertEquals( DefaultGeographicCRS.WGS84, type.getCoordinateReferenceSystem() );
		
		assertNull( type.getGeometryDescriptor().getType().getCoordinateReferenceSystem() );
		assertEquals( DefaultGeographicCRS.WGS84, ((GeometryType)type.getType("point2")).getCoordinateReferenceSystem());
	}
	
	
	public void testAttributeDefaultValue() {
	    SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
	    builder.setName("buggy");
	    builder.nillable(false).defaultValue(12).add("attrWithDefault", Integer.class);
	    builder.nillable(true).defaultValue(null).add("attrWithoutDefault", Integer.class);
	    SimpleFeatureType featureType = builder.buildFeatureType();
	    assertFalse(featureType.getDescriptor("attrWithDefault").isNillable());
	    assertEquals(12, featureType.getDescriptor("attrWithDefault").getDefaultValue());
	    assertTrue(featureType.getDescriptor("attrWithoutDefault").isNillable());
	    assertNull(featureType.getDescriptor("attrWithoutDefault").getDefaultValue());
	}
}
