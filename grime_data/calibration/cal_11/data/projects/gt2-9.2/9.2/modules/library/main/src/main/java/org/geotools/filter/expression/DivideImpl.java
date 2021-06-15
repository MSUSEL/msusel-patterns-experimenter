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
package org.geotools.filter.expression;

import org.geotools.filter.Filters;
import org.geotools.filter.MathExpressionImpl;
import org.geotools.util.Utilities;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;

/**
 * Implementation of divide expression.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 *
 *
 * @source $URL$
 */
public class DivideImpl extends MathExpressionImpl implements Divide {

	public DivideImpl(Expression expr1, Expression expr2) {
		super(expr1,expr2);
		
		//	backwards compatability with old type system
		expressionType = MATH_DIVIDE;
	}
	
	public Object evaluate(Object feature) throws IllegalArgumentException {
		ensureOperandsSet();
		
		double leftDouble = Filters.number( getExpression1().evaluate(feature) );
		double rightDouble = Filters.number( getExpression2().evaluate(feature) );
      
		return number(leftDouble / rightDouble);
	}

	public Object accept(ExpressionVisitor visitor, Object extraData) {
		return visitor.visit(this,extraData);
	}
	
	/**
     * Compares this expression to the specified object. Returns true if the 
     *
     * @param obj - the object to compare this expression against.
     *
     * @return true if specified object is equal to this expression; false
     *         otherwise.
     */
    public boolean equals(Object obj) {
    	if (obj instanceof DivideImpl) {
            DivideImpl other = (DivideImpl) obj;

            return Utilities.equals(getExpression1(),other.getExpression1())
            	&& Utilities.equals(getExpression2(),other.getExpression2());
        } else {
            return false;
        }
    }
    
    /**
     * Override of hashCode method.
     *
     * @return a hash code value for this divide expression.
     */
    public int hashCode() {
        int result = 23;
        
        result = (37 * result) + getExpression1().hashCode();
        result = (37 * result) + getExpression2().hashCode();

        return result;
    }
    
    public String toString() {
    	return "(" + getExpression1().toString() + "/" + getExpression2().toString()+ ")";
    }

}
