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

import java.util.List;

import org.geotools.util.NumberRange;
import org.opengis.util.InternationalString;

/**
 * An immutable {@link Domain1D} as a list of {@link DomainElement1D}. {@link DomainElement1D} are sorted by their 
 * values. Overlapping ranges are not allowed. The{@link #findDomainElement(double)} method is responsible for 
 * finding the right {@link DomainElement1D} for an arbitrary domain value.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * 
 *
 *
 *
 * @source $URL$
 */
public interface Domain1D<T extends DomainElement1D> extends List<T>{

	/**
	 * Returns the name of this object. The default implementation returns the
	 * name of what seems to be the "main" domain element (i.e. the domain element with the
	 * widest range of values).
	 */
	public abstract InternationalString getName();

	/**
	 * Returns the range of values in this {@link Domain1D}. This is the union of
	 * the range of values of every {@link Domain1D}.
	 * 
	 * @return The range of values.
	 * 
	 */
	public abstract NumberRange<? extends Number> getApproximateDomainRange();

	/**
	 * Returns the {@link DomainElement1D} of the specified sample value. If no {@link DomainElement1D} fits,
	 * then this method returns {@code null}.
	 * 
	 * @param sample
	 *            The value.
	 * @return The domain element of the supplied value, or {@code null}.
	 */
	public T findDomainElement(final double sample);

	/**
	 * Tell us if there is a gap in this {@link Domain1D} which means a range
	 * where no {@link DomainElement1D} is defined.
	 * 
	 * @return <code>true</code> in case a gap exists, <code>false</code>
	 *         otherwise.
	 * 
	 * @return
	 */
	public boolean hasGaps();

}
