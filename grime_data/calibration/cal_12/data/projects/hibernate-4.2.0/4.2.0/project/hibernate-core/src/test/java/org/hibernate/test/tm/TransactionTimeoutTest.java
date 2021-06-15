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
package org.hibernate.test.tm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.test.jdbc.Person;
import org.hibernate.testing.SkipForDialect;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue(jiraKey = "HHH-6780")
@SkipForDialect( value ={ PostgreSQL81Dialect.class, PostgreSQLDialect.class}, comment = "PostgreSQL jdbc driver doesn't impl timeout method")
public class TransactionTimeoutTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] {"jdbc/Mappings.hbm.xml"};
	}

	@Test
	public void testJdbcCoordinatorTransactionTimeoutCheck() {
		Session session = openSession();
		Transaction transaction = session.getTransaction();
		transaction.setTimeout( 2 );
		assertEquals( -1, ((SessionImplementor)session).getTransactionCoordinator().getJdbcCoordinator().determineRemainingTransactionTimeOutPeriod() );
		transaction.begin();
		assertNotSame( -1, ((SessionImplementor)session).getTransactionCoordinator().getJdbcCoordinator().determineRemainingTransactionTimeOutPeriod() );
		transaction.commit();
		session.close();
	}

	@Test(expected = TransactionException.class)
	public void testTransactionTimeoutFailure() throws InterruptedException {
		Session session = openSession();
		Transaction transaction = session.getTransaction();
		transaction.setTimeout( 1 );
		assertEquals( -1, ((SessionImplementor)session).getTransactionCoordinator().getJdbcCoordinator().determineRemainingTransactionTimeOutPeriod() );
		transaction.begin();
		Thread.sleep( 1000 );
		session.persist( new Person( "Lukasz", "Antoniak" ) );
		transaction.commit();
		session.close();
	}

	@Test
	public void testTransactionTimeoutSuccess() {
		Session session = openSession();
		Transaction transaction = session.getTransaction();
		transaction.setTimeout( 60 );
		transaction.begin();
		session.persist( new Person( "Lukasz", "Antoniak" ) );
		transaction.commit();
		session.close();
	}
}
