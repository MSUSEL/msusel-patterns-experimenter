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
package org.hibernate.test.ondelete;

import java.util.List;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.stat.Statistics;
import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Gavin King
 */
public class OnDeleteTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "ondelete/Person.hbm.xml" };
	}

	@Override
	public void configure(Configuration cfg) {
		cfg.setProperty(Environment.GENERATE_STATISTICS, "true");
	}

	@Test
	@RequiresDialectFeature(
			value = DialectChecks.SupportsCircularCascadeDeleteCheck.class,
			comment = "db/dialect does not support circular cascade delete constraints"
	)
	public void testJoinedSubclass() {
		Statistics statistics = sessionFactory().getStatistics();
		statistics.clear();
		
		Session s = openSession();
		Transaction t = s.beginTransaction();
		
		Salesperson mark = new Salesperson();
		mark.setName("Mark");
		mark.setTitle("internal sales");
		mark.setSex('M');
		mark.setAddress("buckhead");
		mark.setZip("30305");
		mark.setCountry("USA");
		
		Person joe = new Person();
		joe.setName("Joe");
		joe.setAddress("San Francisco");
		joe.setZip("XXXXX");
		joe.setCountry("USA");
		joe.setSex('M');
		joe.setSalesperson(mark);
		mark.getCustomers().add(joe);
				
		s.save(mark);
		
		t.commit();
		
		assertEquals( statistics.getEntityInsertCount(), 2 );
		assertEquals( statistics.getPrepareStatementCount(), 5 );
		
		statistics.clear();
		
		t = s.beginTransaction();
		s.delete(mark);
		t.commit();

		assertEquals( statistics.getEntityDeleteCount(), 2 );
		if ( getDialect().supportsCascadeDelete() ) {
			assertEquals( statistics.getPrepareStatementCount(), 1 );
		}
		
		t = s.beginTransaction();
		List names = s.createQuery("select name from Person").list();
		assertTrue( names.isEmpty() );
		t.commit();

		s.close();
	}

}

