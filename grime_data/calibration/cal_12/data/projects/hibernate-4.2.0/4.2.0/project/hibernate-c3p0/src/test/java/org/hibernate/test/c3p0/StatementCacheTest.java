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
package org.hibernate.test.c3p0;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.*;

import java.util.List;

/**
 * Tests that when using cached prepared statement with batching enabled doesn't bleed over into new transactions. 
 * 
 * @author Shawn Clowater
 */
public class StatementCacheTest extends BaseCoreFunctionalTestCase {
	@Test
	@TestForIssue( jiraKey = "HHH-7193" )
	public void testStatementCaching() {
		Session session = openSession();
		session.beginTransaction();

		//save 2 new entities, one valid, one invalid (neither should be persisted)
		IrrelevantEntity irrelevantEntity = new IrrelevantEntity();
		irrelevantEntity.setName( "valid 1" );
		session.save( irrelevantEntity );
		//name is required
		irrelevantEntity = new IrrelevantEntity();
		session.save( irrelevantEntity );
		try {
			session.flush();
			Assert.fail( "Validation exception did not occur" );
		}
		catch (Exception e) {
			//this is expected roll the transaction back
			session.getTransaction().rollback();
		}
		session.close();

		session = openSession();
		session.beginTransaction();

		//save a new entity and commit it
		irrelevantEntity = new IrrelevantEntity();
		irrelevantEntity.setName( "valid 2" );
		session.save( irrelevantEntity );
		session.flush();
		session.getTransaction().commit();
		session.close();

		//only one entity should have been inserted to the database (if the statement in the cache wasn't cleared then it would have inserted both entities)
		session = openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria( IrrelevantEntity.class );
		List results = criteria.list();
		session.getTransaction().commit();
		session.close();

		Assert.assertEquals( 1, results.size() );
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[]{ IrrelevantEntity.class };
	}
}
