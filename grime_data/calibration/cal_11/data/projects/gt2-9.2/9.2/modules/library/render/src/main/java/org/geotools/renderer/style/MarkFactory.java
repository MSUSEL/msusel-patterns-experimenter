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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style;

import java.awt.Graphics2D;
import java.awt.Shape;

import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;

/**
 * Symbol handler for a Mark.
 *
 *
 *
 * @source $URL$
 */
public interface MarkFactory {
    /**
     * Turns the specified URL into an Shape, eventually using the Feature
     * attributes to evaluate the expression, or returns <code>null</code> if
     * the factory cannot evaluate this symbolUrl.
     * <p>
     * The returned Shape must not exceed the [-0.5, -0.5, 0.5, 0.5] bounds
     * (will be rescaled according to the <size> parameter given in graphics
     * on the fly</p>
     * 
     * @param symbolUrl
     *            the expression that will return the symbol name. Once
     *            evaluated the expression should return something like
     *            <code>plainName</code> or like <code>protocol://path</code>.
     *            See the actual implementations for details on the kind of
     *            supported name.
     * @param feature
     *            The feature that will be used to evaluate the symbolURL
     *            expression (or to extract data from it, think complex attributes, in that
     *            case a visit to the expression and some direct attribute value extraction 
     *            might be needed instead)
     * 
     */
    public Shape getShape(Graphics2D graphics, Expression symbolUrl, Feature feature) throws Exception;
}
