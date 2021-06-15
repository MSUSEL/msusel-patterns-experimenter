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
package org.geotools.filter;

import org.opengis.filter.FilterVisitor;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class IsLessThenOrEqualToImpl extends MultiCompareFilterImpl implements
		PropertyIsLessThanOrEqualTo {
	
	protected IsLessThenOrEqualToImpl(org.opengis.filter.FilterFactory factory) {
		this(factory, null, null);
	}
	
        protected IsLessThenOrEqualToImpl(org.opengis.filter.FilterFactory factory, Expression expression1, Expression expression2) {
            this(factory, expression1, expression2,false);
    }

    protected IsLessThenOrEqualToImpl(org.opengis.filter.FilterFactory factory, Expression expression1, Expression expression2,boolean matchCase ) {
            super(factory, expression1, expression2,matchCase);
            
            //backwards compat with old type system
            this.filterType = COMPARE_LESS_THAN_EQUAL;
    }
    
        protected IsLessThenOrEqualToImpl(org.opengis.filter.FilterFactory factory, Expression expression1, Expression expression2, MatchAction matchAction) {
            this(factory, expression1, expression2,false, matchAction);
        }
        
        protected IsLessThenOrEqualToImpl(org.opengis.filter.FilterFactory factory, Expression expression1, Expression expression2,boolean matchCase, MatchAction matchAction ) {
                super(factory, expression1, expression2,matchCase, matchAction);
                
                //backwards compat with old type system
                this.filterType = COMPARE_LESS_THAN_EQUAL;
        }

        @Override
	public boolean evaluateInternal(Object v1, Object v2) {
		Object[] values = eval( v1, v2 );
		Comparable value1 = comparable( values[ 0 ] );
		Comparable value2 = comparable( values[ 1 ] );
		
		return value1 != null && value2 != null && compare(value1,value2) <= 0;
	}
	
	public Object accept(FilterVisitor visitor, Object extraData) {
		return visitor.visit(this,extraData);
	}
}
