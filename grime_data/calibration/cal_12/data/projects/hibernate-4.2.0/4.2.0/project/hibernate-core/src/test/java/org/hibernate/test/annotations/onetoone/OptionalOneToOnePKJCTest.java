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
package org.hibernate.test.annotations.onetoone;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.id.IdentifierGenerationException;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Emmanuel Bernard
 * @author Gail Badner
 */
public class OptionalOneToOnePKJCTest extends BaseCoreFunctionalTestCase {

	@Test
	@TestForIssue( jiraKey = "HHH-4982")
	public void testNullBidirForeignIdGenerator() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Person person = new Person();
		person.setPersonAddress( null );
		try {
			s.persist( person );
			s.flush();
			fail( "should have thrown IdentifierGenerationException.");
		}
		catch ( IdentifierGenerationException ex ) {
			// expected
		}
		finally {
			tx.rollback();
			s.close();
		}
	}

	@Test
	@TestForIssue( jiraKey = "HHH-4982")
	public void testNotFoundBidirForeignIdGenerator() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Person person = new Person();
		person.setPersonAddress( null );
		person.setId( 1 );
		try {
			// Hibernate resets the ID to null before executing the foreign generator
			s.persist( person );
			s.flush();
			fail( "should have thrown IdentifierGenerationException.");
		}
		catch ( IdentifierGenerationException ex ) {
			// expected
		}
		finally {
			tx.rollback();
			s.close();
		}
	}

	// @PrimaryKeyJoinColumn @OneToOne(optional=true) non-foreign generator
	@Test
	@TestForIssue( jiraKey = "HHH-4982")
	public void testNotFoundBidirDefaultIdGenerator() {
		Session s = openSession();
		s.getTransaction().begin();
		Owner owner = new Owner();
		owner.setAddress( null );
		s.persist( owner );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.getTransaction().begin();
		owner = ( Owner ) s.get( Owner.class, owner.getId() );
		assertNotNull( owner );
		assertNull( owner.getAddress() );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.getTransaction().begin();
		owner = ( Owner ) s.createCriteria( Owner.class )
				.add( Restrictions.idEq( owner.getId() ) )
				.uniqueResult();
		assertNotNull( owner );
		assertNull( owner.getAddress() );
		s.delete( owner );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testNotFoundBidirAssignedId() throws Exception {
		Session s = openSession();
		s.getTransaction().begin();
		Party party = new Party();
		party.partyId = "id";
		party.partyAffiliate = null;
		s.persist( party );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.getTransaction().begin();
		party = ( Party ) s.get( Party.class, "id" );
		assertNull( party.partyAffiliate );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.getTransaction().begin();
		party = ( Party ) s.createCriteria( Party.class )
				.add( Restrictions.idEq( "id" ) )
				.uniqueResult();
		assertNotNull( party );
		assertEquals( "id", party.partyId );
		assertNull( party.partyAffiliate );
		s.delete( party );
		s.getTransaction().commit();
		s.close();
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				Party.class,
				PartyAffiliate.class,
				Owner.class,
				OwnerAddress.class,
				Person.class,
				PersonAddress.class
		};
	}

	@Override
	protected String[] getXmlFiles() {
		return new String[] { "org/hibernate/test/annotations/onetoone/orm.xml" };
	}
}
