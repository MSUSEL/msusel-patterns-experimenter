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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import org.jboss.logging.Logger;
import org.junit.Test;

import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;

import static org.junit.Assert.assertTrue;

/**
 * Test that we can upgrade locks
 *
 * @author Scott Marlow
 */
public class UpgradeLockTest extends BaseEntityManagerFunctionalTestCase {
	private static final Logger log = Logger.getLogger( UpgradeLockTest.class );

	/**
	 * Initially in tx1, get a LockModeType.READ and upgrade to LockModeType.OPTIMISTIC_FORCE_INCREMENT.
	 * To prove success, tx2, will modify the entity which should cause a failure in tx1.
	 *
	 * @throws Exception
	 */
	@Test
	public void testUpgradeReadLockToOptimisticForceIncrement() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		final EntityManager em2 = createIsolatedEntityManager();
		try {
			Lock lock = new Lock();  //
			lock.setName( "name" );


			em.getTransaction().begin(); // create the test entity first
			em.persist( lock );

			em.getTransaction().commit();

			em.getTransaction().begin(); // start tx1
			lock = em.getReference( Lock.class, lock.getId() );
			final Integer id = lock.getId();

			em.lock( lock, LockModeType.READ );  // start with READ lock in tx1
												 // upgrade to OPTIMISTIC_FORCE_INCREMENT in tx1
			em.lock( lock, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
			lock.setName( "surname" );		   // don't end tx1 yet

			final CountDownLatch latch = new CountDownLatch(1);
			Thread t = new Thread( new Runnable() {
				public void run() {
					try {
						em2.getTransaction().begin();  // start tx2
						Lock lock2 = em2.getReference( Lock.class, id );
						lock2.setName("renamed");	   // change entity
					}
					finally {
						em2.getTransaction().commit();
						latch.countDown();	// signal that tx2 is committed
						em2.close();
					}
				}
			} );

			t.setDaemon( true );
			t.setName("testUpgradeReadLockToOptimisticForceIncrement tx2");
			t.start();
			log.info("testUpgradeReadLockToOptimisticForceIncrement:  wait on BG thread");
			boolean latchSet = latch.await( 10, TimeUnit.SECONDS );
			assertTrue( "background test thread finished (lock timeout is broken)", latchSet );
			// tx2 is complete, try to commit tx1
			try {
				em.getTransaction().commit();
			}
			catch (Throwable expectedToFail) {
				while(expectedToFail != null &&
					!(expectedToFail instanceof javax.persistence.OptimisticLockException)) {
					expectedToFail = expectedToFail.getCause();
				}
				assertTrue("upgrade to OPTIMISTIC_FORCE_INCREMENT is expected to fail at end of transaction1 since tranaction2 already updated the entity",
						expectedToFail instanceof javax.persistence.OptimisticLockException);
			}
		}
		finally {
	 		em.close();
		}
	}


	@Override
	public Class[] getAnnotatedClasses() {
		return new Class[]{
				Lock.class,
				UnversionedLock.class
		};
	}
}
