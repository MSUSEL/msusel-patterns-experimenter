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
package org.hibernate.test.locking;

import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.PessimisticLockException;
import org.hibernate.Session;
import org.hibernate.dialect.SybaseASE15Dialect;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.LockAcquisitionException;
import org.hibernate.testing.SkipForDialect;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.async.Executable;
import org.hibernate.testing.async.TimedExecutor;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Make sure that directly specifying lock modes, even though deprecated, continues to work until removed.
 *
 * @author Steve Ebersole
 */
@TestForIssue( jiraKey = "HHH-5275")
@SkipForDialect(value=SybaseASE15Dialect.class, strictMatching=true,
		comment = "skip this test on Sybase ASE 15.5, but run it on 15.7, see HHH-6820")
public class LockModeTest extends BaseCoreFunctionalTestCase {
	private Long id;

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return  new Class[] { A.class };
	}

	@Override
	public void prepareTest() throws Exception {
		Session session = sessionFactory().openSession();
		session.beginTransaction();
		id = (Long) session.save( new A( "it" ) );
		session.getTransaction().commit();
		session.close();
	}
	@Override
	protected boolean isCleanupTestDataRequired(){return true;}

	@Test
	@SuppressWarnings( {"deprecation"})
	public void testLoading() {
		// open a session, begin a transaction and lock row
		Session s1 = sessionFactory().openSession();
		s1.beginTransaction();
		try {
			A it = (A) s1.get( A.class, id, LockMode.PESSIMISTIC_WRITE );
			// make sure we got it
			assertNotNull( it );

			// that initial transaction is still active and so the lock should still be held.
			// Lets open another session/transaction and verify that we cannot update the row
			nowAttemptToUpdateRow();
		}
		finally {
			s1.getTransaction().commit();
			s1.close();
		}
	}

	@Test
	public void testLegacyCriteria() {
		// open a session, begin a transaction and lock row
		Session s1 = sessionFactory().openSession();
		s1.beginTransaction();
		try {
			A it = (A) s1.createCriteria( A.class )
					.setLockMode( LockMode.PESSIMISTIC_WRITE )
					.uniqueResult();
			// make sure we got it
			assertNotNull( it );

			// that initial transaction is still active and so the lock should still be held.
			// Lets open another session/transaction and verify that we cannot update the row
			nowAttemptToUpdateRow();
		}
		finally {
			s1.getTransaction().commit();
			s1.close();
		}
	}

	@Test
	public void testLegacyCriteriaAliasSpecific() {
		// open a session, begin a transaction and lock row
		Session s1 = sessionFactory().openSession();
		s1.beginTransaction();
		try {
			A it = (A) s1.createCriteria( A.class )
					.setLockMode( "this", LockMode.PESSIMISTIC_WRITE )
					.uniqueResult();
			// make sure we got it
			assertNotNull( it );

			// that initial transaction is still active and so the lock should still be held.
			// Lets open another session/transaction and verify that we cannot update the row
			nowAttemptToUpdateRow();
		}
		finally {
			s1.getTransaction().commit();
			s1.close();
		}
	}

	@Test
	public void testQuery() {
		// open a session, begin a transaction and lock row
		Session s1 = sessionFactory().openSession();
		s1.beginTransaction();
		try {
			A it = (A) s1.createQuery( "from A a" )
					.setLockMode( "a", LockMode.PESSIMISTIC_WRITE )
					.uniqueResult();
			// make sure we got it
			assertNotNull( it );

			// that initial transaction is still active and so the lock should still be held.
			// Lets open another session/transaction and verify that we cannot update the row
			nowAttemptToUpdateRow();
		}
		finally {
			s1.getTransaction().commit();
			s1.close();
		}
	}

	@Test
	public void testQueryUsingLockOptions() {
		// todo : need an association here to make sure the alias-specific lock modes are applied correctly
		Session s1 = sessionFactory().openSession();
		s1.beginTransaction();
		s1.createQuery( "from A a" )
				.setLockOptions( new LockOptions( LockMode.PESSIMISTIC_WRITE ) )
				.uniqueResult();
		s1.createQuery( "from A a" )
				.setLockOptions( new LockOptions().setAliasSpecificLockMode( "a", LockMode.PESSIMISTIC_WRITE ) )
				.uniqueResult();
		s1.getTransaction().commit();
		s1.close();
	}

	private void nowAttemptToUpdateRow() {
		// here we just need to open a new connection (database session and transaction) and make sure that
		// we are not allowed to acquire exclusive locks to that row and/or write to that row.  That may take
		// one of two forms:
		//		1) either the get-with-lock or the update fails immediately with a sql error
		//		2) either the get-with-lock or the update blocks indefinitely (in real world, it would block
		//			until the txn in the calling method completed.
		// To be able to cater to the second type, we run this block in a separate thread to be able to "time it out"

		try {
			new TimedExecutor( 10*1000, 1*1000 ).execute(
					new Executable() {
						Session s;

						@Override
						public void execute() {
							s = sessionFactory().openSession();
							s.beginTransaction();
							try {
								// load with write lock to deal with databases that block (wait indefinitely) direct attempts
								// to write a locked row
								A it = (A) s.get(
										A.class,
										id,
										new LockOptions( LockMode.PESSIMISTIC_WRITE ).setTimeOut( LockOptions.NO_WAIT )
								);
								it.setValue( "changed" );
								s.flush();
								fail( "Pessimistic lock not obtained/held" );
							}
							catch ( Exception e ) {
								// grr, exception can be any number of types based on database
								// 		see HHH-6887
								if ( LockAcquisitionException.class.isInstance( e )
										|| GenericJDBCException.class.isInstance( e )
										|| PessimisticLockException.class.isInstance( e ) ) {
									// "ok"
								}
								else {
									fail( "Unexpected error type testing pessimistic locking : " + e.getClass().getName() );
								}
							}
							finally {
								shutDown();
							}
						}

						private void shutDown() {
							try {
								s.getTransaction().rollback();
								s.close();
							}
							catch (Exception ignore) {
							}
							s = null;
						}

						@Override
						public void timedOut() {
							s.cancelQuery();
							shutDown();
						}
					}
			);
		}
		catch (TimeoutException e) {
			// timeout is ok, see rule #2 above
		}
	}
}
