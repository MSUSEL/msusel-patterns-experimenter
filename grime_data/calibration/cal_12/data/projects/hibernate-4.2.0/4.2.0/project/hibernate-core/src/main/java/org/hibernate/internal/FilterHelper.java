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
package org.hibernate.internal;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.sql.Template;

/**
 * Implementation of FilterHelper.
 *
 * @author Steve Ebersole
 * @author Rob Worsnop
 */
public class FilterHelper {

	private final String[] filterNames;
	private final String[] filterConditions;
	private final boolean[] filterAutoAliasFlags;
	private final Map<String,String>[] filterAliasTableMaps;

	/**
	 * The map of defined filters.  This is expected to be in format
	 * where the filter names are the map keys, and the defined
	 * conditions are the values.
	 *
	 * @param filters The map of defined filters.
	 * @param dialect The sql dialect
	 * @param factory The session factory
	 */
	public FilterHelper(List filters, SessionFactoryImplementor factory) {
		int filterCount = filters.size();
		filterNames = new String[filterCount];
		filterConditions = new String[filterCount];
		filterAutoAliasFlags = new boolean[filterCount];
		filterAliasTableMaps = new Map[filterCount];
		Iterator iter = filters.iterator();
		filterCount = 0;
		while ( iter.hasNext() ) {
			filterAutoAliasFlags[filterCount] = false;
			final FilterConfiguration filter = (FilterConfiguration) iter.next();
			filterNames[filterCount] = (String) filter.getName();
			filterConditions[filterCount] = filter.getCondition();
			filterAliasTableMaps[filterCount] = filter.getAliasTableMap(factory);
			if ((filterAliasTableMaps[filterCount].isEmpty() || isTableFromPersistentClass(filterAliasTableMaps[filterCount])) && filter.useAutoAliasInjection()){
				filterConditions[filterCount] = Template.renderWhereStringTemplate(
						filter.getCondition(),
						FilterImpl.MARKER,
						factory.getDialect(),
						factory.getSqlFunctionRegistry()
					);
				filterAutoAliasFlags[filterCount] = true;
			}
			filterConditions[filterCount] = StringHelper.replace(
					filterConditions[filterCount],
					":",
					":" + filterNames[filterCount] + "."
			);
			filterCount++;
		}
	}
	
	private static boolean isTableFromPersistentClass(Map<String,String> aliasTableMap){
		return aliasTableMap.size() == 1 && aliasTableMap.containsKey(null);
	}

	public boolean isAffectedBy(Map enabledFilters) {
		for ( int i = 0, max = filterNames.length; i < max; i++ ) {
			if ( enabledFilters.containsKey( filterNames[i] ) ) {
				return true;
			}
		}
		return false;
	}

	public String render(FilterAliasGenerator aliasGenerator, Map enabledFilters) {
		StringBuilder buffer = new StringBuilder();
		render( buffer, aliasGenerator, enabledFilters );
		return buffer.toString();
	}

	public void render(StringBuilder buffer, FilterAliasGenerator aliasGenerator, Map enabledFilters) {
		if ( filterNames != null && filterNames.length > 0 ) {
			for ( int i = 0, max = filterNames.length; i < max; i++ ) {
				if ( enabledFilters.containsKey( filterNames[i] ) ) {
					final String condition = filterConditions[i];
					if ( StringHelper.isNotEmpty( condition ) ) {
						buffer.append(" and " ).append(render(aliasGenerator, i));
					}
				}
			}
		}
	}
	
	private String render(FilterAliasGenerator aliasGenerator, int filterIndex){
		Map<String,String> aliasTableMap = filterAliasTableMaps[filterIndex];
		String condition = filterConditions[filterIndex];
		if (filterAutoAliasFlags[filterIndex]){
			return StringHelper.replace(condition, FilterImpl.MARKER, aliasGenerator.getAlias(aliasTableMap.get(null)));
		} else if (isTableFromPersistentClass(aliasTableMap)){
			return condition.replace("{alias}", aliasGenerator.getAlias(aliasTableMap.get(null)));
		} else {
			for (Map.Entry<String, String> entry : aliasTableMap.entrySet()){
				condition = condition.replace("{"+entry.getKey()+"}", aliasGenerator.getAlias(entry.getValue()));
			}
			return condition;
		}
	}
}
