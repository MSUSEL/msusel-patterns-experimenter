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

import java.io.Serializable;

import org.geotools.util.NumberRange;
import org.opengis.util.InternationalString;

/**
 * A {@link DomainElement1D} can be seen as a monodimensional range of values with its own label. 
 * 
 * <p>
 * All {@link DomainElement1D}D <strong>must</strong> have a human readable name.
 * <p>
 * All {@code DomainElement1D} objects are immutable and thread-safe.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * 
 *
 *
 *
 * @source $URL$
 */
public interface DomainElement1D extends Serializable, Comparable<DomainElement1D> {

	/**
	 * Returns the domain element name.
	 */
	public InternationalString getName();

	/**
	 * Compares the specified object with this domain element for equality.
	 */
	public boolean equals(final Object object);


	/**
	 * Provides access to the input {@link NumberRange} for this
	 * {@link DomainElement1D}.
	 * 
	 * @return the range where this {@link DomainElement1D} is defined.
	 */
	public NumberRange<? extends Number> getRange();


	/**
	 * This methods can be used to check whether or not a given value belongs to
	 * {@link DomainElement1D}.
	 * 
	 * @param value
	 *            to check for the inclusion.
	 * @return <code>true</code> if the value belongs to this {@link DomainElement1D},
	 *         <code>false</code> otherwise.
	 */
	public boolean contains(final double value);

	/**
	 * This methods can be used to check whether or not a given value belongs to
	 * {@link DomainElement1D}.
	 * 
	 * @param value
	 *            to check for the inclusion.
	 * @return <code>true</code> if the value belongs to this {@link DomainElement1D},
	 *         <code>false</code> otherwise.
	 */
	public boolean contains(final Number value);

	/**
	 * This methods can be used to check whether or not a given
	 * {@link NumberRange} belongs to {@link DomainElement1D}.
	 * 
	 * @param value
	 *            to check for the inclusion.
	 * @return <code>true</code> if the {@link NumberRange} belongs to this
	 *         {@link DomainElement1D}, <code>false</code> otherwise.
	 */
	public boolean contains(final NumberRange<? extends Number> range);

}
