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
package org.hibernate.test.collectionalias;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author Dave Stephan
 * @author Gail Badner
 */
public class CollectionAliasTest extends BaseCoreFunctionalTestCase {

	@TestForIssue( jiraKey = "HHH-7545" )
	@Test
	public void test() {
		Session s = openSession();
		s.getTransaction().begin();
		ATable aTable = new ATable( 1 );
		TableB tableB = new TableB(
			new TableBId( 1, "a", "b" )
		);
		aTable.getTablebs().add( tableB );
		tableB.setTablea( aTable );
		s.save( aTable );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		aTable = (ATable) s.createQuery( "select distinct	tablea from ATable tablea LEFT JOIN FETCH tablea.tablebs " ).uniqueResult();
		assertEquals( new Integer( 1 ), aTable.getFirstId() );
		assertEquals( 1, aTable.getTablebs().size() );
		tableB = aTable.getTablebs().get( 0 );
		assertSame( aTable, tableB.getTablea() );
		assertEquals( new Integer( 1 ), tableB.getId().getFirstId() );
		assertEquals( "a", tableB.getId().getSecondId() );
		assertEquals( "b", tableB.getId().getThirdId() );
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				TableBId.class,
				TableB.class,
				TableA.class,
				ATable.class
		};
	}

}
