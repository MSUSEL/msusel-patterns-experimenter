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
package org.hibernate.test.annotations.inheritance.joined;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Emmanuel Bernard
 */
public class JoinedSubclassAndSecondaryTable extends BaseCoreFunctionalTestCase {
	@Test
	public void testSecondaryTableAndJoined() throws Exception {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		SwimmingPool sp = new SwimmingPool();
		s.persist( sp );
		s.flush();
		s.clear();

		long rowCount = getTableRowCount( s );
		assertEquals(
				"The address table is marked as optional. For null values no database row should be created",
				0,
				rowCount
		);

		SwimmingPool sp2 = (SwimmingPool) s.get( SwimmingPool.class, sp.getId() );
		assertEquals( sp.getAddress(), null );

		PoolAddress address = new PoolAddress();
		address.setAddress( "Park Avenue" );
		sp2.setAddress( address );

		s.flush();
		s.clear();

		sp2 = (SwimmingPool) s.get( SwimmingPool.class, sp.getId() );
		rowCount = getTableRowCount( s );
		assertEquals(
				"Now we should have a row in the pool address table ",
				1,
				rowCount
		);
		assertFalse( sp2.getAddress() == null );
		assertEquals( sp2.getAddress().getAddress(), "Park Avenue" );

		tx.rollback();
		s.close();
	}

	private long getTableRowCount(Session s) {
		// the type returned for count(*) in a native query depends on the dialect
		// Oracle returns Types.NUMERIC, which is mapped to BigDecimal;
		// H2 returns Types.BIGINT, which is mapped to BigInteger;
		Object retVal = s.createSQLQuery( "select count(*) from POOL_ADDRESS" ).uniqueResult();
		assertTrue( Number.class.isInstance( retVal ) );
		return ( ( Number ) retVal ).longValue();
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] { Pool.class, SwimmingPool.class };
	}
}
