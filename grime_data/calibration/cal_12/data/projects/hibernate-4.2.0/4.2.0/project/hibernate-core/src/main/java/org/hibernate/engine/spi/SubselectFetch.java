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
package org.hibernate.engine.spi;

import java.util.Map;
import java.util.Set;

import org.hibernate.internal.util.StringHelper;
import org.hibernate.persister.entity.Loadable;
import org.hibernate.persister.entity.PropertyMapping;

/**
 * @author Gavin King
 */
public class SubselectFetch {
	private final Set resultingEntityKeys;
	private final String queryString;
	private final String alias;
	private final Loadable loadable;
	private final QueryParameters queryParameters;
	private final Map namedParameterLocMap;

	public SubselectFetch(
		//final String queryString,
		final String alias,
		final Loadable loadable,
		final QueryParameters queryParameters,
		final Set resultingEntityKeys,
		final Map namedParameterLocMap
	) {
		this.resultingEntityKeys = resultingEntityKeys;
		this.queryParameters = queryParameters;
		this.namedParameterLocMap = namedParameterLocMap;
		this.loadable = loadable;
		this.alias = alias;

		//TODO: ugly here:
		final String queryString = queryParameters.getFilteredSQL();
		int fromIndex = queryString.indexOf(" from ");
		int orderByIndex = queryString.lastIndexOf("order by");
		this.queryString = orderByIndex>0 ?
				queryString.substring(fromIndex, orderByIndex) :
				queryString.substring(fromIndex);

	}

	public QueryParameters getQueryParameters() {
		return queryParameters;
	}

	/**
	 * Get the Set of EntityKeys
	 */
	public Set getResult() {
		return resultingEntityKeys;
	}

	public String toSubselectString(String ukname) {

		String[] joinColumns = ukname==null ?
			StringHelper.qualify( alias, loadable.getIdentifierColumnNames() ) :
			( (PropertyMapping) loadable ).toColumns(alias, ukname);

		return new StringBuilder()
			.append("select ")
			.append( StringHelper.join(", ", joinColumns) )
			.append(queryString)
			.toString();
	}

	@Override
    public String toString() {
		return "SubselectFetch(" + queryString + ')';
	}

	public Map getNamedParameterLocMap() {
		return namedParameterLocMap;
	}

}
