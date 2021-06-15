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
package org.hibernate.ejb.test.lock;

import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.junit.Test;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.ejb.AvailableSettings;
import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;
import org.hibernate.internal.QueryImpl;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.TestForIssue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * no need to run this on DB matrix
 *
 * @author Strong Liu <stliu@hibernate.org>
 */
@RequiresDialect(H2Dialect.class)
public class LockTimeoutPropertyTest extends BaseEntityManagerFunctionalTestCase {
	@Override
	protected void addConfigOptions(Map options) {
		options.put( AvailableSettings.LOCK_TIMEOUT, "2000" );
	}

	@Test
	public void testLockTimeoutASNamedQueryHint(){
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		Query query = em.createNamedQuery( "getAll" );
		query.setLockMode( LockModeType.PESSIMISTIC_READ );
		int timeout = ((QueryImpl)(((org.hibernate.ejb.QueryImpl)query).getHibernateQuery())).getLockOptions().getTimeOut();
		assertEquals( 3000, timeout );
	}


	@Test
	@TestForIssue( jiraKey = "HHH-6256")
	public void testTimeoutHint(){
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		boolean b= em.getProperties().containsKey( AvailableSettings.LOCK_TIMEOUT );
		assertTrue( b );
		int timeout = Integer.valueOf( em.getProperties().get( AvailableSettings.LOCK_TIMEOUT ).toString() );
		assertEquals( 2000, timeout);
		org.hibernate.ejb.QueryImpl q = (org.hibernate.ejb.QueryImpl) em.createQuery( "select u from UnversionedLock u" );
		timeout = ((QueryImpl)q.getHibernateQuery()).getLockOptions().getTimeOut();
		assertEquals( 2000, timeout );

		Query query = em.createQuery( "select u from UnversionedLock u" );
		query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
		query.setHint( AvailableSettings.LOCK_TIMEOUT, 3000 );
		q = (org.hibernate.ejb.QueryImpl)query;
		timeout = ((QueryImpl)q.getHibernateQuery()).getLockOptions().getTimeOut();
		assertEquals( 3000, timeout );
		em.getTransaction().rollback();
		em.close();
	}


	@Override
	public Class[] getAnnotatedClasses() {
		return new Class[] {
				UnversionedLock.class
		};
	}
}
