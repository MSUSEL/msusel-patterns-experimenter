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
package org.hibernate.test.deletetransient;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ebersole
 */
public class DeleteTransientEntityTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "deletetransient/Person.hbm.xml" };
	}
	@Override
	protected boolean isCleanupTestDataRequired() {
		return true;
	}

	@Test
	public void testTransientEntityDeletionNoCascades() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		s.delete( new Address() );
		t.commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testTransientEntityDeletionCascadingToTransientAssociation() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Person p = new Person();
		p.getAddresses().add( new Address() );
		s.delete( p );
		t.commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testTransientEntityDeleteCascadingToCircularity() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Person p1 = new Person();
		Person p2 = new Person();
		p1.getFriends().add( p2 );
		p2.getFriends().add( p1 );
		s.delete( p1 );
		t.commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testTransientEntityDeletionCascadingToDetachedAssociation() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Address address = new Address();
		address.setInfo( "123 Main St." );
		s.save( address );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		Person p = new Person();
		p.getAddresses().add( address );
		s.delete( p );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		Long count = ( Long ) s.createQuery( "select count(*) from Address" ).list().get( 0 );
		assertEquals( "delete not cascaded properly across transient entity", 0, count.longValue() );
		t.commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testTransientEntityDeletionCascadingToPersistentAssociation() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Address address = new Address();
		address.setInfo( "123 Main St." );
		s.save( address );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		address = ( Address ) s.get( Address.class, address.getId() );
		Person p = new Person();
		p.getAddresses().add( address );
		s.delete( p );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		Long count = ( Long ) s.createQuery( "select count(*) from Address" ).list().get( 0 );
		assertEquals( "delete not cascaded properly across transient entity", 0, count.longValue() );
		t.commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testCascadeAllFromClearedPersistentAssnToTransientEntity() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Person p = new Person();
		Address address = new Address();
		address.setInfo( "123 Main St." );
		p.getAddresses().add( address );
		s.save( p );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		Suite suite = new Suite();
		address.getSuites().add( suite );
		p.getAddresses().clear();
		s.saveOrUpdate( p );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		p = ( Person ) s.get( p.getClass(), p.getId() );
		assertEquals( "persistent collection not cleared", 0, p.getAddresses().size() );
		Long count = ( Long ) s.createQuery( "select count(*) from Address" ).list().get( 0 );
		assertEquals( 1, count.longValue() );
		count = ( Long ) s.createQuery( "select count(*) from Suite" ).list().get( 0 );
		assertEquals( 0, count.longValue() );
		s.delete( p );
		t.commit();
		s.close();
	}

	@Test
	@SuppressWarnings( {"unchecked"})
	public void testCascadeAllDeleteOrphanFromClearedPersistentAssnToTransientEntity() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Address address = new Address();
		address.setInfo( "123 Main St." );
		Suite suite = new Suite();
		address.getSuites().add( suite );
		s.save( address );
		t.commit();
		s.close();


		s = openSession();
		t = s.beginTransaction();
		Note note = new Note();
		note.setDescription( "a description" );
		suite.getNotes().add( note );
		address.getSuites().clear();
		s.saveOrUpdate( address );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		Long count = ( Long ) s.createQuery( "select count(*) from Suite" ).list().get( 0 );
		assertEquals( "all-delete-orphan not cascaded properly to cleared persistent collection entities", 0, count.longValue() );
		count = ( Long ) s.createQuery( "select count(*) from Note" ).list().get( 0 );
		assertEquals( 0, count.longValue() );
		s.delete( address );
		t.commit();
		s.close();
	}
}
