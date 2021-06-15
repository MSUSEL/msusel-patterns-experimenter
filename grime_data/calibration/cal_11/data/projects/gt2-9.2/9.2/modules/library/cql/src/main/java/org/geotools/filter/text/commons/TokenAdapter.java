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
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.filter.text.commons.IToken;
import org.geotools.filter.text.generated.parsers.Token;

/**
 * Adapts Token to {@link IToken}.
 * <p>
 * Token class is generated by javacc, this adapter allows reuse this class in
 * different context (Parser, Compiler and FilterBuilder)
 * </p>
 * <p>
 * The token class, used by the specific parser, requires to be adapted to
 * implementing the interface {@link IToken} which is used by the
 * {@link CQLFilterBuilder} and {@link ECQL}}.
 * </p>
 * <p>
 * Warning: This component is not published. It is part of module implementation. 
 * Client module should not use this feature.
 * </p>
 * @see IToken
 * @see CQLFilterBuilder
 * @see CQLCompiler
 * @see CQLParser
 * 
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.5
 *
 *
 *
 * @source $URL$
 */
public class TokenAdapter implements IToken {

    private final Token cqlToken;

    private TokenAdapter(Token token) {
        this.cqlToken = token;
    }

    public static IToken newAdapterFor(Token token) {
        return new TokenAdapter(token);
    }

    public String toString() {
        return this.cqlToken.toString();
    }

    public boolean hasNext() {
        return this.cqlToken.next != null;
    }

    public IToken next() {
        return newAdapterFor(this.cqlToken.next);
    }

    public int beginColumn() {
        return this.cqlToken.beginColumn;
    }

    public int endColumn() {
        return this.cqlToken.endColumn;
    }

    public Token getAdapted() {
        return this.cqlToken;
    }

}
