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
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2005-2006, GeoTools Project Managment Committee (PMC)
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
package org.geotools.data.edigeo;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class EdigeoDataStoreTest extends TestCase {
	private EdigeoDataStore eds ;
	
	@Before protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		eds = new EdigeoDataStore(EdigeoTestUtils.fileName("E000AB01.THF"), "COMMUNE_id");
	}
	
	@After protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
	
	@Test public void testCheckPciObj() {
		assertEquals("BORNE_id should be an existing Edigeo Object", 
				"BORNE_id", EdigeoDataStore.checkPciObj("BORNE_id"));
		try {
			assertEquals("FOO_id", EdigeoDataStore.checkPciObj("FOO_id"));
		} catch (Exception e) {
			assertTrue("FOO_id is not an existing Edigeo object",
					e instanceof IllegalArgumentException);
		}
	}
	
	public void testGetSchema() {
		try {
			SimpleFeatureType schema = eds.getSchema("E000AB01");
			List<AttributeDescriptor> attributes = schema.getAttributeDescriptors();
		    assertEquals("Number of Attributes", 3, attributes.size());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}
	
}
