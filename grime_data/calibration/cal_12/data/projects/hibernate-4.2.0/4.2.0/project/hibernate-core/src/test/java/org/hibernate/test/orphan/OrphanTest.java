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
package org.hibernate.test.orphan;

import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.internal.util.SerializationHelper;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Gavin King
 */
public class OrphanTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "orphan/Product.hbm.xml" };
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testOrphanDeleteOnDelete() {
		Session session = openSession();
		Transaction t = session.beginTransaction();
		Product prod = new Product();
		prod.setName("Widget");
		Part part = new Part();
		part.setName("Widge");
		part.setDescription("part if a Widget");
		prod.getParts().add(part);
		Part part2 = new Part();
		part2.setName("Get");
		part2.setDescription("another part if a Widget");
		prod.getParts().add(part2);
		session.persist(prod);
		session.flush();
		
		prod.getParts().remove(part);
		
		session.delete(prod);
		
		t.commit();
		session.close();
		
		session = openSession();
		t = session.beginTransaction();
		assertNull( session.get(Part.class, "Widge") );
		assertNull( session.get(Part.class, "Get") );
		assertNull( session.get(Product.class, "Widget") );
		t.commit();
		session.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testOrphanDeleteAfterPersist() {
		Session session = openSession();
		Transaction t = session.beginTransaction();
		Product prod = new Product();
		prod.setName("Widget");
		Part part = new Part();
		part.setName("Widge");
		part.setDescription("part if a Widget");
		prod.getParts().add(part);
		Part part2 = new Part();
		part2.setName("Get");
		part2.setDescription("another part if a Widget");
		prod.getParts().add(part2);
		session.persist(prod);
		
		prod.getParts().remove(part);
		
		t.commit();
		session.close();
		
		session = openSession();
		t = session.beginTransaction();
		assertNull( session.get(Part.class, "Widge") );
		assertNotNull( session.get(Part.class, "Get") );
		session.delete( session.get(Product.class, "Widget") );
		t.commit();
		session.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testOrphanDeleteAfterPersistAndFlush() {
		Session session = openSession();
		Transaction t = session.beginTransaction();
		Product prod = new Product();
		prod.setName("Widget");
		Part part = new Part();
		part.setName("Widge");
		part.setDescription("part if a Widget");
		prod.getParts().add(part);
		Part part2 = new Part();
		part2.setName("Get");
		part2.setDescription("another part if a Widget");
		prod.getParts().add(part2);
		session.persist(prod);
		session.flush();
		
		prod.getParts().remove(part);
		
		t.commit();
		session.close();
		
		session = openSession();
		t = session.beginTransaction();
		assertNull( session.get(Part.class, "Widge") );
		assertNotNull( session.get(Part.class, "Get") );
		session.delete( session.get(Product.class, "Widget") );
		t.commit();
		session.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testOrphanDeleteAfterLock() {
		Session session = openSession();
		Transaction t = session.beginTransaction();
		Product prod = new Product();
		prod.setName("Widget");
		Part part = new Part();
		part.setName("Widge");
		part.setDescription("part if a Widget");
		prod.getParts().add(part);
		Part part2 = new Part();
		part2.setName("Get");
		part2.setDescription("another part if a Widget");
		prod.getParts().add(part2);
		session.persist(prod);
		t.commit();
		session.close();
		
		
		session = openSession();
		t = session.beginTransaction();
		session.lock( prod, LockMode.READ );
		prod.getParts().remove(part);
		t.commit();
		session.close();
		
		session = openSession();
		t = session.beginTransaction();
		assertNull( session.get(Part.class, "Widge") );
		assertNotNull( session.get(Part.class, "Get") );
		session.delete( session.get(Product.class, "Widget") );
		t.commit();
		session.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testOrphanDeleteOnSaveOrUpdate() {
		Session session = openSession();
		Transaction t = session.beginTransaction();
		Product prod = new Product();
		prod.setName("Widget");
		Part part = new Part();
		part.setName("Widge");
		part.setDescription("part if a Widget");
		prod.getParts().add(part);
		Part part2 = new Part();
		part2.setName("Get");
		part2.setDescription("another part if a Widget");
		prod.getParts().add(part2);
		session.persist(prod);
		t.commit();
		session.close();
		
		prod.getParts().remove(part);
		
		session = openSession();
		t = session.beginTransaction();
		session.saveOrUpdate(prod);
		t.commit();
		session.close();
		
		session = openSession();
		t = session.beginTransaction();
		assertNull( session.get(Part.class, "Widge") );
		assertNotNull( session.get(Part.class, "Get") );
		session.delete( session.get(Product.class, "Widget") );
		t.commit();
		session.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testOrphanDeleteOnSaveOrUpdateAfterSerialization() {
		Session session = openSession();
		Transaction t = session.beginTransaction();
		Product prod = new Product();
		prod.setName("Widget");
		Part part = new Part();
		part.setName("Widge");
		part.setDescription("part if a Widget");
		prod.getParts().add(part);
		Part part2 = new Part();
		part2.setName("Get");
		part2.setDescription("another part if a Widget");
		prod.getParts().add(part2);
		session.persist(prod);
		t.commit();
		session.close();
		
		prod.getParts().remove(part);
		
		prod = (Product) SerializationHelper.clone( prod );
		
		session = openSession();
		t = session.beginTransaction();
		session.saveOrUpdate(prod);
		t.commit();
		session.close();
		
		session = openSession();
		t = session.beginTransaction();
		assertNull( session.get(Part.class, "Widge") );
		assertNotNull( session.get(Part.class, "Get") );
		session.delete( session.get(Product.class, "Widget") );
		t.commit();
		session.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testOrphanDelete() {
		Session session = openSession();
		Transaction t = session.beginTransaction();
		Product prod = new Product();
		prod.setName("Widget");
		Part part = new Part();
		part.setName("Widge");
		part.setDescription("part if a Widget");
		prod.getParts().add(part);
		Part part2 = new Part();
		part2.setName("Get");
		part2.setDescription("another part if a Widget");
		prod.getParts().add(part2);
		session.persist(prod);
		t.commit();
		session.close();
		
		sessionFactory().getCache().evictEntityRegion( Product.class );
		sessionFactory().getCache().evictEntityRegion( Part.class );

		
		session = openSession();
		t = session.beginTransaction();
		prod = (Product) session.get(Product.class, "Widget");
		assertTrue( Hibernate.isInitialized( prod.getParts() ) );
		part = (Part) session.get(Part.class, "Widge");
		prod.getParts().remove(part);
		t.commit();
		session.close();
		
		session = openSession();
		t = session.beginTransaction();
		assertNull( session.get(Part.class, "Widge") );
		assertNotNull( session.get(Part.class, "Get") );
		session.delete( session.get(Product.class, "Widget") );
		t.commit();
		session.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testOrphanDeleteOnMerge() {
		Session session = openSession();
		Transaction t = session.beginTransaction();
		Product prod = new Product();
		prod.setName("Widget");
		Part part = new Part();
		part.setName("Widge");
		part.setDescription("part if a Widget");
		prod.getParts().add(part);
		Part part2 = new Part();
		part2.setName("Get");
		part2.setDescription("another part if a Widget");
		prod.getParts().add(part2);
		session.persist(prod);
		t.commit();
		session.close();
		
		prod.getParts().remove(part);
		
		session = openSession();
		t = session.beginTransaction();
		session.merge(prod);
		t.commit();
		session.close();
		
		session = openSession();
		t = session.beginTransaction();
		assertNull( session.get(Part.class, "Widge") );
		assertNotNull( session.get(Part.class, "Get") );
		session.delete( session.get(Product.class, "Widget") );
		t.commit();
		session.close();
	}

}

