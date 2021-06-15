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
package org.hibernate.test.manytomany;

import org.junit.Test;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Gavin King
 */
public class ManyToManyTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "manytomany/UserGroup.hbm.xml" };
	}

	@Override
	public void configure(Configuration cfg) {
		cfg.setProperty(Environment.USE_SECOND_LEVEL_CACHE, "false");
	}

	@Test
	public void testManyToManyWithFormula() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User gavin = new User("gavin", "jboss");
		Group seam = new Group("seam", "jboss");
		Group hb = new Group("hibernate", "jboss");
		gavin.getGroups().add(seam);
		gavin.getGroups().add(hb);
		seam.getUsers().add(gavin);
		hb.getUsers().add(gavin);
		s.persist(gavin);
		s.persist(seam);
		s.persist(hb);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		gavin = (User) s.get(User.class, gavin);
		assertFalse( Hibernate.isInitialized( gavin.getGroups() ) );
		assertEquals( 2, gavin.getGroups().size() );
		hb = (Group) s.get(Group.class, hb);
		assertFalse( Hibernate.isInitialized( hb.getUsers() ) );
		assertEquals( 1, hb.getUsers().size() );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		gavin = (User) s.createCriteria(User.class)
			.setFetchMode("groups", FetchMode.JOIN)
			.uniqueResult();
		assertTrue( Hibernate.isInitialized( gavin.getGroups() ) );
		assertEquals( 2, gavin.getGroups().size() );
		Group group = (Group) gavin.getGroups().iterator().next();
		assertFalse( Hibernate.isInitialized( group.getUsers() ) );
		assertEquals( 1, group.getUsers().size() );
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		gavin = (User) s.createCriteria(User.class)
			.setFetchMode("groups", FetchMode.JOIN)
			.setFetchMode("groups.users", FetchMode.JOIN)
			.uniqueResult();
		assertTrue( Hibernate.isInitialized( gavin.getGroups() ) );
		assertEquals( 2, gavin.getGroups().size() );
		group = (Group) gavin.getGroups().iterator().next();
		assertTrue( Hibernate.isInitialized( group.getUsers() ) );
		assertEquals( 1, group.getUsers().size() );
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		gavin = (User) s.createQuery("from User u join fetch u.groups g join fetch g.users").uniqueResult();
		assertTrue( Hibernate.isInitialized( gavin.getGroups() ) );
		assertEquals( 2, gavin.getGroups().size() );
		group = (Group) gavin.getGroups().iterator().next();
		assertTrue( Hibernate.isInitialized( group.getUsers() ) );
		assertEquals( 1, group.getUsers().size() );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		gavin = (User) s.get(User.class, gavin);
		hb = (Group) s.get(Group.class, hb);
		gavin.getGroups().remove(hb);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		gavin = (User) s.get(User.class, gavin);
		assertEquals( gavin.getGroups().size(), 1 );
		hb = (Group) s.get(Group.class, hb);
		assertEquals( hb.getUsers().size(), 0 );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		s.delete(gavin);
		s.flush();
		s.createQuery("delete from Group").executeUpdate();
		t.commit();
		s.close();
	}
}

