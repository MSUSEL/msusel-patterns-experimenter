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
package org.hibernate.test.join;

import java.util.List;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Chris Jones and Gail Badner
 */
public class OptionalJoinTest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "join/Thing.hbm.xml" };
	}

	@Test
	public void testUpdateNonNullOptionalJoinToDiffNonNull() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		// create a new thing with a non-null name
		Thing thing = new Thing();
		thing.setName("one");
		s.save(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		List things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = (Thing)things.get(0);
		assertEquals("one", thing.getName());
		// give it a new non-null name and save it
		thing.setName("one_changed");
		s.update(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = (Thing)things.get(0);
		assertEquals("one_changed", thing.getName());
		s.delete(thing);
		t.commit();
		s.close();
	}

	@Test
	public void testUpdateNonNullOptionalJoinToDiffNonNullDetached() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		// create a new thing with a non-null name
		Thing thing = new Thing();
		thing.setName("one");
		s.save(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		List things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = (Thing)things.get(0);
		assertEquals("one", thing.getName());
		t.commit();
		s.close();
				
		// change detached thing name to a new non-null name and save it
		thing.setName("one_changed");

		s = openSession();
		t = s.beginTransaction();
		s.update(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = (Thing)things.get(0);
		assertEquals("one_changed", thing.getName());
		s.delete(thing);
		t.commit();
		s.close();
	}

	@Test
	public void testMergeNonNullOptionalJoinToDiffNonNullDetached() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		// create a new thing with a non-null name
		Thing thing = new Thing();
		thing.setName("one");
		s.save(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		List things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = (Thing)things.get(0);
		assertEquals("one", thing.getName());
		t.commit();
		s.close();

		// change detached thing name to a new non-null name and save it
		thing.setName("one_changed");

		s = openSession();
		t = s.beginTransaction();
		s.merge(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = (Thing)things.get(0);
		assertEquals("one_changed", thing.getName());
		s.delete(thing);
		t.commit();
		s.close();
	}

	@Test
	public void testUpdateNonNullOptionalJoinToNull() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		// create a new thing with a non-null name
		Thing thing = new Thing();
		thing.setName("one");
		s.save(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		List things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = (Thing)things.get(0);
		assertEquals("one", thing.getName());
		// give it a null name and save it
		thing.setName(null);
		s.update(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = (Thing)things.get(0);
		assertNull(thing.getName());
		s.delete(thing);
		t.commit();
		s.close();
	}

	@Test
	public void testUpdateNonNullOptionalJoinToNullDetached() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		// create a new thing with a non-null name
		Thing thing = new Thing();
		thing.setName("one");
		s.save(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		List things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = (Thing)things.get(0);
		assertEquals("one", thing.getName());
		t.commit();
		s.close();

		// give detached thing a null name and save it
		thing.setName(null);

		s = openSession();
		t = s.beginTransaction();
		s.update(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = (Thing)things.get(0);
		assertNull(thing.getName());
		s.delete(thing);
		t.commit();
		s.close();
	}

	@Test
	public void testMergeNonNullOptionalJoinToNullDetached() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		// create a new thing with a non-null name
		Thing thing = new Thing();
		thing.setName("one");
		s.save(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		List things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = (Thing)things.get(0);
		assertEquals("one", thing.getName());
		t.commit();
		s.close();

		// give detached thing a null name and save it
		thing.setName(null);

		s = openSession();
		t = s.beginTransaction();
		s.merge(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = (Thing)things.get(0);
		assertNull(thing.getName());
		s.delete(thing);
		t.commit();
		s.close();
	}

	@Test
	public void testUpdateNullOptionalJoinToNonNull() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		// create a new thing with a null name
		Thing thing = new Thing();
		thing.setName(null);
		s.save(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		List things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = (Thing)things.get(0);
		assertNull(thing.getName());
		// change name to a non-null value
		thing.setName("two");
		s.update(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = ((Thing) things.get(0));
		assertEquals("two", thing.getName());
		s.delete(thing);
		t.commit();
		s.close();
	}

	@Test
	public void testUpdateNullOptionalJoinToNonNullDetached() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		// create a new thing with a null name
		Thing thing = new Thing();
		thing.setName(null);
		s.save(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		List things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = (Thing)things.get(0);
		assertNull(thing.getName());
		t.commit();
		s.close();

		// change detached thing name to a non-null value
		thing.setName("two");

		s = openSession();
		t = s.beginTransaction();
		s.update(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = ((Thing) things.get(0));
		assertEquals("two", thing.getName());
		s.delete(thing);
		t.commit();
		s.close();
	}

	@Test
	public void testMergeNullOptionalJoinToNonNullDetached() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		// create a new thing with a null name
		Thing thing = new Thing();
		thing.setName(null);
		s.save(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		List things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = (Thing)things.get(0);
		assertNull(thing.getName());
		t.commit();
		s.close();

		// change detached thing name to a non-null value
		thing.setName("two");

		s = openSession();
		t = s.beginTransaction();
		s.merge(thing);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		things = s.createQuery("from Thing").list();
		assertEquals(1, things.size());
		thing = ((Thing) things.get(0));
		assertEquals("two", thing.getName());
		s.delete(thing);
		t.commit();
		s.close();
	}
}
