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
package org.geotools.renderer.lite.gridcoverage2d;

import java.awt.Color;

import org.geotools.util.NumberRange;

/**
 * The {@link ConstantColorMapElement} is a special type of
 * {@link ColorMapTransformElement} that is used to render no data values.
 * 
 * @author Simone Giannecchini, GeoSolutions.
 * @todo simplify
 */
class ConstantColorMapElement extends LinearColorMapElement
		implements ColorMapTransformElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4754147707013696371L;

	ConstantColorMapElement(CharSequence name, final Color color,
			final NumberRange<? extends Number> inRange, final int outVal)
			throws IllegalArgumentException {
		super(name, new Color[] { color }, inRange, NumberRange.create(outVal,
				outVal));
	}
	
	/**
	 * @see LinearColorMapElement#ClassificationCategory(CharSequence,
	 *      Color[], NumberRange, NumberRange)
	 */
	ConstantColorMapElement(final CharSequence name,
			final Color color, final short value, final int sample)
			throws IllegalArgumentException {
		this(name,  color , NumberRange.create(value, value),
				sample);

	}
	/**
	 * @see LinearColorMapElement#ClassificationCategory(CharSequence,
	 *      Color[], NumberRange, NumberRange)
	 */
	ConstantColorMapElement(final CharSequence name,
			final Color color, final int value, final int sample)
			throws IllegalArgumentException {
		this(name,  color , NumberRange.create(value, value),
				sample);

	}
	/**
	 * @see LinearColorMapElement#ClassificationCategory(CharSequence,
	 *      Color[], NumberRange, NumberRange)
	 */
	ConstantColorMapElement(final CharSequence name,
			final Color color, final float value, final int sample)
			throws IllegalArgumentException {
		this(name,  color , NumberRange.create(value, value),
				sample);

	}

	
	/**
	 * @see LinearColorMapElement#ClassificationCategory(CharSequence,
	 *      Color[], NumberRange, NumberRange)
	 */
	ConstantColorMapElement(final CharSequence name,
			final Color color, final double value, final int sample)
			throws IllegalArgumentException {
		this(name,  color , NumberRange.create(value, value),
				sample);

	}




}
