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
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class EdigeoSCDTest extends TestCase {
	private EdigeoSCD eScd ;
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		eScd = new EdigeoSCD(EdigeoTestUtils.fileName("EDAB01SE.SCD"));
	}
	
	@After
	protected void tearDown() throws Exception {
		eScd = null;
		super.tearDown();
	}
	
	@Test
	public void testReadSCDFile() {
		HashMap<String,String> attIds = null;
		try {
			// Gets defined attibutes for PTCANV_id object
			attIds = eScd.readSCDFile("PTCANV_id");
			// Gets returned values for attribute name in Edigeo schema
			assertEquals("CAN attribute name for PTCANV_id object", "ID_N_ATT_CAN", attIds.get("CAN_id"));
			assertEquals("IDU attribute name for PTCANV_id object", "ID_N_ATT_IDU", attIds.get("IDU_id"));
			assertEquals("MAP attribute name for PTCANV_id object", "ID_N_ATT_MAP", attIds.get("MAP_id"));
			assertEquals("ORI attribute name for PTCANV_id object", "ID_N_ATT_ORI", attIds.get("ORI_id"));
			assertEquals("PALT attribute name for PTCANV_id object", "ID_N_ATT_PALT", attIds.get("PALT_id"));
			assertEquals("PALT attribute name for PTCANV_id object", "ID_N_ATT_PPLN", attIds.get("PPLN_id"));
			assertEquals("PALT attribute name for PTCANV_id object", "ID_N_ATT_SYM", attIds.get("SYM_id"));
		} catch (IOException e) {
			assertFalse(true);
		}
		attIds = null;
	}
	
}
