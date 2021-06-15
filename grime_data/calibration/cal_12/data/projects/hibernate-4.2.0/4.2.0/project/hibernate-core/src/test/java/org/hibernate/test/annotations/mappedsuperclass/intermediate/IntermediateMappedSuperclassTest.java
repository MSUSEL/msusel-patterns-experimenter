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
package org.hibernate.test.annotations.mappedsuperclass.intermediate;

import java.math.BigDecimal;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ebersole
 */
public class IntermediateMappedSuperclassTest extends BaseCoreFunctionalTestCase {
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { AccountBase.class, Account.class, SavingsAccountBase.class, SavingsAccount.class };
	}

	@Test
	public void testGetOnIntermediateMappedSuperclass() {
		final BigDecimal withdrawalLimit = new BigDecimal( 1000.00 ).setScale( 2 );
		Session session = openSession();
		session.beginTransaction();
		SavingsAccount savingsAccount = new SavingsAccount( "123", withdrawalLimit );
		session.save( savingsAccount );
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		Account account = (Account) session.get( Account.class, savingsAccount.getId() );
		// Oracle returns the BigDecimal with scale=0, which is equal to 1000 (not 1000.00);
		// compare using BigDecimal.doubleValue;
		assertEquals(
				withdrawalLimit.doubleValue(),
				( (SavingsAccount) account ).getWithdrawalLimit().doubleValue(),
				0.001);
		session.delete( account );
		session.getTransaction().commit();
		session.close();
	}
}
