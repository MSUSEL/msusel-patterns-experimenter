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

import org.geotools.referencing.operation.transform.LinearTransform1D;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.util.NumberRange;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.NoninvertibleTransformException;

/**
 * Convenience implementation of a {@link PiecewiseTransform1DElement} that can be used to
 * map single values or an interval to a single output value.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * 
 */
class DefaultConstantPiecewiseTransformElement extends DefaultLinearPiecewiseTransform1DElement implements
		PiecewiseTransform1DElement, MathTransform1D, Comparable<DomainElement1D>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6704840161747974131L;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}
	 * @param inRange
	 *            for this {@link DomainElement1D}
	 * @param outVal
	 *            for this {@link DefaultLinearPiecewiseTransform1DElement}
	 * @throws IllegalArgumentException
	 *             in case the input values are illegal.
	 */
	DefaultConstantPiecewiseTransformElement(CharSequence name,
			final NumberRange<?> inRange, final double outVal)
			throws IllegalArgumentException {
		super(name, inRange, NumberRange.create(outVal, outVal));

	}


	/**
	 * Constructor.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}
	 * @param inRange
	 *            for this {@link DomainElement1D}
	 * @param outVal
	 *            for this {@link DefaultLinearPiecewiseTransform1DElement}
	 * @throws IllegalArgumentException
	 *             in case the input values are illegal.
	 */
	 DefaultConstantPiecewiseTransformElement(CharSequence name,
			final NumberRange<?> inRange, final int outVal)
			throws IllegalArgumentException {
		super(name, inRange, NumberRange.create(outVal, outVal));
	}


	/**
	 * Constructor.
	 * 
	 * @param name
	 *            for this {@link DomainElement1D}
	 * @param inRange
	 *            for this {@link DomainElement1D}
	 * @param outVal
	 *            for this {@link DefaultLinearPiecewiseTransform1DElement}
	 * @throws IllegalArgumentException
	 *             in case the input values are illegal.
	 */
	DefaultConstantPiecewiseTransformElement(CharSequence name,
			final NumberRange<?> inRange, final byte outVal)
			throws IllegalArgumentException {
		super(name, inRange, NumberRange.create(outVal, outVal));
	}

	/**
	 * The transformation we are specifying here is not always invertible, well,
	 * to be honest, strictly speaking it never really is. However when the
	 * underlying transformation is a 1:1 mapping we can invert it.
	 */
	public MathTransform1D inverse() throws NoninvertibleTransformException {
		if (this.getInputMinimum() == getInputMaximum())
			return LinearTransform1D.create(0, getInputMinimum());
		throw new UnsupportedOperationException(Errors.format(
				ErrorKeys.UNSUPPORTED_OPERATION_$1, "inverse"));

	}

}
