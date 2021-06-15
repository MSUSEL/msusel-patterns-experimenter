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
package org.hibernate.test.manytomanyassociationclass;
import java.util.HashSet;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Abstract class for tests on many-to-many association using an association class.
 *
 * @author Gail Badner
 */
public abstract class AbstractManyToManyAssociationClassTest extends BaseCoreFunctionalTestCase {
	private User user;
	private Group group;
	private Membership membership;

	public abstract Membership createMembership(String name);

	@Override
	protected void prepareTest() {
		Session s = openSession();
		s.beginTransaction();
		user = new User( "user" );
		group = new Group( "group" );
		s.save( user );
		s.save( group );
		membership = createMembership( "membership");
		addMembership( user, group, membership );
		s.getTransaction().commit();
		s.close();
	}

	@Override
	protected void cleanupTest() {
		if ( sessionFactory() != null ) {
			Session s = openSession();
			s.beginTransaction();
			s.createQuery( "delete from " + membership.getClass().getName() );
			s.createQuery( "delete from User" );
			s.createQuery( "delete from Group" );
			s.getTransaction().commit();
			s.close();
		}
	}

	public User getUser() {
		return user;
	}

	public Group getGroup() {
		return group;
	}

	public Membership getMembership() {
		return membership;
	}

	@Test
	public void testRemoveAndAddSameElement() {
		deleteMembership( user, group, membership );
		addMembership( user, group, membership );

		Session s = openSession();
		s.beginTransaction();
		s.merge( user );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		user = ( User ) s.get( User.class, user.getId() );
		group = ( Group ) s.get( Group.class, group.getId() );
		membership = ( Membership ) s.get( membership.getClass(), membership.getId() );
		assertEquals( "user", user.getName() );
		assertEquals( "group", group.getName() );
		assertEquals( "membership", membership.getName() );
		assertEquals( 1, user.getMemberships().size() );
		assertEquals( 1, group.getMemberships().size() );
		assertSame( membership, user.getMemberships().iterator().next() );
		assertSame( membership, group.getMemberships().iterator().next() );
		assertSame( user, membership.getUser() );
		assertSame( group, membership.getGroup() );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testRemoveAndAddEqualElement() {
		deleteMembership( user, group, membership );
		membership = createMembership( "membership" );
		addMembership( user, group, membership );

		Session s = openSession();
		s.beginTransaction();
		s.merge( user );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		user = ( User ) s.get( User.class, user.getId() );
		group = ( Group ) s.get( Group.class, group.getId() );
		membership = ( Membership ) s.get( membership.getClass(), membership.getId() );
		assertEquals( "user", user.getName() );
		assertEquals( "group", group.getName() );
		assertEquals( "membership", membership.getName() );
		assertEquals( 1, user.getMemberships().size() );
		assertEquals( 1, group.getMemberships().size() );
		assertSame( membership, user.getMemberships().iterator().next() );
		assertSame( membership, group.getMemberships().iterator().next() );
		assertSame( user, membership.getUser() );
		assertSame( group, membership.getGroup() );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testRemoveAndAddEqualCollection() {
		deleteMembership( user, group, membership );
		membership = createMembership( "membership" );
		user.setMemberships( new HashSet() );
		group.setMemberships( new HashSet() );
		addMembership( user, group, membership );

		Session s = openSession();
		s.beginTransaction();
		s.merge( user );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		user = ( User ) s.get( User.class, user.getId() );
		group = ( Group ) s.get( Group.class, group.getId() );
		membership = ( Membership ) s.get( membership.getClass(), membership.getId() );
		assertEquals( "user", user.getName() );
		assertEquals( "group", group.getName() );
		assertEquals( "membership", membership.getName() );
		assertEquals( 1, user.getMemberships().size() );
		assertEquals( 1, group.getMemberships().size() );
		assertSame( membership, user.getMemberships().iterator().next() );
		assertSame( membership, group.getMemberships().iterator().next() );
		assertSame( user, membership.getUser() );
		assertSame( group, membership.getGroup() );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testRemoveAndAddSameElementNonKeyModified() {
		deleteMembership( user, group, membership );
		addMembership( user, group, membership );
		membership.setName( "membership1" );

		Session s = openSession();
		s.beginTransaction();
		s.merge( user );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		user = ( User ) s.get( User.class, user.getId() );
		group = ( Group ) s.get( Group.class, group.getId() );
		membership = ( Membership ) s.get( membership.getClass(), membership.getId() );
		assertEquals( "user", user.getName() );
		assertEquals( "group", group.getName() );
		assertEquals( "membership1", membership.getName() );
		assertEquals( 1, user.getMemberships().size() );
		assertEquals( 1, group.getMemberships().size() );
		assertSame( membership, user.getMemberships().iterator().next() );
		assertSame( membership, group.getMemberships().iterator().next() );
		assertSame( user, membership.getUser() );
		assertSame( group, membership.getGroup() );
		s.getTransaction().commit();
		s.close();

	}

	@Test
	public void testRemoveAndAddEqualElementNonKeyModified() {
		deleteMembership( user, group, membership );
		membership = createMembership( "membership" );
		addMembership( user, group, membership );
		membership.setName( "membership1" );

		Session s = openSession();
		s.beginTransaction();
		s.merge( user );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		user = ( User ) s.get( User.class, user.getId() );
		group = ( Group ) s.get( Group.class, group.getId() );
		membership = ( Membership ) s.get( membership.getClass(), membership.getId() );
		assertEquals( "user", user.getName() );
		assertEquals( "group", group.getName() );
		assertEquals( "membership1", membership.getName() );
		assertEquals( 1, user.getMemberships().size() );
		assertEquals( 1, group.getMemberships().size() );
		assertSame( membership, user.getMemberships().iterator().next() );
		assertSame( membership, group.getMemberships().iterator().next() );
		assertSame( user, membership.getUser() );
		assertSame( group, membership.getGroup() );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testDeleteDetached() {
		Session s = openSession();
		s.beginTransaction();
		s.delete( user );
		s.delete( group );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		assertNull( s.get( User.class, user.getId() ) );
		assertNull( s.get( Group.class , group.getId() ) );
		assertNull( s.get( membership.getClass(), membership.getId() ) );
		s.getTransaction().commit();
		s.close();
	}

	public void deleteMembership(User u, Group g, Membership ug) {
		if ( u == null || g == null ) {
			throw new IllegalArgumentException();
		}
		u.getMemberships().remove( ug );
		g.getMemberships().remove( ug );
		ug.setUser( null );
		ug.setGroup( null );
	}

	public void addMembership(User u, Group g, Membership ug) {
		if ( u == null || g == null ) {
			throw new IllegalArgumentException();
		}
		ug.setUser( u );
		ug.setGroup( g );
		u.getMemberships().add( ug );
		g.getMemberships().add( ug );
	}
}
