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
package org.hibernate.test.optlock;


import org.junit.Test;

import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.hibernate.StaleStateException;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.fail;

/**
 * Tests relating to the optimistic-lock mapping option.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
@RequiresDialectFeature(
		value = DialectChecks.DoesRepeatableReadNotCauseReadersToBlockWritersCheck.class,
		comment = "potential deadlock"
)
public class OptimisticLockTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "optlock/Document.hbm.xml" };
	}
	
	@Test
	public void testOptimisticLockDirty() {
		testUpdateOptimisticLockFailure( "LockDirty" );
	}

	@Test
	public void testOptimisticLockAll() {
		testUpdateOptimisticLockFailure( "LockAll" );
	}

	@Test
	public void testOptimisticLockDirtyDelete() {
		testDeleteOptimisticLockFailure( "LockDirty" );
	}

	@Test
	public void testOptimisticLockAllDelete() {
		testDeleteOptimisticLockFailure( "LockAll" );
	}

	private void testUpdateOptimisticLockFailure(String entityName) {
		Session mainSession = openSession();
		mainSession.beginTransaction();
		Document doc = new Document();
		doc.setTitle( "Hibernate in Action" );
		doc.setAuthor( "Bauer et al" );
		doc.setSummary( "Very boring book about persistence" );
		doc.setText( "blah blah yada yada yada" );
		doc.setPubDate( new PublicationDate( 2004 ) );
		mainSession.save( entityName, doc );
		mainSession.getTransaction().commit();
		mainSession.close();

		mainSession = openSession();
		mainSession.beginTransaction();
		doc = ( Document ) mainSession.get( entityName, doc.getId() );

		Session otherSession = sessionFactory().openSession();
		otherSession.beginTransaction();
		Document otherDoc = ( Document ) otherSession.get( entityName, doc.getId() );
		otherDoc.setSummary( "A modern classic" );
		otherSession.getTransaction().commit();
		otherSession.close();

		try {
			doc.setSummary( "A machiavellian achievement of epic proportions" );
			mainSession.flush();
			fail( "expecting opt lock failure" );
		}
		catch ( StaleObjectStateException expected ) {
			// expected result...
		}
		catch( StaleStateException expected ) {
			// expected result (if using versioned batching)...
		}
		catch( JDBCException e ) {
			// SQLServer will report this condition via a SQLException
			// when using its SNAPSHOT transaction isolation...
			if ( ! ( getDialect() instanceof SQLServerDialect && e.getErrorCode() == 3960 ) ) {
				throw e;
			}
			else {
				// it seems to "lose track" of the transaction as well...
				mainSession.getTransaction().rollback();
				mainSession.beginTransaction();
			}
		}
		mainSession.clear();
		mainSession.getTransaction().commit();
		mainSession.close();

		mainSession = openSession();
		mainSession.beginTransaction();
		doc = ( Document ) mainSession.load( entityName, doc.getId() );
		mainSession.delete( entityName, doc );
		mainSession.getTransaction().commit();
		mainSession.close();
	}

	@SuppressWarnings({ "UnnecessaryBoxing" })
	private void testDeleteOptimisticLockFailure(String entityName) {
		Session mainSession = openSession();
		mainSession.beginTransaction();
		Document doc = new Document();
		doc.setTitle( "Hibernate in Action" );
		doc.setAuthor( "Bauer et al" );
		doc.setSummary( "Very boring book about persistence" );
		doc.setText( "blah blah yada yada yada" );
		doc.setPubDate( new PublicationDate( 2004 ) );
		mainSession.save( entityName, doc );
		mainSession.flush();
		doc.setSummary( "A modern classic" );
		mainSession.flush();
		doc.getPubDate().setMonth( Integer.valueOf( 3 ) );
		mainSession.flush();
		mainSession.getTransaction().commit();
		mainSession.close();

		mainSession = openSession();
		mainSession.beginTransaction();
		doc = ( Document ) mainSession.get( entityName, doc.getId() );

		Session otherSession = openSession();
		otherSession.beginTransaction();
		Document otherDoc = ( Document ) otherSession.get( entityName, doc.getId() );
		otherDoc.setSummary( "my other summary" );
		otherSession.flush();
		otherSession.getTransaction().commit();
		otherSession.close();

		try {
			mainSession.delete( doc );
			mainSession.flush();
			fail( "expecting opt lock failure" );
		}
		catch ( StaleObjectStateException e ) {
			// expected
		}
		catch( StaleStateException expected ) {
			// expected result (if using versioned batching)...
		}
		catch( JDBCException e ) {
			// SQLServer will report this condition via a SQLException
			// when using its SNAPSHOT transaction isolation...
			if ( ! ( getDialect() instanceof SQLServerDialect && e.getErrorCode() == 3960 ) ) {
				throw e;
			}
			else {
				// it seems to "lose track" of the transaction as well...
				mainSession.getTransaction().rollback();
				mainSession.beginTransaction();
			}
		}
		mainSession.clear();
		mainSession.getTransaction().commit();
		mainSession.close();

		mainSession = openSession();
		mainSession.beginTransaction();
		doc = ( Document ) mainSession.load( entityName, doc.getId() );
		mainSession.delete( entityName, doc );
		mainSession.getTransaction().commit();
		mainSession.close();
	}

}

