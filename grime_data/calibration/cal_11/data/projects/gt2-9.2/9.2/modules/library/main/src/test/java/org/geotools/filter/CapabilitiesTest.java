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
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.spatial.Beyond;
/**
 * Unit test for FilterCapabilities.
 *
 * @author Chris Holmes, TOPP
 *
 *
 * @source $URL$
 */
public class CapabilitiesTest extends TestCase {
    public void testCapablities(){
        Capabilities capabilities = new Capabilities();
        capabilities.addType( Beyond.class ); // add to SpatialCapabilities
        capabilities.addType( PropertyIsEqualTo.class ); // add to ScalarCapabilities
        capabilities.addName( "NullCheck" ); // will enable PropertyIsNull use
        capabilities.addName( "Mul" ); // will enable hasSimpleArithmatic
        capabilities.addName( "random" ); // a function returning a random number
        capabilities.addName( "Length", 1 ); // single argument function
        capabilities.addName( "toDegrees", "radians" ); // single argument function
        capabilities.addName( "length", "expression" );

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter filter = ff.between( ff.literal(0), ff.property("x"), ff.literal( 2 ) );        
        assertFalse("supports", capabilities.supports( filter ) );
        
        filter = ff.equals(ff.property("x"), ff.literal( 2 ) );        
        assertTrue("supports", capabilities.supports( filter ) );
        
        assertTrue("fullySupports", capabilities.fullySupports( filter ) );

        Capabilities capabilities2 = new Capabilities();

        capabilities2.addAll( capabilities );
        capabilities2.addType( And.class );
        
        assertTrue( capabilities2.getContents().getScalarCapabilities().hasLogicalOperators() );
        assertFalse( capabilities.getContents().getScalarCapabilities().hasLogicalOperators() );
        
    }
}
