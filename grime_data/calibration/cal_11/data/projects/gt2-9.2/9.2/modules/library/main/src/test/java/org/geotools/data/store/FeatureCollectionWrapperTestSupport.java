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

import junit.framework.TestCase;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 *
 * @source $URL$
 */
public class FeatureCollectionWrapperTestSupport extends TestCase {

	protected CoordinateReferenceSystem crs;
	protected DefaultFeatureCollection delegate;
	
	protected void setUp() throws Exception {
		crs = CRS.parseWKT( 
			"GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]" 
		);
		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		
		typeBuilder.setName( "test" );
		typeBuilder.setNamespaceURI( "test" );
		typeBuilder.setCRS( crs );
		typeBuilder.add( "defaultGeom", Point.class, crs );
		typeBuilder.add( "someAtt", Integer.class );
		typeBuilder.add( "otherGeom", LineString.class );
		typeBuilder.setDefaultGeometry( "defaultGeom" );
		
		SimpleFeatureType featureType = (SimpleFeatureType) typeBuilder.buildFeatureType();
		
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
		
		GeometryFactory gf = new GeometryFactory();
		delegate = new DefaultFeatureCollection( "test", featureType ){};
		
		double x = -140;
		double y = 45;
		final int features = 5;
		for ( int i = 0; i < features; i++ ) {
			Point point = gf.createPoint( new Coordinate( x+i, y+i ) );
			point.setUserData( crs );
			
			builder.add( point );
			builder.add( new Integer( i ) );
			
			LineString line = gf.createLineString( new Coordinate[] { new Coordinate( x+i, y+i ), new Coordinate( x+i+1, y+i+1 ) } );
			line.setUserData( crs );
			builder.add( line );
			
			delegate.add( builder.buildFeature( i + "" ) );
		}
		
		// add a feature with a null geometry
		builder.add( null );
        builder.add( new Integer( -1 ) );
        builder.add( null );
        
        delegate.add( builder.buildFeature( (features + 1) + "" ) );
	}
}
