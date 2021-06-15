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
package org.hibernate.engine.internal;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.hibernate.engine.spi.SessionImplementor;

/**
 * Tracks non-nullable transient entities that would cause a particular entity insert to fail.
 *
 * @author Gail Badner
 */
public class NonNullableTransientDependencies {

	// Multiple property paths can refer to the same transient entity, so use Set<String>
	// for the map value.
	private final Map<Object,Set<String>> propertyPathsByTransientEntity =
			new IdentityHashMap<Object,Set<String>>();

	/* package-protected */
	void add(String propertyName, Object transientEntity) {
		Set<String> propertyPaths = propertyPathsByTransientEntity.get( transientEntity );
		if ( propertyPaths == null ) {
			propertyPaths = new HashSet<String>();
			propertyPathsByTransientEntity.put( transientEntity, propertyPaths );
		}
		propertyPaths.add( propertyName );
	}

	public Iterable<Object> getNonNullableTransientEntities() {
		return propertyPathsByTransientEntity.keySet();
	}

	public Iterable<String> getNonNullableTransientPropertyPaths(Object entity) {
		return propertyPathsByTransientEntity.get( entity );
	}

	public boolean isEmpty() {
		return propertyPathsByTransientEntity.isEmpty();
	}

	public void resolveNonNullableTransientEntity(Object entity) {
		if ( propertyPathsByTransientEntity.remove( entity ) == null ) {
			throw new IllegalStateException( "Attempt to resolve a non-nullable, transient entity that is not a dependency." );
		}
	}

	public String toLoggableString(SessionImplementor session) {
		StringBuilder sb = new StringBuilder( getClass().getSimpleName() ).append( '[' );
		for ( Map.Entry<Object,Set<String>> entry : propertyPathsByTransientEntity.entrySet() ) {
			sb.append( "transientEntityName=" ).append( session.bestGuessEntityName( entry.getKey() ) );
			sb.append( " requiredBy=" ).append( entry.getValue() );
		}
		sb.append( ']' );
		return sb.toString();
	}
}
