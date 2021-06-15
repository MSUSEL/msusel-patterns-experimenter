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
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.operation;

import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.Matrix;
import org.geotools.referencing.operation.matrix.XMatrix;


/**
 * Interface for linear {@link MathTransform}s.  A linear transform can be express as an affine
 * transform using a {@linkplain #getMatrix matrix}. The {@linkplain Matrix#getNumCol number of
 * columns} is equals to the number of {@linkplain #getSourceDimensions source dimensions} plus 1,
 * and the {@linkplain Matrix#getNumRow number of rows} is equals to the number of
 * {@linkplain #getTargetDimensions target dimensions} plus 1.
 *
 * @since 2.0
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public interface LinearTransform extends MathTransform {
    /**
     * Returns this transform as an affine transform matrix.
     *
     * @return A copy of the underlying matrix.
     */
    Matrix getMatrix();

    /**
     * Tests whether this transform does not move any points, by using the provided
     * {@code tolerance} value. The signification of <cite>tolerance value</cite> is
     * the same than in the following pseudo-code:
     *
     * <blockquote><pre>
     * {@linkplain #getMatrix()}.{@linkplain XMatrix#isIdentity(double) isIdentity}(tolerance);
     * </pre></blockquote>
     *
     * @param tolerance The tolerance factor.
     * @return {@code true} if this transform is the identity one
     *
     * @since 2.4
     */
    boolean isIdentity(double tolerance);
}
