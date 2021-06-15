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
package org.hibernate.test.annotations.collectionelement;

import java.util.HashSet;
import java.util.Iterator;

import junit.framework.Assert;
import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

public class OrderByTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testOrderByName() throws Exception {
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		Products p = new Products();
		HashSet<Widgets> set = new HashSet<Widgets>();

		Widgets widget = new Widgets();
		widget.setName("hammer");
		set.add(widget);
		s.persist(widget);

		widget = new Widgets();
		widget.setName("axel");
		set.add(widget);
		s.persist(widget);

		widget = new Widgets();
		widget.setName("screwdriver");
		set.add(widget);
		s.persist(widget);

		p.setWidgets(set);
		s.persist(p);
		tx.commit();

		tx = s.beginTransaction();
		s.clear();
		p = (Products) s.get(Products.class,p.getId());
		Assert.assertTrue("has three Widgets", p.getWidgets().size() == 3);
		Iterator iter = p.getWidgets().iterator();
		Assert.assertEquals( "axel", ((Widgets)iter.next()).getName() );
		Assert.assertEquals( "hammer", ((Widgets)iter.next()).getName() );
		Assert.assertEquals( "screwdriver", ((Widgets)iter.next()).getName() );
		tx.commit();
		s.close();
	}

	@Test
	public void testOrderByWithDottedNotation() throws Exception {
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		BugSystem bs = new BugSystem();
		HashSet<Bug> set = new HashSet<Bug>();

		Bug bug = new Bug();
		bug.setDescription("JPA-2 locking");
		bug.setSummary("JPA-2 impl locking");
		Person p = new Person();
		p.setFirstName("Scott");
		p.setLastName("Marlow");
		bug.setReportedBy(p);
		set.add(bug);

		bug = new Bug();
		bug.setDescription("JPA-2 annotations");
		bug.setSummary("JPA-2 impl annotations");
		p = new Person();
		p.setFirstName("Emmanuel");
		p.setLastName("Bernard");
		bug.setReportedBy(p);
		set.add(bug);

		bug = new Bug();
		bug.setDescription("Implement JPA-2 criteria");
		bug.setSummary("JPA-2 impl criteria");
		p = new Person();
		p.setFirstName("Steve");
		p.setLastName("Ebersole");
		bug.setReportedBy(p);
		set.add(bug);

		bs.setBugs(set);
		s.persist(bs);
		tx.commit();

		tx = s.beginTransaction();
		s.clear();
		bs = (BugSystem) s.get(BugSystem.class,bs.getId());
		Assert.assertTrue("has three bugs", bs.getBugs().size() == 3);
		Iterator iter = bs.getBugs().iterator();
		Assert.assertEquals( "Emmanuel", ((Bug)iter.next()).getReportedBy().getFirstName() );
		Assert.assertEquals( "Steve", ((Bug)iter.next()).getReportedBy().getFirstName() );
		Assert.assertEquals( "Scott", ((Bug)iter.next()).getReportedBy().getFirstName() );
		tx.commit();
		s.close();

	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
			Products.class,
			Widgets.class,
			BugSystem.class
		};
	}

}
