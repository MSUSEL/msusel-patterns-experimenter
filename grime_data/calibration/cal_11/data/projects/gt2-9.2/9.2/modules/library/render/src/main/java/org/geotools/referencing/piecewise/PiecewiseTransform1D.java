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
package org.geotools.referencing.piecewise;

import org.opengis.referencing.operation.MathTransform1D;

/**
 * The {@link PiecewiseTransform1D} interface extends the {@link Domain1D}
 * adding transformation capabilities to it.
 * 
 * @author Simone Giannecchini, GeoSolutions.
 * 
 *
 *
 *
 * @source $URL$
 */
public interface PiecewiseTransform1D<T extends PiecewiseTransform1DElement> extends Domain1D<T>, MathTransform1D {

	/**
	 * Indicates whether or not this {@link PiecewiseTransform1D} has a default value which
	 * will be returned when asked to transform a value outside the valid domain elements.
	 * 
	 * @return a <code>boolean</code> to indicate whether or not this {@link PiecewiseTransform1D} has a default value.
	 */
	public boolean hasDefaultValue() ;


	/**
	 * The default value which will be returned when asked to transform a value outside the valid domain elements.
	 * 
	 * <p>
	 * In case {@link #hasDefaultValue()} return <code>false</code> this value has no meaning.
	 * 
	 * @return The default value which will be returned when asked to transform a value outside the valid domain elements.
	 */
	public double getDefaultValue() ;
}
