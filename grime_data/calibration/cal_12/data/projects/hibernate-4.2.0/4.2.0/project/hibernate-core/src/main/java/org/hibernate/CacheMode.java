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
package org.hibernate;

/**
 * Controls how the session interacts with the second-level
 * cache and query cache.
 *
 * @author Gavin King
 * @author Strong Liu
 * @see Session#setCacheMode(CacheMode)
 */
public enum CacheMode {

	/**
	 * The session may read items from the cache, and add items to the cache
	 */
	NORMAL( true, true),
	/**
	 * The session will never interact with the cache, except to invalidate
	 * cache items when updates occur
	 */
	IGNORE( false, false),
	/**
	 * The session may read items from the cache, but will not add items,
	 * except to invalidate items when updates occur
	 */
	GET( false, true),
	/**
	 * The session will never read items from the cache, but will add items
	 * to the cache as it reads them from the database.
	 */
	PUT( true, false),
	/**
	 * The session will never read items from the cache, but will add items
	 * to the cache as it reads them from the database. In this mode, the
	 * effect of <tt>hibernate.cache.use_minimal_puts</tt> is bypassed, in
	 * order to <em>force</em> a cache refresh
	 */
	REFRESH( true, false);


	private final boolean isPutEnabled;
	private final boolean isGetEnabled;

	CacheMode( boolean isPutEnabled, boolean isGetEnabled) {
		this.isPutEnabled = isPutEnabled;
		this.isGetEnabled = isGetEnabled;
	}

	public boolean isGetEnabled() {
		return isGetEnabled;
	}

	public boolean isPutEnabled() {
		return isPutEnabled;
	}
}
