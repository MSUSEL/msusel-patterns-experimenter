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
package org.hibernate.test.cache.infinispan.functional.cluster;

import javax.transaction.TransactionManager;

import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;
import org.infinispan.test.TestingUtil;
import org.jboss.logging.Logger;
import org.junit.Test;

import org.hibernate.SessionFactory;
import org.hibernate.cache.infinispan.InfinispanRegionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.test.cache.infinispan.functional.classloader.Account;
import org.hibernate.test.cache.infinispan.functional.classloader.ClassLoaderTestDAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * SessionRefreshTestCase.
 *
 * @author Galder Zamarreño
 * @since 3.5
 */
public class SessionRefreshTestCase extends DualNodeTestCase {
	private static final Logger log = Logger.getLogger( SessionRefreshTestCase.class );

	static int test = 0;
	private Cache localCache;

	@Override
	protected void configureSecondNode(Configuration cfg) {
		super.configureSecondNode( cfg );
		cfg.setProperty( Environment.USE_SECOND_LEVEL_CACHE, "false" );
	}

	@Override
	protected void standardConfigure(Configuration cfg) {
		super.standardConfigure( cfg );
		cfg.setProperty( InfinispanRegionFactory.ENTITY_CACHE_RESOURCE_PROP, getEntityCacheConfigName() );
	}

	protected String getEntityCacheConfigName() {
		return "entity";
	}

	@Override
	public String[] getMappings() {
		return new String[] {"cache/infinispan/functional/classloader/Account.hbm.xml"};
	}

	@Override
	protected void cleanupTransactionManagement() {
		// Don't clean up the managers, just the transactions
		// Managers are still needed by the long-lived caches
		DualNodeJtaTransactionManagerImpl.cleanupTransactions();
	}

	@Test
	public void testRefreshAfterExternalChange() throws Exception {
		// First session factory uses a cache
		CacheContainer localManager = ClusterAwareRegionFactory.getCacheManager( DualNodeTestCase.LOCAL );
		localCache = localManager.getCache( Account.class.getName() );
		TransactionManager localTM = DualNodeJtaTransactionManagerImpl.getInstance( DualNodeTestCase.LOCAL );
		SessionFactory localFactory = sessionFactory();

		// Second session factory doesn't; just needs a transaction manager
		TransactionManager remoteTM = DualNodeJtaTransactionManagerImpl.getInstance( DualNodeTestCase.REMOTE );
		SessionFactory remoteFactory = secondNodeEnvironment().getSessionFactory();

		ClassLoaderTestDAO dao0 = new ClassLoaderTestDAO( localFactory, localTM );
		ClassLoaderTestDAO dao1 = new ClassLoaderTestDAO( remoteFactory, remoteTM );

		Integer id = new Integer( 1 );
		dao0.createAccount( dao0.getSmith(), id, new Integer( 5 ), DualNodeTestCase.LOCAL );

		// Basic sanity check
		Account acct1 = dao1.getAccount( id );
		assertNotNull( acct1 );
		assertEquals( DualNodeTestCase.LOCAL, acct1.getBranch() );

		// This dao's session factory isn't caching, so cache won't see this change
		dao1.updateAccountBranch( id, DualNodeTestCase.REMOTE );

		// dao1's session doesn't touch the cache,
		// so reading from dao0 should show a stale value from the cache
		// (we check to confirm the cache is used)
		Account acct0 = dao0.getAccount( id );
		assertNotNull( acct0 );
		assertEquals( DualNodeTestCase.LOCAL, acct0.getBranch() );
		log.debug( "Contents when re-reading from local: " + TestingUtil.printCache( localCache ) );

		// Now call session.refresh and confirm we get the correct value
		acct0 = dao0.getAccountWithRefresh( id );
		assertNotNull( acct0 );
		assertEquals( DualNodeTestCase.REMOTE, acct0.getBranch() );
		log.debug( "Contents after refreshing in remote: " + TestingUtil.printCache( localCache ) );

		// Double check with a brand new session, in case the other session
		// for some reason bypassed the 2nd level cache
		ClassLoaderTestDAO dao0A = new ClassLoaderTestDAO( localFactory, localTM );
		Account acct0A = dao0A.getAccount( id );
		assertNotNull( acct0A );
		assertEquals( DualNodeTestCase.REMOTE, acct0A.getBranch() );
		log.debug( "Contents after creating a new session: " + TestingUtil.printCache( localCache ) );
	}
}
