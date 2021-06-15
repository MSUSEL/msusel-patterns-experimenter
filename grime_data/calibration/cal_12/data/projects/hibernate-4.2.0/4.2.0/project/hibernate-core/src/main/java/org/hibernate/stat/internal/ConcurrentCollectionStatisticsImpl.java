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
package org.hibernate.stat.internal;

import java.util.concurrent.atomic.AtomicLong;

import org.hibernate.stat.CollectionStatistics;

/**
 * Collection related statistics
 *
 * @author Alex Snaps
 */
public class ConcurrentCollectionStatisticsImpl extends CategorizedStatistics implements CollectionStatistics {
	ConcurrentCollectionStatisticsImpl(String role) {
		super(role);
	}

	private	AtomicLong loadCount	 = new AtomicLong();
	private	AtomicLong fetchCount	 = new AtomicLong();
	private	AtomicLong updateCount	 = new AtomicLong();
	private	AtomicLong removeCount	 = new AtomicLong();
	private	AtomicLong recreateCount = new AtomicLong();

	public long getLoadCount() {
		return loadCount.get();
	}

	public long getFetchCount() {
		return fetchCount.get();
	}

	public long getRecreateCount() {
		return recreateCount.get();
	}

	public long getRemoveCount() {
		return removeCount.get();
	}

	public long getUpdateCount() {
		return updateCount.get();
	}

	public String toString() {
		return new StringBuilder()
				.append("CollectionStatistics")
				.append("[loadCount=").append(this.loadCount)
				.append(",fetchCount=").append(this.fetchCount)
				.append(",recreateCount=").append(this.recreateCount)
				.append(",removeCount=").append(this.removeCount)
				.append(",updateCount=").append(this.updateCount)
				.append(']')
				.toString();
	}

	void incrementLoadCount() {
		loadCount.getAndIncrement();
	}

	void incrementFetchCount() {
		fetchCount.getAndIncrement();
	}

	void incrementUpdateCount() {
		updateCount.getAndIncrement();
	}

	void incrementRecreateCount() {
		recreateCount.getAndIncrement();
	}

	void incrementRemoveCount() {
		removeCount.getAndIncrement();
	}
}
