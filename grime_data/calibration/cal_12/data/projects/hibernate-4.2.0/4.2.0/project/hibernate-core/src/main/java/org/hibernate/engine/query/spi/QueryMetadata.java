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
package org.hibernate.engine.query.spi;
import java.io.Serializable;
import java.util.Set;

import org.hibernate.type.Type;

/**
 * Defines metadata regarding a translated HQL or native-SQL query.
 *
 * @author Steve Ebersole
 */
public class QueryMetadata implements Serializable {
	private final String sourceQuery;
	private final ParameterMetadata parameterMetadata;
	private final String[] returnAliases;
	private final Type[] returnTypes;
	private final Set querySpaces;

	public QueryMetadata(
			String sourceQuery,
	        ParameterMetadata parameterMetadata,
	        String[] returnAliases,
	        Type[] returnTypes,
	        Set querySpaces) {
		this.sourceQuery = sourceQuery;
		this.parameterMetadata = parameterMetadata;
		this.returnAliases = returnAliases;
		this.returnTypes = returnTypes;
		this.querySpaces = querySpaces;
	}

	/**
	 * Get the source HQL or native-SQL query.
	 *
	 * @return The source query.
	 */
	public String getSourceQuery() {
		return sourceQuery;
	}

	public ParameterMetadata getParameterMetadata() {
		return parameterMetadata;
	}

	/**
	 * Return source query select clause aliases (if any)
	 *
	 * @return an array of aliases as strings.
	 */
	public String[] getReturnAliases() {
		return returnAliases;
	}

	/**
	 * An array of types describing the returns of the source query.
	 *
	 * @return The return type array.
	 */
	public Type[] getReturnTypes() {
		return returnTypes;
	}

	/**
	 * The set of query spaces affected by this source query.
	 *
	 * @return The set of query spaces.
	 */
	public Set getQuerySpaces() {
		return querySpaces;
	}
}
