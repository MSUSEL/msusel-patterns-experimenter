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
package org.hibernate.hql.internal;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.persister.collection.CollectionPropertyNames;

/**
 * Provides a map of collection function names to the corresponding property names.
 *
 * @author josh
 */
public final class CollectionProperties {
	public static final Map HQL_COLLECTION_PROPERTIES;

	private static final String COLLECTION_INDEX_LOWER = CollectionPropertyNames.COLLECTION_INDEX.toLowerCase();

	static {
		HQL_COLLECTION_PROPERTIES = new HashMap();
		HQL_COLLECTION_PROPERTIES.put( CollectionPropertyNames.COLLECTION_ELEMENTS.toLowerCase(), CollectionPropertyNames.COLLECTION_ELEMENTS );
		HQL_COLLECTION_PROPERTIES.put( CollectionPropertyNames.COLLECTION_INDICES.toLowerCase(), CollectionPropertyNames.COLLECTION_INDICES );
		HQL_COLLECTION_PROPERTIES.put( CollectionPropertyNames.COLLECTION_SIZE.toLowerCase(), CollectionPropertyNames.COLLECTION_SIZE );
		HQL_COLLECTION_PROPERTIES.put( CollectionPropertyNames.COLLECTION_MAX_INDEX.toLowerCase(), CollectionPropertyNames.COLLECTION_MAX_INDEX );
		HQL_COLLECTION_PROPERTIES.put( CollectionPropertyNames.COLLECTION_MIN_INDEX.toLowerCase(), CollectionPropertyNames.COLLECTION_MIN_INDEX );
		HQL_COLLECTION_PROPERTIES.put( CollectionPropertyNames.COLLECTION_MAX_ELEMENT.toLowerCase(), CollectionPropertyNames.COLLECTION_MAX_ELEMENT );
		HQL_COLLECTION_PROPERTIES.put( CollectionPropertyNames.COLLECTION_MIN_ELEMENT.toLowerCase(), CollectionPropertyNames.COLLECTION_MIN_ELEMENT );
		HQL_COLLECTION_PROPERTIES.put( COLLECTION_INDEX_LOWER, CollectionPropertyNames.COLLECTION_INDEX );
	}

	private CollectionProperties() {
	}

	public static boolean isCollectionProperty(String name) {
		String key = name.toLowerCase();
		// CollectionPropertyMapping processes everything except 'index'.
		if ( COLLECTION_INDEX_LOWER.equals( key ) ) {
			return false;
		}
		else {
			return HQL_COLLECTION_PROPERTIES.containsKey( key );
		}
	}

	public static String getNormalizedPropertyName(String name) {
		return ( String ) HQL_COLLECTION_PROPERTIES.get( name );
	}

	public static boolean isAnyCollectionProperty(String name) {
		String key = name.toLowerCase();
		return HQL_COLLECTION_PROPERTIES.containsKey( key );
	}
}
