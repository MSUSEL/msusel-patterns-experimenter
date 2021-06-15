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
package org.geotools.data.wms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.geotools.data.ows.Layer;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 *
 * @source $URL$
 */
public class WMSUtilsTest extends TestCase {

	public void testFindCommonEPSGs() {
		List layers = new ArrayList();
		
		Layer layer1 = new Layer();
		TreeSet set1 = new TreeSet();
		set1.add("EPSG:4326");
		set1.add("EPSG:3005");
		set1.add("EPSG:42101");
		layer1.setSrs(set1);
		
		Layer layer2 = new Layer();
		TreeSet set2 = new TreeSet();
		set2.add("EPSG:3005");
		set2.add("EPSG:42101");
		layer2.setSrs(set2);
		
		Layer layer3 = new Layer();
		TreeSet set3 = new TreeSet();
		set3.add("EPSG:42111");
		layer3.setSrs(set3);
		
		layers.add(layer1);
		layers.add(layer2);
		
		Set results1 = WMSUtils.findCommonEPSGs(layers);
		
		assertNotNull(results1);
		assertEquals(2, results1.size());
		assertTrue(results1.contains("EPSG:3005"));
		assertTrue(results1.contains("EPSG:42101"));
		assertFalse(results1.contains("EPSG:4326"));
		
		layers.clear();
		
		layers.add(layer1);
		layers.add(layer3);
		
		Set results2 = WMSUtils.findCommonEPSGs(layers);
		
		assertNotNull(results2);
		assertEquals(0, results2.size());
		assertTrue(results2.isEmpty());
	}

	public void testMatchEPSG() throws Exception {
		CoordinateReferenceSystem crs4326 = CRS.decode("EPSG:4326");
		CoordinateReferenceSystem crs3005 = CRS.decode("EPSG:3005");
		CoordinateReferenceSystem crs3347 = CRS.decode("EPSG:3347");
		
		Set codes = new TreeSet();
		codes.add("EPSG:4326");
		codes.add("EPSG:42102");
		codes.add("EPSG:bork"); //invalid CRSs allowed
		
		String result1 = WMSUtils.matchEPSG(crs4326, codes);
		
		assertNotNull(result1);
		assertEquals("EPSG:4326", result1);
		
		
		//3005 == 42102
		String result2 = WMSUtils.matchEPSG(crs3005, codes);
		
		assertNotNull(result2);
		assertEquals("EPSG:42102", result2);
		
		String result3 = WMSUtils.matchEPSG(crs3347, codes);
		assertNull(result3);
	}

}
