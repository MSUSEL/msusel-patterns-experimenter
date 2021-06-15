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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.expression.InternalVolatileFunction;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.InternalFunction;


/**
 * Unit test for DuplicatorFilterVisitor.
 *
 * @author Cory Horner, Refractions Research Inc.
 *
 *
 * @source $URL$
 */
public class DuplicatorFilterVisitorTest extends TestCase {
    FilterFactory fac;

    public DuplicatorFilterVisitorTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        fac = CommonFactoryFinder.getFilterFactory(null);
    }
    
    public void testLogicFilterDuplication() throws IllegalFilterException {
        List filters = new ArrayList();
        // create a filter
        Filter filter1 = fac.greater(fac.literal(2), fac.literal(1));
        filters.add(filter1);
        Filter filter2 = fac.greater(fac.literal(4), fac.literal(3));
        filters.add(filter2);

        And oldFilter = fac.and(filters);
        //duplicate it
    	DuplicatingFilterVisitor visitor = new DuplicatingFilterVisitor((FilterFactory2) fac);
    	Filter newFilter = (Filter) oldFilter.accept(visitor, null);

    	//compare it
    	assertNotNull(newFilter);
    	//TODO: a decent comparison
    }
    
    public void testDuplicateInternalFunction() throws IllegalFilterException {
        class TestInternalFunction extends InternalVolatileFunction {

            @Override
            public Object evaluate(Object object) {
                return null;
            }
            @Override
            public InternalFunction duplicate(Expression... parameters) {
                return new TestInternalFunction();
            }

        }
        
        Expression internalFunction = new TestInternalFunction();
        Filter filter = fac.isNull(internalFunction);

        DuplicatingFilterVisitor visitor = new DuplicatingFilterVisitor((FilterFactory2) fac);
        Filter newFilter = (Filter) filter.accept(visitor, null);
        
        assertTrue(newFilter instanceof PropertyIsNull);
        Expression newExpression = ((PropertyIsNull)newFilter).getExpression();
        assertNotNull(newExpression);
        assertTrue(newExpression instanceof TestInternalFunction);
        assertNotSame(internalFunction, newExpression);
    }
}
