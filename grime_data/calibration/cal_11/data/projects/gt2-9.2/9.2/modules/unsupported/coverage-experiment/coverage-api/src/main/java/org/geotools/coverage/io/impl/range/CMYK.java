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

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.Unit;

/**
 * Process Color is a subtractive model used when working with pigment. This
 * model is often used when printing.
 * <p>
 * This is a normal Java 5 enum capturing the closed set of CMYK names. It is
 * used as a basis for the definition of an Axis built around these constants.
 * <p>
 * Please understand that this is not the only possible subtractive color model
 * - a commercial alternative is the Pantone (tm)) colors.
 *
 *
 *
 * @source $URL$
 */
public enum CMYK {
	CYAN, MAGENTA, YELLOW, KEY;

	/**
	 * Axis covering the full {@link CMYK} range.
	 */
	public static final DefaultAxis<CMYK,Dimensionless> AXIS
		= new DefaultAxis<CMYK,Dimensionless>("Process Color", EnumMeasure.valueOf( CMYK.class ), Unit.ONE );

	/**
	 * Axis around {@link #KEY }.
	 */
	public static final DefaultAxis<CMYK,Dimensionless> BLACK_AXIS =
		new DefaultAxis<CMYK,Dimensionless>("Process Black", EnumMeasure.valueOf(KEY), Unit.ONE );
    
}
