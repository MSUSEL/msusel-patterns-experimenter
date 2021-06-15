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

import org.hibernate.stat.EntityStatistics;

/**
 * Entity related statistics
 *
 * @author Alex Snaps
 */
public class ConcurrentEntityStatisticsImpl extends CategorizedStatistics implements EntityStatistics {

	ConcurrentEntityStatisticsImpl(String name) {
		super(name);
	}

	private	AtomicLong loadCount			  =	new	AtomicLong();
	private	AtomicLong updateCount			  =	new	AtomicLong();
	private	AtomicLong insertCount			  =	new	AtomicLong();
	private	AtomicLong deleteCount			  =	new	AtomicLong();
	private	AtomicLong fetchCount			  =	new	AtomicLong();
	private	AtomicLong optimisticFailureCount =	new	AtomicLong();

	public long getDeleteCount() {
		return deleteCount.get();
	}

	public long getInsertCount() {
		return insertCount.get();
	}

	public long getLoadCount() {
		return loadCount.get();
	}

	public long getUpdateCount() {
		return updateCount.get();
	}

	public long getFetchCount() {
		return fetchCount.get();
	}

	public long getOptimisticFailureCount() {
		return optimisticFailureCount.get();
	}

	public String toString() {
		return new StringBuilder()
				.append("EntityStatistics")
				.append("[loadCount=").append(this.loadCount)
				.append(",updateCount=").append(this.updateCount)
				.append(",insertCount=").append(this.insertCount)
				.append(",deleteCount=").append(this.deleteCount)
				.append(",fetchCount=").append(this.fetchCount)
				.append(",optimisticLockFailureCount=").append(this.optimisticFailureCount)
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

	void incrementInsertCount() {
		insertCount.getAndIncrement();
	}

	void incrementDeleteCount() {
		deleteCount.getAndIncrement();
	}

	void incrementOptimisticFailureCount() {
		optimisticFailureCount.getAndIncrement();
	}
}
