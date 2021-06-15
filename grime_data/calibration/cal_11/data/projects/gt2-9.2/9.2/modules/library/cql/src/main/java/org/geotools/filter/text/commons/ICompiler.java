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
package org.geotools.filter.text.commons;

import java.util.List;

import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;

/**
 * This interface presents the methods which will be implemented by the different compiles.
 * 
 * <p>
 * Warning: This component is not published. It is part of module implementation. 
 * Client module should not use this feature.
 * </p>
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 *
 *
 *
 * @source $URL$
 */
public interface ICompiler {


    /**
     * @return the compilation source
     */
    public String getSource();

    /**
     * Compiles the source string to produce a {@link Filter}. 
     * The filter result must be retrieved with {@link #getFilter()}.
     *
     * @throws CQLException
     */
    public void compileFilter()throws CQLException;

    /**
     * The resultant filter of the compilation
     * @see #compileFilter()
     * 
     * @return Filter
     * @throws CQLException
     */
    public Filter getFilter() throws CQLException;


    /**
     * Compiles the source string to produce an {@link Expression}. 
     * The resultant expression must be retrieved with {@link #getExpression()}.
     *
     * @throws CQLException
     */
    public void compileExpression()throws CQLException;
    /**
     * The resultant {@link Expression} of the compilation.
     * @see #compileExpression()
     * @return Expression
     * @throws CQLException
     */
    public Expression getExpression() throws CQLException;

    
    /**
     * Compiles the source string to produce a {@link List} of {@link Filter}. 
     * The result must be retrieved with {@link #getFilterList()()}.
     *
     * @throws CQLException
     */
    public void compileFilterList()throws CQLException;
    
    /**
     * Return the compilation result.
     * 
     * @see #compileFilterList()
     * @return List<Filter>
     * @throws CQLException
     */
    public List<Filter> getFilterList() throws CQLException;

    /**
     * Return the token presents in the position specified.
     * 
     * @param position
     * @return IToken
     */
    public IToken getTokenInPosition(int position);

}
