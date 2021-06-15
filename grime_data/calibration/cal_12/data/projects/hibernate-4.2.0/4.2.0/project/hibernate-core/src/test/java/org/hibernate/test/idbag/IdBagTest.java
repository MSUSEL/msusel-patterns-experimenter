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
package org.hibernate.test.idbag;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Gavin King
 */
public class IdBagTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "idbag/UserGroup.hbm.xml" };
	}

	@Test
	public void testUpdateIdBag() throws HibernateException, SQLException {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User gavin = new User("gavin");
		Group admins = new Group("admins");
		Group plebs = new Group("plebs");
		Group moderators = new Group("moderators");
		Group banned = new Group("banned");
		gavin.getGroups().add(plebs);
		//gavin.getGroups().add(moderators);
		s.persist(gavin);
		s.persist(plebs);
		s.persist(admins);
		s.persist(moderators);
		s.persist(banned);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		gavin = (User) s.createCriteria(User.class).uniqueResult();
		admins = (Group) s.load(Group.class, "admins");
		plebs = (Group) s.load(Group.class, "plebs");
		banned = (Group) s.load(Group.class, "banned");
		gavin.getGroups().add(admins);
		gavin.getGroups().remove(plebs);
		//gavin.getGroups().add(banned);

		s.delete(plebs);
		s.delete(banned);
		s.delete(moderators);
		s.delete(admins);
		s.delete(gavin);
		
		t.commit();
		s.close();		
	}

	@Test
	public void testJoin() throws HibernateException, SQLException {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User gavin = new User("gavin");
		Group admins = new Group("admins");
		Group plebs = new Group("plebs");
		gavin.getGroups().add(plebs);
		gavin.getGroups().add(admins);
		s.persist(gavin);
		s.persist(plebs);
		s.persist(admins);
		
		List l = s.createQuery("from User u join u.groups g").list();
		assertEquals( l.size(), 2 );
		s.clear();
		
		gavin = (User) s.createQuery("from User u join fetch u.groups").uniqueResult();
		assertTrue( Hibernate.isInitialized( gavin.getGroups() ) );
		assertEquals( gavin.getGroups().size(), 2 );
		assertEquals( ( (Group) gavin.getGroups().get(0) ).getName(), "admins" );
		
		s.delete( gavin.getGroups().get(0) );
		s.delete( gavin.getGroups().get(1) );
		s.delete(gavin);
		
		t.commit();
		s.close();		
	}

}

