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
package org.geotools.data;

import java.io.IOException;

import junit.framework.TestCase;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 *
 * @source $URL$
 */
public class DiffWriterTest extends TestCase {

	DiffFeatureWriter writer;
	private Point geom;
	private SimpleFeatureType type;
	
	protected void setUp() throws Exception {
        type = DataUtilities.createType("default", "name:String,*geom:Geometry");
        GeometryFactory fac=new GeometryFactory();
        geom = fac.createPoint(new Coordinate(10,10));

        Diff diff=new Diff();
		diff.add("1", SimpleFeatureBuilder.build(type, new Object[]{ "diff1", geom }, "1"));
		diff.modify("original", SimpleFeatureBuilder.build(type, new Object[]{ "diff2", geom }, "original"));
		FeatureReader<SimpleFeatureType, SimpleFeature> reader=new TestReader(type,SimpleFeatureBuilder.build(type,new Object[]{ "original", geom }, "original") );
		writer=new DiffFeatureWriter(reader, diff){
            protected void fireNotification(int eventType,
                    ReferencedEnvelope bounds) {
            }
			
		};
	}

	public void testRemove() throws Exception {
		writer.next();
		SimpleFeature feature=writer.next();
		writer.remove();
		assertNull(writer.diff.getAdded().get(feature.getID()));
	}

	public void testHasNext() throws Exception {
		assertTrue(writer.hasNext());
		assertEquals(2, writer.diff.getAdded().size()+writer.diff.getModified().size());
		writer.next();
		assertTrue(writer.hasNext());
		assertEquals(2, writer.diff.getAdded().size()+writer.diff.getModified().size());
		writer.next();
		assertFalse(writer.hasNext());
		assertEquals(2, writer.diff.getAdded().size()+writer.diff.getModified().size());
	}
	
	public void testWrite() throws IOException, Exception {
		while( writer.hasNext() ){
			writer.next();
		}
		
		SimpleFeature feature=writer.next();
		feature.setAttribute("name", "new1");
		
		writer.write();
		assertEquals(2, writer.diff.getAdded().size() );
		feature=writer.next();
		feature.setAttribute("name", "new2");
		
		writer.write();
		
		assertEquals(3, writer.diff.getAdded().size() );
	}


}
