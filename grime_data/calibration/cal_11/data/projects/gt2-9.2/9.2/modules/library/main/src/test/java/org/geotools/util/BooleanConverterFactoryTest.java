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
package org.geotools.util;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class BooleanConverterFactoryTest extends TestCase {

	BooleanConverterFactory factory;
	
	protected void setUp() throws Exception {
		factory = new BooleanConverterFactory();
	}
	
	public void testFromString() throws Exception {
		assertEquals( Boolean.TRUE, convert( "true" ) );
		assertEquals( Boolean.TRUE, convert( "1" ) );
		assertEquals( Boolean.FALSE, convert( "false" ) );
		assertEquals( Boolean.FALSE, convert( "0" ) );
		
	}
	
	public void testFromInteger() throws Exception {
		assertEquals( Boolean.TRUE, convert( new Integer( 1 ) ) );
		assertEquals( Boolean.FALSE, convert( new Integer( 0 ) ) );
	}
	
	Boolean convert( Object value ) throws Exception {
		return (Boolean) factory.createConverter( value.getClass(), Boolean.class, null ).convert( value, Boolean.class );
	}
}
