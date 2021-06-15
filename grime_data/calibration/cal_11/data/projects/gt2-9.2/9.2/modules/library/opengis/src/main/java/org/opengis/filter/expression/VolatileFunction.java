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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */

package org.opengis.filter.expression;

/**
 * <p>
 * Marker interface indicating that that the function return value can change
 * during a single data access operation even if the argument values provided to
 * it remain constant
 * </p>
 * <p>
 * Very few functions are truly volatile, one example being random(), whose
 * value is going to change over each invocation, even during the same feature
 * collection filtering
 * </p>
 * <p>
 * Functions whose value changes over time but not within the same feature
 * collection filtering are considered to be <em>stable</em> and as such their
 * result can be considered a constant during the single data access operation
 * </p>
 * <p>
 * GeoTools will try to optimize out the stable functions and replace them with
 * a constant that can be easily encoded in whatever native filtering mechanism
 * the datastores have
 * </p>
 * <p>
 * Given the vast majority of function are <em>stable</em> by the above
 * definition only the fews that aren't suitable for constant replacement during
 * a single run against a feature collection should be marked as
 * <em>volatile</em>
 * 
 * @author Andrea Aime - GeoSolutions
 *
 *
 * @source $URL$
 */
public interface VolatileFunction extends Function {

}
