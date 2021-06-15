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
package org.hibernate.cache.ehcache.management.impl;


import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.openmbean.TabularData;

import net.sf.ehcache.hibernate.management.api.HibernateStats;

/**
 * Implementation of {@link HibernateStats} that does nothing
 * <p/>
 * <p/>
 *
 * @author <a href="mailto:asanoujam@terracottatech.com">Abhishek Sanoujam</a>
 */
public final class NullHibernateStats implements HibernateStats {

	/**
	 * Singleton instance.
	 */
	public static final HibernateStats INSTANCE = new NullHibernateStats();

	/**
	 * private constructor. No need to create instances of this. Use singleton instance
	 */
	private NullHibernateStats() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#clearStats()
	 */
	public void clearStats() {
		// no-op

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#disableStats()
	 */
	public void disableStats() {
		// no-op

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#enableStats()
	 */
	public void enableStats() {
		// no-op

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#getCloseStatementCount()
	 */
	public long getCloseStatementCount() {
		// no-op
		return 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#getCollectionStats()
	 */
	public TabularData getCollectionStats() {
		// no-op
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#getConnectCount()
	 */
	public long getConnectCount() {
		// no-op
		return 0;
	}

	/**
	 * Not supported right now
	 */
	public long getDBSQLExecutionSample() {
		// no-op
		return 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#getEntityStats()
	 */
	public TabularData getEntityStats() {
		// no-op
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#getFlushCount()
	 */
	public long getFlushCount() {
		// no-op
		return 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#getOptimisticFailureCount()
	 */
	public long getOptimisticFailureCount() {
		// no-op
		return 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#getPrepareStatementCount()
	 */
	public long getPrepareStatementCount() {
		// no-op
		return 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#getQueryExecutionCount()
	 */
	public long getQueryExecutionCount() {
		// no-op
		return 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#getQueryExecutionRate()
	 */
	public double getQueryExecutionRate() {
		// no-op
		return 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#getQueryExecutionSample()
	 */
	public long getQueryExecutionSample() {
		// no-op
		return 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#getQueryStats()
	 */
	public TabularData getQueryStats() {
		// no-op
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#getSessionCloseCount()
	 */
	public long getSessionCloseCount() {
		// no-op
		return 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#getSessionOpenCount()
	 */
	public long getSessionOpenCount() {
		// no-op
		return 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#getSuccessfulTransactionCount()
	 */
	public long getSuccessfulTransactionCount() {
		// no-op
		return 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#getTransactionCount()
	 */
	public long getTransactionCount() {
		// no-op
		return 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#isStatisticsEnabled()
	 */
	public boolean isStatisticsEnabled() {
		// no-op
		return false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see HibernateStats#setStatisticsEnabled(boolean)
	 */
	public void setStatisticsEnabled(boolean flag) {
		// no-op
	}

	/**
	 * @see HibernateStats#getCacheRegionStats()
	 */
	public TabularData getCacheRegionStats() {
		return null;
	}

	/**
	 * @see javax.management.NotificationEmitter#removeNotificationListener(javax.management.NotificationListener, javax.management.NotificationFilter, java.lang.Object)
	 */
	public void removeNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback)
			throws ListenerNotFoundException {
		/**/
	}

	/**
	 * @see javax.management.NotificationBroadcaster#addNotificationListener(javax.management.NotificationListener, javax.management.NotificationFilter, java.lang.Object)
	 */
	public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback)
			throws IllegalArgumentException {
		/**/
	}

	/**
	 * @see javax.management.NotificationBroadcaster#getNotificationInfo()
	 */
	public MBeanNotificationInfo[] getNotificationInfo() {
		return null;
	}

	/**
	 * @see javax.management.NotificationBroadcaster#removeNotificationListener(javax.management.NotificationListener)
	 */
	public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
		/**/
	}
}
