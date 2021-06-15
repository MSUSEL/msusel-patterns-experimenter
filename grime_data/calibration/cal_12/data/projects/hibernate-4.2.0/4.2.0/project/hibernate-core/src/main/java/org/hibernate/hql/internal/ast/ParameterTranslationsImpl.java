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
package org.hibernate.hql.internal.ast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.hql.spi.ParameterTranslations;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.param.NamedParameterSpecification;
import org.hibernate.param.ParameterSpecification;
import org.hibernate.param.PositionalParameterSpecification;
import org.hibernate.type.Type;

/**
 * Defines the information available for parameters encountered during
 * query translation through the antlr-based parser.
 *
 * @author Steve Ebersole
 */
public class ParameterTranslationsImpl implements ParameterTranslations {

	private final Map namedParameters;
	private final ParameterInfo[] ordinalParameters;

	public boolean supportsOrdinalParameterMetadata() {
		return true;
	}

	public int getOrdinalParameterCount() {
		return ordinalParameters.length;
	}

	public ParameterInfo getOrdinalParameterInfo(int ordinalPosition) {
		// remember that ordinal parameters numbers are 1-based!!!
		return ordinalParameters[ordinalPosition - 1];
	}

	public int getOrdinalParameterSqlLocation(int ordinalPosition) {
		return getOrdinalParameterInfo( ordinalPosition ).getSqlLocations()[0];
	}

	public Type getOrdinalParameterExpectedType(int ordinalPosition) {
		return getOrdinalParameterInfo( ordinalPosition ).getExpectedType();
	}

	public Set getNamedParameterNames() {
		return namedParameters.keySet();
	}

	public ParameterInfo getNamedParameterInfo(String name) {
		return ( ParameterInfo ) namedParameters.get( name );
	}

	public int[] getNamedParameterSqlLocations(String name) {
		return getNamedParameterInfo( name ).getSqlLocations();
	}

	public Type getNamedParameterExpectedType(String name) {
		return getNamedParameterInfo( name ).getExpectedType();
	}

	/**
	 * Constructs a parameter metadata object given a list of parameter
	 * specifications.
	 * </p>
	 * Note: the order in the incoming list denotes the parameter's
	 * psudeo-position within the resulting sql statement.
	 *
	 * @param parameterSpecifications
	 */
	public ParameterTranslationsImpl(List parameterSpecifications) {

		class NamedParamTempHolder {
			String name;
			Type type;
			List positions = new ArrayList();
		}

		int size = parameterSpecifications.size();
		List ordinalParameterList = new ArrayList();
		Map namedParameterMap = new HashMap();
		for ( int i = 0; i < size; i++ ) {
			final ParameterSpecification spec = ( ParameterSpecification ) parameterSpecifications.get( i );
			if ( PositionalParameterSpecification.class.isAssignableFrom( spec.getClass() ) ) {
				PositionalParameterSpecification ordinalSpec = ( PositionalParameterSpecification ) spec;
				ordinalParameterList.add( new ParameterInfo( i, ordinalSpec.getExpectedType() ) );
			}
			else if ( NamedParameterSpecification.class.isAssignableFrom( spec.getClass() ) ) {
				NamedParameterSpecification namedSpec = ( NamedParameterSpecification ) spec;
				NamedParamTempHolder paramHolder = ( NamedParamTempHolder ) namedParameterMap.get( namedSpec.getName() );
				if ( paramHolder == null ) {
					paramHolder = new NamedParamTempHolder();
					paramHolder.name = namedSpec.getName();
					paramHolder.type = namedSpec.getExpectedType();
					namedParameterMap.put( namedSpec.getName(), paramHolder );
				}
				paramHolder.positions.add( i );
			}
			else {
				// don't care about other param types here, just those explicitly user-defined...
			}
		}

		ordinalParameters = ( ParameterInfo[] ) ordinalParameterList.toArray( new ParameterInfo[ordinalParameterList.size()] );

		if ( namedParameterMap.isEmpty() ) {
			namedParameters = java.util.Collections.EMPTY_MAP;
		}
		else {
			Map namedParametersBacking = new HashMap( namedParameterMap.size() );
			Iterator itr = namedParameterMap.values().iterator();
			while( itr.hasNext() ) {
				final NamedParamTempHolder holder = ( NamedParamTempHolder ) itr.next();
				namedParametersBacking.put(
						holder.name,
				        new ParameterInfo( ArrayHelper.toIntArray( holder.positions ), holder.type )
				);
			}
			namedParameters = java.util.Collections.unmodifiableMap( namedParametersBacking );
		}
	}

	public static class ParameterInfo implements Serializable {
		private final int[] sqlLocations;
		private final Type expectedType;

		public ParameterInfo(int[] sqlPositions, Type expectedType) {
			this.sqlLocations = sqlPositions;
			this.expectedType = expectedType;
		}

		public ParameterInfo(int sqlPosition, Type expectedType) {
			this.sqlLocations = new int[] { sqlPosition };
			this.expectedType = expectedType;
		}

		public int[] getSqlLocations() {
			return sqlLocations;
		}

		public Type getExpectedType() {
			return expectedType;
		}
	}
}
