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
package org.hibernate.test.map;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Gavin King
 */
public class MapIndexFormulaTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "map/UserGroup.hbm.xml" };
	}

	@Test
	public void testIndexFunctionOnManyToManyMap() {
		Session s = openSession();
		s.beginTransaction();
		s.createQuery( "from Group g join g.users u where g.name = 'something' and index(u) = 'nada'" )
				.list();
		s.createQuery( "from Group g join g.users u where g.name = 'something' and minindex(u) = 'nada'" )
				.list();
		s.createQuery( "from Group g join g.users u where g.name = 'something' and maxindex(u) = 'nada'" )
				.list();
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"unchecked", "UnnecessaryBoxing"})
	public void testIndexFormulaMap() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User gavin = new User("gavin", "secret");
		User turin = new User("turin", "tiger");
		Group g = new Group("developers");
		g.getUsers().put("gavin", gavin);
		g.getUsers().put("turin", turin);
		s.persist(g);
		gavin.getSession().put( "foo", new SessionAttribute("foo", "foo bar baz") );
		gavin.getSession().put( "bar", new SessionAttribute("bar", "foo bar baz 2") );
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		g = (Group) s.get(Group.class, "developers");
		assertEquals( g.getUsers().size(), 2 );
		g.getUsers().remove("turin");
		Map smap = ( (User) g.getUsers().get("gavin") ).getSession();
		assertEquals(smap.size(), 2);
		smap.remove("bar");
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		g = (Group) s.get(Group.class, "developers");
		assertEquals( g.getUsers().size(), 1 );
		smap = ( (User) g.getUsers().get("gavin") ).getSession();
		assertEquals(smap.size(), 1);
		gavin = (User) g.getUsers().put("gavin", turin);
		s.delete(gavin);
		assertEquals( s.createQuery("select count(*) from SessionAttribute").uniqueResult(), new Long(0) );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		g = (Group) s.get(Group.class, "developers");
		assertEquals( g.getUsers().size(), 1 );
		turin = (User) g.getUsers().get("turin");
		smap = turin.getSession();
		assertEquals(smap.size(), 0);
		assertEquals( s.createQuery("select count(*) from User").uniqueResult(), Long.valueOf(1) );
		s.delete(g);
		s.delete(turin);
		assertEquals( s.createQuery("select count(*) from User").uniqueResult(), Long.valueOf( 0 ) );
		t.commit();
		s.close();
	}
	
	@Test
	@SuppressWarnings( {"unchecked"})
	public void testSQLQuery() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User gavin = new User("gavin", "secret");
		User turin = new User("turin", "tiger");
		gavin.getSession().put( "foo", new SessionAttribute("foo", "foo bar baz") );
		gavin.getSession().put( "bar", new SessionAttribute("bar", "foo bar baz 2") );
		s.persist(gavin);
		s.persist(turin);
		s.flush();
		s.clear();
		List results = s.getNamedQuery("userSessionData").setParameter("uname", "%in").list();
		assertEquals( results.size(), 2 );
		gavin = (User) ( (Object[]) results.get(0) )[0];
		assertEquals( gavin.getName(), "gavin" );
		assertEquals( gavin.getSession().size(), 2 );
		s.createQuery("delete SessionAttribute").executeUpdate();
		s.createQuery("delete User").executeUpdate();
		t.commit();
		s.close();
	}

}

