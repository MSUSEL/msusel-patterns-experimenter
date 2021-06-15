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
package org.hibernate.ejb;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class QueryHints {
	/**
	 * @deprecated HINT_TIMEOUT (org.hibernate.timeout),
	 * instead use SPEC_HINT_TIMEOUT (javax.persistence.query.timeout)
	 */
	public static final String HINT_TIMEOUT = "org.hibernate.timeout"; // Query timeout in seconds
	public static final String SPEC_HINT_TIMEOUT = "javax.persistence.query.timeout"; // timeout in milliseconds
	public static final String HINT_COMMENT = "org.hibernate.comment";
	public static final String HINT_FETCH_SIZE = "org.hibernate.fetchSize";
	public static final String HINT_CACHE_REGION = "org.hibernate.cacheRegion";
	public static final String HINT_CACHEABLE = "org.hibernate.cacheable";
	public static final String HINT_READONLY = "org.hibernate.readOnly";
	public static final String HINT_CACHE_MODE = "org.hibernate.cacheMode";
	public static final String HINT_FLUSH_MODE = "org.hibernate.flushMode";

	private static final Set<String> HINTS = buildHintsSet();

	private static Set<String> buildHintsSet() {
		HashSet<String> hints = new HashSet<String>();
		hints.add( HINT_TIMEOUT );
		hints.add( SPEC_HINT_TIMEOUT );
		hints.add( HINT_COMMENT );
		hints.add( HINT_FETCH_SIZE );
		hints.add( HINT_CACHE_REGION );
		hints.add( HINT_CACHEABLE );
		hints.add( HINT_READONLY );
		hints.add( HINT_CACHE_MODE );
		hints.add( HINT_FLUSH_MODE );
		return java.util.Collections.unmodifiableSet( hints );
	}

	public static Set<String> getDefinedHints() {
		return HINTS;
	}
}
