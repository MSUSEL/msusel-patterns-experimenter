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
package org.geotools.graph.build.feature;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.graph.build.line.LineStringGraphGenerator;
import org.geotools.graph.structure.Edge;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 * 
 *
 * @source $URL$
 */
public class FeatureGraphGeneratorTest extends TestCase {

	public void test() throws Exception {
		LineStringGraphGenerator lsgg = new LineStringGraphGenerator();
		FeatureGraphGenerator fgg = new FeatureGraphGenerator( new LineStringGraphGenerator() );
		
		LineString[] lines = lines();
		SimpleFeature[] features = features( lines );
		
		List el1 = new ArrayList();
		List el2 = new ArrayList();
		for ( int i = 0; i < lines.length; i++ ) {
			el1.add( lsgg.add( lines[i] ) );
			el2.add( fgg.add( features[i] ) );
		}
		
		for ( int i = 0; i < el1.size(); i++ ) {
			Edge e1 = (Edge) el1.get( i );
			Edge e2 = (Edge) el2.get( i );
			
			assertTrue( e1.getObject() instanceof LineString );
			assertTrue( e2.getObject() instanceof SimpleFeature );
			
			LineString line = (LineString) e1.getObject();
			SimpleFeature feature = (SimpleFeature) e2.getObject();
			
			assertEquals( line, feature.getDefaultGeometry() );
		}
	}
	
	LineString[] lines() {
		GeometryFactory gf = new GeometryFactory();
		LineString[] lines = new LineString[5];
		
		for ( int i = 0; i < lines.length; i++ ) {
			lines[i] = gf.createLineString(
				new Coordinate[]{
					new Coordinate( i, i+1), new Coordinate( i+2, i+3 )
				}
			);
		}
		
		return lines;
	}
	
	SimpleFeature[] features( LineString[] lines ) throws SchemaException, IllegalAttributeException {
		
		
		SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
		b.setName( "test");
		b.add("the_geom", LineString.class);
		b.add("id", Integer.class);
		SimpleFeatureType schema = b.buildFeatureType();
		SimpleFeature[] features = new SimpleFeature[ lines.length ];
		
		for ( int i = 0; i < lines.length; i++) {
			Integer id = new Integer(i);
			features[i] = SimpleFeatureBuilder.build(schema, new Object[] {lines[i], id}, "fid" + id.toString());
		}
		
		return features;
	}
	
	
}
