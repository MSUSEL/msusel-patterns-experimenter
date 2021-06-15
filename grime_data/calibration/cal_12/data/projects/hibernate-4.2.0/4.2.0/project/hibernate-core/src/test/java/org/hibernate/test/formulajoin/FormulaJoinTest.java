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
package org.hibernate.test.formulajoin;
import java.util.List;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Gavin King
 */
public class FormulaJoinTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "formulajoin/Master.hbm.xml" };
	}

	@Test
	public void testFormulaJoin() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Master master = new Master();
		master.setName("master 1");
		Detail current = new Detail();
		current.setCurrentVersion(true);
		current.setVersion(2);
		current.setDetails("details of master 1 blah blah");
		current.setMaster(master);
		master.setDetail(current);
		Detail past = new Detail();
		past.setCurrentVersion(false);
		past.setVersion(1);
		past.setDetails("old details of master 1 yada yada");
		past.setMaster(master);
		s.persist(master);
		s.persist(past);
		s.persist(current);
		tx.commit();
		s.close();
		
		if ( getDialect() instanceof PostgreSQLDialect  || getDialect() instanceof PostgreSQL81Dialect ) return;

		s = openSession();
		tx = s.beginTransaction();
		List l = s.createQuery("from Master m left join m.detail d").list();
		assertEquals( l.size(), 1 );
		tx.commit();
		s.close();
		
		s = openSession();
		tx = s.beginTransaction();
		l = s.createQuery("from Master m left join fetch m.detail").list();
		assertEquals( l.size(), 1 );
		Master m = (Master) l.get(0);
		assertEquals( "master 1", m.getDetail().getMaster().getName() );
		assertTrue( m==m.getDetail().getMaster() );
		tx.commit();
		s.close();
		
		s = openSession();
		tx = s.beginTransaction();
		l = s.createQuery("from Master m join fetch m.detail").list();
		assertEquals( l.size(), 1 );
		tx.commit();
		s.close();
		
		s = openSession();
		tx = s.beginTransaction();
		l = s.createQuery("from Detail d join fetch d.currentMaster.master").list();
		assertEquals( l.size(), 2 );
		tx.commit();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		l = s.createQuery("from Detail d join fetch d.currentMaster.master m join fetch m.detail").list();
		assertEquals( l.size(), 2 );
		
		s.createQuery("delete from Detail").executeUpdate();
		s.createQuery("delete from Master").executeUpdate();
		
		tx.commit();
		s.close();

	}

}

