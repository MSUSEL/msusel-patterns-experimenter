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

/**
 * Defines contract for regions which hold transactionally-managed data.
 * <p/>
 * The data is not transactionally managed within the region; merely it is
 * transactionally-managed in relation to its association with a particular
 * {@link org.hibernate.Session}.
 *
 * @author Steve Ebersole
 */
public interface TransactionalDataRegion extends Region {
	/**
	 * Is the underlying cache implementation aware of (and "participating in")
	 * ongoing JTA transactions?
	 * <p/>
	 * Regions which report that they are transaction-aware are considered
	 * "synchronous", in that we assume we can immediately (i.e. synchronously)
	 * write the changes to the cache and that the cache will properly manage
	 * application of the written changes within the bounds of ongoing JTA
	 * transactions.  Conversely, regions reporting false are considered
	 * "asynchronous", where it is assumed that changes must be manually
	 * delayed by Hibernate until we are certain that the current transaction
	 * is successful (i.e. maintaining READ_COMMITTED isolation).
	 *
	 * @return True if transaction aware; false otherwise.
	 */
	public boolean isTransactionAware();

	public CacheDataDescription getCacheDataDescription();
}
