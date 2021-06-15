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
package org.geotools.styling;

import org.geotools.filter.ConstantExpression;
import org.opengis.filter.expression.Expression;
import org.opengis.style.StyleVisitor;


/**
 * A Displacement gives X and Y offset displacements to use for rendering a
 * text label near a point.
 *
 *
 * @author Ian Turton, CCG
 * @version $Id$
 *
 *
 * @source $URL$
 */
public interface Displacement extends org.opengis.style.Displacement{
    /**
     * Default Displacement instance.
     */
    static final Displacement DEFAULT = new ConstantDisplacement() {
            private void cannotModifyConstant() {
                throw new UnsupportedOperationException("Constant Stroke may not be modified");
            }

            public Expression getDisplacementX() {
                return ConstantExpression.ZERO;
            }

            public Expression getDisplacementY() {
                return ConstantExpression.ZERO;
            }

            public Object accept(StyleVisitor visitor, Object extraData) {
                cannotModifyConstant();
                return null;
            }

        };

    /**
     * Null Displacement instance.
     */
    static final Displacement NULL = new ConstantDisplacement() {
            private void cannotModifyConstant() {
                throw new UnsupportedOperationException("Constant Stroke may not be modified");
            }

            public Expression getDisplacementX() {
                return ConstantExpression.NULL;
            }

            public Expression getDisplacementY() {
                return ConstantExpression.NULL;
            }

            public Object accept(StyleVisitor visitor, Object extraData) {
                cannotModifyConstant();
                return null;
            }
        };

    /**
     * Returns an expression that computes a pixel offset from the geometry
     * point.  This offset point is where the text's anchor point gets
     * located. If this expression is null, the default offset of zero is
     * used.
     *
     * @return Horizontal offeset
     */
    Expression getDisplacementX();

    /**
     * Sets the expression that computes a pixel offset from the geometry
     * point.
     */
    void setDisplacementX(Expression x);

    /**
     * Sets the expression that computes a pixel offset from the geometry
     * point.
     */
    void setDisplacementY(Expression y);

    void accept(org.geotools.styling.StyleVisitor visitor);
}


abstract class ConstantDisplacement implements Displacement {
    private void cannotModifyConstant() {
        throw new UnsupportedOperationException("Constant Displacement may not be modified");
    }

    public void setDisplacementX(Expression x) {
        cannotModifyConstant();
    }

    public void setDisplacementY(Expression y) {
        cannotModifyConstant();
    }

    public void accept(org.geotools.styling.StyleVisitor visitor) {
        cannotModifyConstant();
    }
    
    public void accept(StyleVisitor visitor) {
        cannotModifyConstant();
    }
};
