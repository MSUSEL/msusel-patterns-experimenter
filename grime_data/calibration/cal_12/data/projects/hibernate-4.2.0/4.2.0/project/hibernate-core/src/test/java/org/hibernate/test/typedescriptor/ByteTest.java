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
package org.hibernate.test.typedescriptor;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class ByteTest extends BaseCoreFunctionalTestCase {
	public static final byte TEST_VALUE = 65;

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] {
				VariousTypesEntity.class
		};
	}

	@Test
	@TestForIssue( jiraKey = "HHH-6533" )
	public void testByteDataPersistenceAndRetrieval() {
		Session session = openSession();
		Transaction transaction = session.beginTransaction();
		VariousTypesEntity entity = new VariousTypesEntity();
		entity.setId( 1 );
		entity.setByteData( TEST_VALUE );
		session.persist( entity );
		transaction.commit();
		session.close();

		// Testing sample value.
		session = openSession();
		transaction = session.beginTransaction();
		entity = (VariousTypesEntity) session.createQuery(
				" from VariousTypesEntity " +
						" where byteData = org.hibernate.test.typedescriptor.ByteTest.TEST_VALUE "
		).uniqueResult();
		Assert.assertNotNull( entity );
		Assert.assertEquals( TEST_VALUE, entity.getByteData() );
		entity.setByteData( Byte.MIN_VALUE );
		session.update( entity );
		transaction.commit();
		session.close();

		// Testing minimal value.
		session = openSession();
		transaction = session.beginTransaction();
		entity = (VariousTypesEntity) session.createQuery(
				" from VariousTypesEntity " +
						" where byteData = java.lang.Byte.MIN_VALUE "
		).uniqueResult();
		Assert.assertNotNull( entity );
		Assert.assertEquals( Byte.MIN_VALUE, entity.getByteData() );
		entity.setByteData( Byte.MAX_VALUE );
		session.update( entity );
		transaction.commit();
		session.close();

		// Testing maximal value.
		session = openSession();
		transaction = session.beginTransaction();
		entity = (VariousTypesEntity) session.createQuery(
				" from VariousTypesEntity " +
						" where byteData = java.lang.Byte.MAX_VALUE "
		).uniqueResult();
		Assert.assertNotNull( entity );
		Assert.assertEquals( Byte.MAX_VALUE, entity.getByteData() );
		transaction.commit();
		session.close();
	}
}
