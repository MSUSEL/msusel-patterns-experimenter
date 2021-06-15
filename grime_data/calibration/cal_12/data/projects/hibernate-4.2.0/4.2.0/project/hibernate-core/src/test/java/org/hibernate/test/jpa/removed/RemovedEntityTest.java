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
package org.hibernate.test.jpa.removed;

import java.math.BigDecimal;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.test.jpa.AbstractJPATest;
import org.hibernate.test.jpa.Item;
import org.hibernate.test.jpa.Part;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Steve Ebersole
 */
public class RemovedEntityTest extends AbstractJPATest {
	@Test
	public void testRemoveThenContains() {
		Session s = openSession();
		s.beginTransaction();
		Item item = new Item();
		item.setName( "dummy" );
		s.persist( item );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.delete( item );
		boolean contains = s.contains( item );
		s.getTransaction().commit();
		s.close();

		assertFalse( "expecting removed entity to not be contained", contains );
	}

	@Test
	public void testRemoveThenGet() {
		Session s = openSession();
		s.beginTransaction();
		Item item = new Item();
		item.setName( "dummy" );
		s.persist( item );
		s.getTransaction().commit();
		s.close();

		Long id = item.getId();

		s = openSession();
		s.beginTransaction();
		s.delete( item );
		item = ( Item ) s.get( Item.class, id );
		s.getTransaction().commit();
		s.close();

		assertNull( "expecting removed entity to be returned as null from get()", item );
	}

	@Test
	public void testRemoveThenSave() {
		Session s = openSession();
		s.beginTransaction();
		Item item = new Item();
		item.setName( "dummy" );
		s.persist( item );
		s.getTransaction().commit();
		s.close();

		Long id = item.getId();

		s = openSession();
		s.beginTransaction();
		item = ( Item ) s.get( Item.class, id );
		String sessionAsString = s.toString();

		s.delete( item );

		Item item2 = ( Item ) s.get( Item.class, id );
		assertNull( "expecting removed entity to be returned as null from get()", item2 );

		s.persist( item );
		assertEquals( "expecting session to be as it was before", sessionAsString, s.toString() );

		item.setName("Rescued");
		item = ( Item ) s.get( Item.class, id );
		assertNotNull( "expecting rescued entity to be returned from get()", item );

		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		item = ( Item ) s.get( Item.class, id );
		s.getTransaction().commit();
		s.close();

		assertNotNull( "expecting removed entity to be returned as null from get()", item );
		assertEquals("Rescued", item.getName());

		// clean up
		s = openSession();
		s.beginTransaction();
		s.delete( item );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testRemoveThenSaveWithCascades() {
		Session s = openSession();
		s.beginTransaction();

		Item item = new Item();
		item.setName( "dummy" );
		Part part = new Part(item, "child", "1234", BigDecimal.ONE);

		// persist cascades to part
		s.persist( item );

		// delete cascades to part also
		s.delete( item );
		assertFalse( "the item is contained in the session after deletion", s.contains( item ) );
		assertFalse( "the part is contained in the session after deletion", s.contains( part ) );

		// now try to persist again as a "unschedule removal" operation
		s.persist( item );
		assertTrue( "the item is contained in the session after deletion", s.contains( item ) );
		assertTrue( "the part is contained in the session after deletion", s.contains( part ) );

		s.getTransaction().commit();
		s.close();

		// clean up
		s = openSession();
		s.beginTransaction();
		s.delete( item );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testRemoveChildThenFlushWithCascadePersist() {
		Session s = openSession();
		s.beginTransaction();

		Item item = new Item();
		item.setName( "dummy" );
		Part child = new Part(item, "child", "1234", BigDecimal.ONE);

		// persist cascades to part
		s.persist( item );

		// delete the part
		s.delete( child );
		assertFalse("the child is contained in the session, since it is deleted", s.contains(child) );

		// now try to flush, which will attempt to cascade persist again to child.
		s.flush();
		assertTrue("Now it is consistent again since if was cascade-persisted by the flush()", s.contains(child));

		s.getTransaction().commit();
		s.close();

		// clean up
		s = openSession();
		s.beginTransaction();
		s.delete( item );
		s.getTransaction().commit();
		s.close();
	}
}
