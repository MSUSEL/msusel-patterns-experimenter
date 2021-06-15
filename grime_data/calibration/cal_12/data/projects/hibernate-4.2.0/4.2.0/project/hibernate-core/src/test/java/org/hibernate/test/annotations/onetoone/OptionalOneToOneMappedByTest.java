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
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Emmanuel Bernard
 * @author Gail Badner
 */
public class OptionalOneToOneMappedByTest extends BaseCoreFunctionalTestCase {

	// @OneToOne(mappedBy="address") with foreign generator
	@Test
	public void testBidirForeignIdGenerator() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		OwnerAddress address = new OwnerAddress();
		address.setOwner( null );
		try {
			s.persist( address );
			s.flush();
			fail( "should have failed with IdentifierGenerationException" );
		}
		catch (IdentifierGenerationException ex) {
			// expected
		}
		finally {
			tx.rollback();
		}
		s.close();
	}

	@Test
	public void testBidirAssignedId() throws Exception {
		Session s = openSession();
		s.getTransaction().begin();
		PartyAffiliate affiliate = new PartyAffiliate();
		affiliate.partyId = "id";

		s.persist( affiliate );
		s.getTransaction().commit();

		s.clear();

		Transaction tx = s.beginTransaction();

		affiliate = ( PartyAffiliate ) s.createCriteria(PartyAffiliate.class)
				.add( Restrictions.idEq( "id" ) )
				.uniqueResult();
		assertNotNull( affiliate );
		assertEquals( "id", affiliate.partyId );
		assertNull( affiliate.party );

		s.clear();

		affiliate = ( PartyAffiliate ) s.get( PartyAffiliate.class, "id" );
		assertNull( affiliate.party );

		s.delete( affiliate );
		tx.commit();
		s.close();
	}

	@Test
	public void testBidirDefaultIdGenerator() throws Exception {
		Session s = openSession();
		s.getTransaction().begin();
		PersonAddress personAddress = new PersonAddress();
		personAddress.setPerson( null );

		s.persist( personAddress );
		s.getTransaction().commit();

		s.clear();

		Transaction tx = s.beginTransaction();

		personAddress = ( PersonAddress ) s.createCriteria(PersonAddress.class)
				.add( Restrictions.idEq( personAddress.getId() ) )
				.uniqueResult();
		assertNotNull( personAddress );
		assertNull( personAddress.getPerson() );

		s.clear();

		personAddress = ( PersonAddress ) s.get( PersonAddress.class, personAddress.getId() );
		assertNull( personAddress.getPerson() );

		s.delete( personAddress );
		tx.commit();
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
}
