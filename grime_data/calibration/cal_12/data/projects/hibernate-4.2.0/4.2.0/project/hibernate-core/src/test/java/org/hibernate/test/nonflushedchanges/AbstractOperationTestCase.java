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
package org.hibernate.test.nonflushedchanges;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.ConnectionReleaseMode;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.internal.StatefulPersistenceContext;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.NonFlushedChanges;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.transaction.internal.jta.CMTTransactionFactory;
import org.hibernate.internal.util.SerializationHelper;
import org.hibernate.testing.jta.TestingJtaBootstrap;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 *  (adapted this from "ops" tests version)
 *
 * @author Gail Badner
 * @author Steve Ebersole
 */
public abstract class AbstractOperationTestCase extends BaseCoreFunctionalTestCase {
	private Map oldToNewEntityRefs = new HashMap();

	public void configure(Configuration cfg) {
		super.configure( cfg );
		TestingJtaBootstrap.prepare( cfg.getProperties() );
		cfg.setProperty( Environment.TRANSACTION_STRATEGY, CMTTransactionFactory.class.getName() );
		cfg.setProperty( Environment.AUTO_CLOSE_SESSION, "true" );
		cfg.setProperty( Environment.FLUSH_BEFORE_COMPLETION, "true" );
		cfg.setProperty( Environment.RELEASE_CONNECTIONS, ConnectionReleaseMode.AFTER_STATEMENT.toString() );
		cfg.setProperty( Environment.GENERATE_STATISTICS, "true" );
		cfg.setProperty( Environment.STATEMENT_BATCH_SIZE, "0" );
	}

	public String[] getMappings() {
		return new String[] {
				"nonflushedchanges/Node.hbm.xml",
				"nonflushedchanges/Employer.hbm.xml",
				"nonflushedchanges/OptLockEntity.hbm.xml",
				"nonflushedchanges/OneToOne.hbm.xml",
				"nonflushedchanges/Competition.hbm.xml"
		};
	}

	@Override
    public String getCacheConcurrencyStrategy() {
		return null;
	}

	protected void clearCounts() {
		sessionFactory().getStatistics().clear();
	}

	protected void assertInsertCount(int expected) {
		int inserts = ( int ) sessionFactory().getStatistics().getEntityInsertCount();
		assertEquals( "unexpected insert count", expected, inserts );
	}

	protected void assertUpdateCount(int expected) {
		int updates = ( int ) sessionFactory().getStatistics().getEntityUpdateCount();
		assertEquals( "unexpected update counts", expected, updates );
	}

	protected void assertDeleteCount(int expected) {
		int deletes = ( int ) sessionFactory().getStatistics().getEntityDeleteCount();
		assertEquals( "unexpected delete counts", expected, deletes );
	}

	protected void assertFetchCount(int count) {
		int fetches = ( int ) sessionFactory().getStatistics().getEntityFetchCount();
		assertEquals( count, fetches );
	}

	@SuppressWarnings( {"unchecked"})
	protected Session applyNonFlushedChangesToNewSessionCloseOldSession(Session oldSession) {
		NonFlushedChanges nfc = ( ( SessionImplementor ) oldSession ).getNonFlushedChanges();
		byte[] bytes = SerializationHelper.serialize( nfc );
		NonFlushedChanges nfc2 = ( NonFlushedChanges ) SerializationHelper.deserialize( bytes );
		Session newSession = openSession();
		( ( SessionImplementor ) newSession ).applyNonFlushedChanges( nfc2 );
		oldToNewEntityRefs.clear();
		for ( Object o : ((SessionImplementor) oldSession).getPersistenceContext()
				.getEntitiesByKey()
				.entrySet() ) {
			Map.Entry entry = (Map.Entry) o;
			EntityKey entityKey = (EntityKey) entry.getKey();
			Object oldEntityRef = entry.getValue();
			oldToNewEntityRefs.put(
					oldEntityRef,
					((SessionImplementor) newSession).getPersistenceContext().getEntity( entityKey )
			);
		}
		for ( Object o : ((StatefulPersistenceContext) ((SessionImplementor) oldSession).getPersistenceContext())
				.getProxiesByKey()
				.entrySet() ) {
			Map.Entry entry = (Map.Entry) o;
			EntityKey entityKey = (EntityKey) entry.getKey();
			Object oldProxyRef = entry.getValue();
			oldToNewEntityRefs.put(
					oldProxyRef,
					((SessionImplementor) newSession).getPersistenceContext().getProxy( entityKey )
			);
		}

		oldSession.clear();
		oldSession.close();
		return newSession;
	}

//	protected void applyNonFlushedChangesToClearedSession(Session s) {
//		NonFlushedChanges nfc = ( ( SessionImplementor ) s ).getNonFlushedChanges();
//		byte[] bytes = SerializationHelper.serialize( nfc );
//		NonFlushedChanges nfc2 = ( NonFlushedChanges ) SerializationHelper.deserialize( bytes );
//		s.clear();
//		( ( SessionImplementor ) s ).applyNonFlushedChanges( nfc2 );
//	}

	@SuppressWarnings( {"unchecked"})
	protected Map getOldToNewEntityRefMap() {
		return Collections.unmodifiableMap( oldToNewEntityRefs );
	}
}
