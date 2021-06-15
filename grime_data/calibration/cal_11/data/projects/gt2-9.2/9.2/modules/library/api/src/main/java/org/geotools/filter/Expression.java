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
package org.geotools.filter;

import org.opengis.feature.simple.SimpleFeature;


/**
 * Defines an expression, the units that make up Filters.   This filter holds
 * one or more filters together and relates them logically in an internally
 * defined manner.
 *
 * @author Rob Hranac, Vision for New York
 *
 *
 * @source $URL$
 * @version $Id$
 *
 * @deprecated use {@link org.opengis.filter.expression.Expression}
 */
public interface Expression extends ExpressionType, org.opengis.filter.expression.Expression {
    /**
     * Gets the type of this expression.
     *
     * @return Expression type.
     *
     * @deprecated The enumeration based type system has been replaced by a
     * class based type system.
     */
    short getType();

    /**
     * Evaluates the expression against an instance of {@link Feature}.
     *
     * @param feature The feature being evaluated.
     *
     * @return The result.
     */
    Object evaluate(SimpleFeature feature);

    /**
     * Returns a value for this expression.  The feature argument is used if a
     * feature is needed to evaluate the expression, as in the case of an
     * AttributeExpression.
     *
     * @param feature Specified feature to use when returning value.   Some
     *        expressions, such as LiteralExpressions, may ignore this as it
     *        does not affect their return value.
     *
     * @return Value of the expression, evaluated with the feature object if
     *         necessary.
     *
     * @deprecated use {@link org.opengis.filter.expression.Expression#evaluate(Feature)}
     */
    Object getValue(SimpleFeature feature);

    /**
     * Used by FilterVisitors to perform some action on this filter instance.
     * Typicaly used by Filter decoders, but may also be used by any thing
     * which needs infomration from filter structure. Implementations should
     * always call: visitor.visit(this); It is importatant that this is not
     * left to a parent class unless the parents API is identical.
     *
     * @param visitor The visitor which requires access to this filter, the
     *        method must call visitor.visit(this);
     * @deprecated use use {@link org.opengis.filter.expression.Expression#accept(ExpressionVisitor, Object)}
     */
    void accept(FilterVisitor visitor);
}
