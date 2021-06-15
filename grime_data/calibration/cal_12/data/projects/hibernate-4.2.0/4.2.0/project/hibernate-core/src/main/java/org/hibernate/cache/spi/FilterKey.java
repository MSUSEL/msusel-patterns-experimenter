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
package org.hibernate.cache.spi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.Filter;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.internal.FilterImpl;
import org.hibernate.type.Type;

/**
 * Allows cached queries to be keyed by enabled filters.
 * 
 * @author Gavin King
 */
public final class FilterKey implements Serializable {
	private String filterName;
	private Map<String,TypedValue> filterParameters = new HashMap<String,TypedValue>();
	
	public FilterKey(String name, Map<String,?> params, Map<String,Type> types) {
		filterName = name;
		for ( Map.Entry<String, ?> paramEntry : params.entrySet() ) {
			Type type = types.get( paramEntry.getKey() );
			filterParameters.put( paramEntry.getKey(), new TypedValue( type, paramEntry.getValue() ) );
		}
	}
	
	public int hashCode() {
		int result = 13;
		result = 37 * result + filterName.hashCode();
		result = 37 * result + filterParameters.hashCode();
		return result;
	}
	
	public boolean equals(Object other) {
		if ( !(other instanceof FilterKey) ) return false;
		FilterKey that = (FilterKey) other;
		if ( !that.filterName.equals(filterName) ) return false;
		if ( !that.filterParameters.equals(filterParameters) ) return false;
		return true;
	}
	
	public String toString() {
		return "FilterKey[" + filterName + filterParameters + ']';
	}
	
	public static Set<FilterKey> createFilterKeys(Map<String,Filter> enabledFilters) {
		if ( enabledFilters.size()==0 ) {
			return null;
		}
		Set<FilterKey> result = new HashSet<FilterKey>();
		for ( Filter filter : enabledFilters.values() ) {
			FilterKey key = new FilterKey(
					filter.getName(),
					( (FilterImpl) filter ).getParameters(),
					filter.getFilterDefinition().getParameterTypes()
			);
			result.add( key );
		}
		return result;
	}
}
