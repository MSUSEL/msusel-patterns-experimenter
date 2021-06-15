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
package org.geotools.geometry.iso.util;

import java.math.BigDecimal;

/**
 * This class offers elementary arithmetic operations for the elementary data type <code>double</code>.
 * The operations are separated from the rest of the code to keep the option to interchange this package with a exact computation package (where needed).
 * 
 * The implementations in this class are robust using the Java type BigDecimal.
 * 
 * @author Sanjay Jena
 *
 *
 *
 *
 *
 * @source $URL$
 */
public class ExactDoubleOperation {
	
	/**
	 * Returns the sum of two doubles: d1 + d2
	 * 
	 * @param d1 First value to add
	 * @param d2 Second value to add
	 * @return Sum of the two values
	 */
	public static double add(double d1, double d2) {
		return (new BigDecimal(d1)).add(new BigDecimal(d2)).doubleValue();
	}
	
	/**
	 * Returns the subtraction of two doubles: d1 - d2
	 * 
	 * @param d1 First value
	 * @param d2 Value to subtract from first value
	 * @return Subtraction d1 - d2
	 */
	public static double subtract(double d1, double d2) {
		return (new BigDecimal(d1)).subtract(new BigDecimal(d2)).doubleValue();
	}
	
	/**
	 * Returns the multiplication of two doubles: d1 * d2
	 * 
	 * @param d1 First value to multiplicate
	 * @param d2 Second value to multiplicate
	 * @return Product of the two values
	 */
	public static double mult(double d1, double d2) {
		return (new BigDecimal(d1)).multiply(new BigDecimal(d2)).doubleValue();
	}
	
	/**
	 * Returns the division of two doubles: d1 / d2
	 * 
	 * @param d1 Dividend
	 * @param d2 Divisor
	 * @return Division of the two values
	 */
	public static double div(double d1, double d2) {
		return (new BigDecimal(d1)).divide(new BigDecimal(d2)).doubleValue();
	}




}
