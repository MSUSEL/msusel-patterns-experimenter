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
package org.geotools.coverage.io.impl.range;

import java.util.ArrayList;
import java.util.List;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.Unit;

import org.opengis.util.CodeList;

/**
 * Process Color is a subtractive model used when working with pigment. This
 * model is often used when printing.
 * <p>
 * This is a normal Java 5 enum capturing the closed set of CMYK names. It is
 * used as a basis for the definition of an Axis built around these constants.
 * <p>
 * Please understand that this is not the only possible subtractive color model
 * - a commerical alternative is the Pantone (tm)) colors.
 *
 *
 *
 * @source $URL$
 */
public class HSV extends CodeList<HSV> {
	private static final long serialVersionUID = 2772167658847829287L;

	private static List<HSV> ALL = new ArrayList<HSV>();

	public static HSV HUE = new HSV("Hue");
	public static HSV SATURATION = new HSV("Saturation");
	public static HSV VALUE = new HSV("Value");

	public HSV(String name) {
		super(name, ALL );
	}
	
	@Override
	public HSV[] family() {
		return ALL.toArray( new HSV[ ALL.size() ]);
	}
	
	/**
	 * Axis covering the full {@link HSV} range.
	 */
	public static final DefaultAxis<HSV,Dimensionless> AXIS
    	= new DefaultAxis<HSV,Dimensionless>("Additive Color", CodeMeasure.valueOf( ALL ), Unit.ONE );

	/**
	 * Axis around {@link #KEY }.
	 */
	public static final DefaultAxis<HSV,Dimensionless> INTENSITY_AXIS =
		new DefaultAxis<HSV,Dimensionless>("Intensity", CodeMeasure.valueOf(VALUE), Unit.ONE );

}
