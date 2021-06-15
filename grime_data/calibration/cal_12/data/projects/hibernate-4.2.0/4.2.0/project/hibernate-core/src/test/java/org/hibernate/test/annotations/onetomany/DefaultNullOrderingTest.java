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
package org.hibernate.test.annotations.onetomany;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue(jiraKey = "HHH-465")
@RequiresDialect(value = H2Dialect.class,
		comment = "By default H2 places NULL values first, so testing 'NULLS LAST' expression.")
public class DefaultNullOrderingTest extends BaseCoreFunctionalTestCase {
	@Override
	protected void configure(Configuration configuration) {
		configuration.setProperty( AvailableSettings.DEFAULT_NULL_ORDERING, "last" );
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] { Monkey.class, Troop.class, Soldier.class };
	}

	@Test
	public void testHqlDefaultNullOrdering() {
		Session session = openSession();

		// Populating database with test data.
		session.getTransaction().begin();
		Monkey monkey1 = new Monkey();
		monkey1.setName( null );
		Monkey monkey2 = new Monkey();
		monkey2.setName( "Warsaw ZOO" );
		session.persist( monkey1 );
		session.persist( monkey2 );
		session.getTransaction().commit();

		session.getTransaction().begin();
		List<Zoo> orderedResults = (List<Zoo>) session.createQuery( "from Monkey m order by m.name" ).list(); // Should order by NULLS LAST.
		Assert.assertEquals( Arrays.asList( monkey2, monkey1 ), orderedResults );
		session.getTransaction().commit();

		session.clear();

		// Cleanup data.
		session.getTransaction().begin();
		session.delete( monkey1 );
		session.delete( monkey2 );
		session.getTransaction().commit();

		session.close();
	}

	@Test
	public void testAnnotationsDefaultNullOrdering() {
		Session session = openSession();

		// Populating database with test data.
		session.getTransaction().begin();
		Troop troop = new Troop();
		troop.setName( "Alpha 1" );
		Soldier ranger = new Soldier();
		ranger.setName( "Ranger 1" );
		troop.addSoldier( ranger );
		Soldier sniper = new Soldier();
		sniper.setName( null );
		troop.addSoldier( sniper );
		session.persist( troop );
		session.getTransaction().commit();

		session.clear();

		session.getTransaction().begin();
		troop = (Troop) session.get( Troop.class, troop.getId() );
		Iterator<Soldier> iterator = troop.getSoldiers().iterator(); // Should order by NULLS LAST.
		Assert.assertEquals( ranger.getName(), iterator.next().getName() );
		Assert.assertNull( iterator.next().getName() );
		session.getTransaction().commit();

		session.clear();

		// Cleanup data.
		session.getTransaction().begin();
		session.delete( troop );
		session.getTransaction().commit();

		session.close();
	}

	@Test
	public void testCriteriaDefaultNullOrdering() {
		Session session = openSession();

		// Populating database with test data.
		session.getTransaction().begin();
		Monkey monkey1 = new Monkey();
		monkey1.setName( null );
		Monkey monkey2 = new Monkey();
		monkey2.setName( "Berlin ZOO" );
		session.persist( monkey1 );
		session.persist( monkey2 );
		session.getTransaction().commit();

		session.getTransaction().begin();
		Criteria criteria = session.createCriteria( Monkey.class );
		criteria.addOrder( org.hibernate.criterion.Order.asc( "name" ) ); // Should order by NULLS LAST.
		Assert.assertEquals( Arrays.asList( monkey2, monkey1 ), criteria.list() );
		session.getTransaction().commit();

		session.clear();

		// Cleanup data.
		session.getTransaction().begin();
		session.delete( monkey1 );
		session.delete( monkey2 );
		session.getTransaction().commit();

		session.close();
	}
}
