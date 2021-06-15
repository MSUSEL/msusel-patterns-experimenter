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
package org.geotools.filter;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class IsNotEqualToImpltest extends TestCase {

	org.opengis.filter.FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory( null );
	
	public void testOperandsSameType() {
		Expression e1 = filterFactory.literal( 1 );
		Expression e2 = filterFactory.literal( 2 );
		
		PropertyIsNotEqualTo notEqual = filterFactory.notEqual( e1, e2, true );
		assertTrue( notEqual.evaluate( null ) );
	}
	
	public void testOperandsDifferentType() {
		Expression e1 = filterFactory.literal( 1 );
		Expression e2 = filterFactory.literal( "2"
				);
		
		PropertyIsNotEqualTo notEqual = filterFactory.notEqual( e1, e2, true );
		assertTrue( notEqual.evaluate( null ) );
	}
	
	public void testOperandsIntDouble() {
        Expression e1 = filterFactory.literal( 1 );
        Expression e2 = filterFactory.literal( "1.0" );
        
        PropertyIsNotEqualTo notEqual = filterFactory.notEqual( e1, e2, true );
        assertFalse( notEqual.evaluate( null ) );
    }
	
	public void testCaseSensitivity() {
		Expression e1 = filterFactory.literal( "foo" );
		Expression e2 = filterFactory.literal( "FoO" );
		
		PropertyIsNotEqualTo caseSensitive = filterFactory.notEqual( e1, e2, true );
		assertTrue( caseSensitive. evaluate( null ) );
		
		PropertyIsNotEqualTo caseInsensitive = filterFactory.notEqual( e1, e2, false );
		assertFalse( caseInsensitive. evaluate( null ) );
		
	}
}
