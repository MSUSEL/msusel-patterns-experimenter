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
package org.hibernate.test.lazyonetoone;
import java.util.Date;

import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.bytecode.instrumentation.internal.FieldInterceptionHelper;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.testing.Skip;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Gavin King
 */
@Skip(
		condition = LazyOneToOneTest.DomainClassesInstrumentedMatcher.class,
		message = "Test domain classes were not instrumented"
)
public class LazyOneToOneTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "lazyonetoone/Person.hbm.xml" };
	}

	public void configure(Configuration cfg) {
		cfg.setProperty(Environment.MAX_FETCH_DEPTH, "2");
		cfg.setProperty(Environment.USE_SECOND_LEVEL_CACHE, "false");
	}

	@Test
	public void testLazy() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Person p = new Person("Gavin");
		Person p2 = new Person("Emmanuel");
		Employee e = new Employee(p);
		new Employment(e, "JBoss");
		Employment old = new Employment(e, "IFA");
		old.setEndDate( new Date() );
		s.persist(p);
		s.persist(p2);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		p = (Person) s.createQuery("from Person where name='Gavin'").uniqueResult();
		//assertFalse( Hibernate.isPropertyInitialized(p, "employee") );
		assertSame( p.getEmployee().getPerson(), p );
		assertTrue( Hibernate.isInitialized( p.getEmployee().getEmployments() ) );
		assertEquals( p.getEmployee().getEmployments().size(), 1 );
		p2 = (Person) s.createQuery("from Person where name='Emmanuel'").uniqueResult();
		assertNull( p2.getEmployee() );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		p = (Person) s.get(Person.class, "Gavin");
		//assertFalse( Hibernate.isPropertyInitialized(p, "employee") );
		assertSame( p.getEmployee().getPerson(), p );
		assertTrue( Hibernate.isInitialized( p.getEmployee().getEmployments() ) );
		assertEquals( p.getEmployee().getEmployments().size(), 1 );
		p2 = (Person) s.get(Person.class, "Emmanuel");
		assertNull( p2.getEmployee() );
		s.delete(p2);
		s.delete(old);
		s.delete(p);
		t.commit();
		s.close();
	}

	public static class DomainClassesInstrumentedMatcher implements Skip.Matcher {
		@Override
		public boolean isMatch() {
			return FieldInterceptionHelper.isInstrumented( Person.class );
		}
	}
}

