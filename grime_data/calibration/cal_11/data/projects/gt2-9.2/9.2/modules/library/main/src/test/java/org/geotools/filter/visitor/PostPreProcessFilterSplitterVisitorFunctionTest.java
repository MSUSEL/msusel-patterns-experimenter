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

import java.util.Arrays;

import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.function.FilterFunction_geometryType;
import org.geotools.filter.function.math.FilterFunction_abs;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;

/**
 * Test case where only specific functions are supported.
 * 
 * @author Jesse
 *
 *
 *
 *
 * @source $URL$
 */
public class PostPreProcessFilterSplitterVisitorFunctionTest extends AbstractPostPreProcessFilterSplittingVisitorTests {
    
    PostPreProcessFilterSplittingVisitor visitor;

	public void testSupportAll() throws Exception {
        
		PropertyIsEqualTo filter1 = createFunctionFilter();
		FilterFunction_abs filterFunction_abs = new FilterFunction_abs();
		filterFunction_abs.setParameters(Arrays.asList(new Expression[]{ff.property("name")}));
        PropertyIsEqualTo filter2 = ff.equals(ff.property("name"), filterFunction_abs);
		
		Filter filter=ff.and(filter1,filter2);

        visitor = newVisitor(new FilterCapabilities());
        filter.accept(visitor, null);
		
		assertEquals(Filter.INCLUDE, visitor.getFilterPre());
		assertEquals(filter, visitor.getFilterPost());
        
        FilterCapabilities filterCapabilitiesMask = new FilterCapabilities();

		filterCapabilitiesMask.addType(FilterFunction_geometryType.class);
		filterCapabilitiesMask.addType(FilterFunction_abs.class);
		filterCapabilitiesMask.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
        filterCapabilitiesMask.addAll(FilterCapabilities.LOGICAL_OPENGIS);
		visitor=newVisitor(filterCapabilitiesMask);

        filter.accept(visitor, null);
		
		assertEquals(Filter.INCLUDE, visitor.getFilterPost());
		assertEquals(filter, visitor.getFilterPre());
	}

	public void testSupportOnlySome() throws Exception {

        PropertyIsEqualTo filter1 = createFunctionFilter();
        FilterFunction_abs filterFunction_abs = new FilterFunction_abs();
        filterFunction_abs.setParameters(Arrays.asList(new Expression[]{ff.property("name")}));
        PropertyIsEqualTo filter2 = ff.equals(ff.property("name"), filterFunction_abs);
        
        Filter filter=ff.and(filter1,filter2);

        FilterCapabilities filterCapabilitiesMask = new FilterCapabilities();
		filterCapabilitiesMask.addType(FilterFunction_geometryType.class);
		filterCapabilitiesMask.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
        filterCapabilitiesMask.addAll(FilterCapabilities.LOGICAL_OPENGIS);
		visitor=newVisitor(filterCapabilitiesMask);

        filter.accept(visitor, null);
		
		assertEquals(filter1, visitor.getFilterPre());
		assertEquals(filter2, visitor.getFilterPost());
		
	}
}
