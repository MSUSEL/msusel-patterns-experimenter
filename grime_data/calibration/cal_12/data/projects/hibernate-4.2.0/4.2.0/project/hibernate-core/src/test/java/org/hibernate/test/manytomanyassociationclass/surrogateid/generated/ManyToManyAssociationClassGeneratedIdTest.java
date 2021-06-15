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
package org.hibernate.test.manytomanyassociationclass.surrogateid.generated;
import java.util.HashSet;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.test.manytomanyassociationclass.AbstractManyToManyAssociationClassTest;
import org.hibernate.test.manytomanyassociationclass.Membership;

import static org.junit.Assert.fail;

/**
 * Tests on many-to-many association using an association class with a surrogate ID that is generated.
 *
 * @author Gail Badner
 */
public class ManyToManyAssociationClassGeneratedIdTest extends AbstractManyToManyAssociationClassTest {
	@Override
	public String[] getMappings() {
		return new String[] { "manytomanyassociationclass/surrogateid/generated/Mappings.hbm.xml" };
	}

	@Override
	public Membership createMembership(String name) {
		return new Membership( name );
	}

	@Test
	public void testRemoveAndAddEqualElement() {
		deleteMembership( getUser(), getGroup(), getMembership() );
		addMembership( getUser(), getGroup(), createMembership( "membership" ) );

		Session s = openSession();
		s.beginTransaction();
		try {
			// The new membership is transient (it has a null surrogate ID), so
			// Hibernate assumes that it should be added to the collection.
			// Inserts are done before deletes, so a ConstraintViolationException
			// will be thrown on the insert because the unique constraint on the
			// user and group IDs in the join table is violated. See HHH-2801.
			s.merge( getUser() );
			s.getTransaction().commit();
			fail( "should have failed because inserts are before deletes");
		}
		catch( ConstraintViolationException ex ) {
			// expected
			s.getTransaction().rollback();
		}
		finally {
			s.close();
		}
	}

	@Test
	public void testRemoveAndAddEqualCollection() {
		deleteMembership( getUser(), getGroup(), getMembership() );
		getUser().setMemberships( new HashSet() );
		getGroup().setMemberships( new HashSet() );
		addMembership( getUser(), getGroup(), createMembership( "membership" ) );

		Session s = openSession();
		s.beginTransaction();
		try {
			// The new membership is transient (it has a null surrogate ID), so
			// Hibernate assumes that it should be added to the collection.
			// Inserts are done before deletes, so a ConstraintViolationException
			// will be thrown on the insert because the unique constraint on the
			// user and group IDs in the join table is violated. See HHH-2801.
			s.merge( getUser() );
			s.getTransaction().commit();
			fail( "should have failed because inserts are before deletes");
		}
		catch( ConstraintViolationException ex ) {
			// expected
			s.getTransaction().rollback();
		}
		finally {
			s.close();
		}
	}

	@Test
	public void testRemoveAndAddEqualElementNonKeyModified() {
		deleteMembership( getUser(), getGroup(), getMembership() );
		Membership membershipNew = createMembership( "membership" );
		addMembership( getUser(), getGroup(), membershipNew );
		membershipNew.setName( "membership1" );

		Session s = openSession();
		s.beginTransaction();
		try {
			// The new membership is transient (it has a null surrogate ID), so
			// Hibernate assumes that it should be added to the collection.
			// Inserts are done before deletes, so a ConstraintViolationException
			// will be thrown on the insert because the unique constraint on the
			// user and group IDs in the join table is violated. See HHH-2801.
			s.merge( getUser() );
			s.getTransaction().commit();
			fail( "should have failed because inserts are before deletes");
		}
		catch( ConstraintViolationException ex ) {
			// expected
			s.getTransaction().rollback();
		}
		finally {
			s.close();
		}
	}
}