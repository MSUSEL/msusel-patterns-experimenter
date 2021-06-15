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
package org.hibernate.ejb.util;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;

import org.hibernate.CacheMode;

/**
 * Helper to deal with {@link CacheMode} <-> {@link CacheRetrieveMode}/{@link CacheStoreMode}
 * conversions.
 *
 * @author Steve Ebersole
 */
public class CacheModeHelper {
	public static final CacheMode DEFAULT_LEGACY_MODE = CacheMode.NORMAL;
	public static final CacheStoreMode DEFAULT_STORE_MODE = CacheStoreMode.USE;
	public static final CacheRetrieveMode DEFAULT_RETRIEVE_MODE = CacheRetrieveMode.USE;

	/**
	 * Given a JPA {@link CacheStoreMode} and {@link CacheRetrieveMode}, determine the corresponding
	 * legacy Hibernate {@link CacheMode}.
	 *
	 * @param storeMode The JPA shared-cache store mode.
	 * @param retrieveMode The JPA shared-cache retrieve mode.
	 *
	 * @return Corresponding {@link CacheMode}.
	 */
	public static CacheMode interpretCacheMode(CacheStoreMode storeMode, CacheRetrieveMode retrieveMode) {
		if ( storeMode == null ) {
			storeMode = DEFAULT_STORE_MODE;
		}
		if ( retrieveMode == null ) {
			retrieveMode = DEFAULT_RETRIEVE_MODE;
		}

		final boolean get = ( CacheRetrieveMode.USE == retrieveMode );

		switch ( storeMode ) {
			case USE: {
				return get ? CacheMode.NORMAL : CacheMode.PUT;
			}
			case REFRESH: {
				// really (get == true) here is a bit of an invalid combo...
				return CacheMode.REFRESH;
			}
			case BYPASS: {
				return get ? CacheMode.GET : CacheMode.IGNORE;
			}
			default: {
				throw new IllegalStateException( "huh? :)" );
			}
		}
	}

	public static CacheStoreMode interpretCacheStoreMode(CacheMode cacheMode) {
		if ( cacheMode == null ) {
			cacheMode = DEFAULT_LEGACY_MODE;
		}

		if ( CacheMode.REFRESH == cacheMode ) {
			return CacheStoreMode.REFRESH;
		}
		if ( CacheMode.NORMAL == cacheMode || CacheMode.PUT == cacheMode ) {
			return CacheStoreMode.USE;
		}
		return CacheStoreMode.BYPASS;
	}

	public static CacheRetrieveMode interpretCacheRetrieveMode(CacheMode cacheMode) {
		if ( cacheMode == null ) {
			cacheMode = DEFAULT_LEGACY_MODE;
		}

		return ( CacheMode.NORMAL == cacheMode || CacheMode.GET == cacheMode )
				? CacheRetrieveMode.USE
				: CacheRetrieveMode.BYPASS;
	}
}
