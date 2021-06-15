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

import java.util.Iterator;

import org.junit.Test;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Join;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;
import org.hibernate.test.annotations.Customer;
import org.hibernate.test.annotations.Discount;
import org.hibernate.test.annotations.Passport;
import org.hibernate.test.annotations.Ticket;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Emmanuel Bernard
 */
public class OneToOneTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testEagerFetching() throws Exception {
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		Client c = new Client();
		c.setName( "Emmanuel" );
		Address a = new Address();
		a.setCity( "Courbevoie" );
		c.setAddress( a );
		s.persist( c );
		tx.commit();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		Query q = s.createQuery( "select c from Client c where c.name = :name" );
		q.setString( "name", c.getName() );
		c = ( Client ) q.uniqueResult();
		//c = (Client) s.get(Client.class, c.getId());
		assertNotNull( c );
		tx.commit();
		s.close();
		assertNotNull( c.getAddress() );
		//assertTrue( "Should be eager fetched", Hibernate.isInitialized( c.getAddress() ) );

	}

	@Test
	public void testDefaultOneToOne() throws Exception {
		//test a default one to one and a mappedBy in the other side
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		Customer c = new Customer();
		c.setName( "Hibernatus" );
		Passport p = new Passport();
		p.setNumber( "123456789" );
		s.persist( c ); //we need the id to assigned it to passport
		c.setPassport( p );
		p.setOwner( c );
		p.setId( c.getId() );
		tx.commit();
		s.close();
		s = openSession();
		tx = s.beginTransaction();
		c = ( Customer ) s.get( Customer.class, c.getId() );
		assertNotNull( c );
		p = c.getPassport();
		assertNotNull( p );
		assertEquals( "123456789", p.getNumber() );
		assertNotNull( p.getOwner() );
		assertEquals( "Hibernatus", p.getOwner().getName() );
		tx.commit(); // commit or rollback is the same, we don't care for read queries
		s.close();
	}

	@Test
	public void testOneToOneWithExplicitFk() throws Exception {
		Client c = new Client();
		Address a = new Address();
		a.setCity( "Paris" );
		c.setName( "Emmanuel" );
		c.setAddress( a );

		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		s.persist( c );
		tx.commit();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		c = ( Client ) s.get( Client.class, c.getId() );
		assertNotNull( c );
		assertNotNull( c.getAddress() );
		assertEquals( "Paris", c.getAddress().getCity() );
		tx.commit();
		s.close();
	}

	@Test
	public void testOneToOneWithExplicitSecondaryTableFk() throws Exception {
		Client c = new Client();
		Address a = new Address();
		a.setCity( "Paris" );
		c.setName( "Emmanuel" );
		c.setSecondaryAddress( a );

		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		s.persist( c );
		tx.commit();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		c = ( Client ) s.get( Client.class, c.getId() );
		assertNotNull( c );
		assertNotNull( c.getSecondaryAddress() );
		assertEquals( "Paris", c.getSecondaryAddress().getCity() );
		tx.commit();
		s.close();
	}

	@Test
	public void testUnidirectionalTrueOneToOne() throws Exception {
		Body b = new Body();
		Heart h = new Heart();
		b.setHeart( h );
		b.setId( 1 );
		h.setId( b.getId() ); //same PK
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		s.persist( h );
		s.persist( b );
		tx.commit();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		b = ( Body ) s.get( Body.class, b.getId() );
		assertNotNull( b );
		assertNotNull( b.getHeart() );
		assertEquals( h.getId(), b.getHeart().getId() );
		tx.commit();
		s.close();
	}

	@Test
	public void testCompositePk() throws Exception {
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		ComputerPk cid = new ComputerPk();
		cid.setBrand( "IBM" );
		cid.setModel( "ThinkPad" );
		Computer c = new Computer();
		c.setId( cid );
		c.setCpu( "2 GHz" );
		SerialNumberPk sid = new SerialNumberPk();
		sid.setBrand( cid.getBrand() );
		sid.setModel( cid.getModel() );
		SerialNumber sn = new SerialNumber();
		sn.setId( sid );
		sn.setValue( "REZREZ23424" );
		c.setSerial( sn );
		s.persist( c );
		tx.commit();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		c = ( Computer ) s.get( Computer.class, cid );
		assertNotNull( c );
		assertNotNull( c.getSerial() );
		assertEquals( sn.getValue(), c.getSerial().getValue() );
		tx.commit();
		s.close();
	}

	@Test
	public void testBidirectionalTrueOneToOne() throws Exception {
		Session s = openSession();
		s.getTransaction().begin();
		Party party = new Party();
		PartyAffiliate affiliate = new PartyAffiliate();
		affiliate.partyId = "id";
		party.partyId = "id";
		party.partyAffiliate = affiliate;
		affiliate.party = party;
		
		s.persist( party );
		s.getTransaction().commit();

		s.clear();

		Transaction tx = s.beginTransaction();
		affiliate = ( PartyAffiliate ) s.get( PartyAffiliate.class, "id" );
		assertNotNull( affiliate.party );
		assertEquals( affiliate.partyId, affiliate.party.partyId );

		s.clear();

		party = ( Party ) s.get( Party.class, "id" );
		assertNotNull( party.partyAffiliate );
		assertEquals( party.partyId, party.partyAffiliate.partyId );

		s.delete( party );
		s.delete( party.partyAffiliate );
		tx.commit();
		s.close();
	}

	@Test
	public void testBidirectionalFkOneToOne() throws Exception {
		Session s = openSession();
		s.getTransaction().begin();
		Trousers trousers = new Trousers();
		TrousersZip zip = new TrousersZip();
		trousers.id = 1;
		zip.id = 2;
		trousers.zip = zip;
		zip.trousers = trousers;
		s.persist( trousers );
		s.persist( zip );
		s.getTransaction().commit();

		s.clear();

		Transaction tx = s.beginTransaction();
		trousers = ( Trousers ) s.get( Trousers.class, trousers.id );
		assertNotNull( trousers.zip );
		assertEquals( zip.id, trousers.zip.id );

		s.clear();

		zip = ( TrousersZip ) s.get( TrousersZip.class, zip.id );
		assertNotNull( zip.trousers );
		assertEquals( trousers.id, zip.trousers.id );

		s.delete( zip );
		s.delete( zip.trousers );
		tx.commit();
		s.close();
	}

	@Test
	public void testForeignGenerator() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Owner owner = new Owner();
		OwnerAddress address = new OwnerAddress();
		owner.setAddress( address );
		address.setOwner( owner );
		s.persist( owner );
		s.flush();
		s.clear();
		owner = ( Owner ) s.get( Owner.class, owner.getId() );
		assertNotNull( owner );
		assertNotNull( owner.getAddress() );
		assertEquals( owner.getId(), owner.getAddress().getId() );
		tx.rollback();
		s.close();
	}

	@Test
	@TestForIssue( jiraKey = "HHH-4606" )
	public void testJoinColumnConfiguredInXml() {
		PersistentClass pc = configuration().getClassMapping( Son.class.getName() );
		Iterator iter = pc.getJoinIterator();
		Table table = ( ( Join ) iter.next() ).getTable();
		Iterator columnIter = table.getColumnIterator();
		boolean fooFound = false;
		boolean barFound = false;
		while ( columnIter.hasNext() ) {
			Column column = ( Column ) columnIter.next();
			if ( column.getName().equals( "foo" ) ) {
				fooFound = true;
			}
			if ( column.getName().equals( "bar" ) ) {
				barFound = true;
			}
		}
		assertTrue(
				"The mapping defines join columns which could not be found in the metadata.", fooFound && barFound
		);
	}

	@Test
	@TestForIssue( jiraKey = "HHH-6723" )
	public void testPkOneToOneSelectStatementDoesNotGenerateExtraJoin() {
		// This test uses an interceptor to verify that correct number of joins are generated.
		Session s = openSession(new JoinCounter(1));
		Transaction tx = s.beginTransaction();
		Owner owner = new Owner();
		OwnerAddress address = new OwnerAddress();
		owner.setAddress( address );
		address.setOwner( owner );
		s.persist( owner );
		s.flush();
		s.clear();
		
		owner = ( Owner ) s.get( Owner.class, owner.getId() );
		assertNotNull( owner );
		assertNotNull( owner.getAddress() );
		assertEquals( owner.getId(), owner.getAddress().getId() );
		s.flush();
		s.clear();
		
		address = ( OwnerAddress ) s.get( OwnerAddress.class, address.getId() );
		assertNotNull( address );
		assertNotNull( address.getOwner() );
		assertEquals( address.getId(), address.getOwner().getId() );

		s.flush();
		s.clear();

		owner = ( Owner ) s.createCriteria( Owner.class )
				.add( Restrictions.idEq( owner.getId() ) )
				.uniqueResult();

		assertNotNull( owner );
		assertNotNull( owner.getAddress() );
		assertEquals( owner.getId(), owner.getAddress().getId() );
		s.flush();
		s.clear();

		address = ( OwnerAddress ) s.createCriteria( OwnerAddress.class )
				.add( Restrictions.idEq( address.getId() ) )
				.uniqueResult();

		address = ( OwnerAddress ) s.get( OwnerAddress.class, address.getId() );
		assertNotNull( address );
		assertNotNull( address.getOwner() );
		assertEquals( address.getId(), address.getOwner().getId() );

		s.flush();
		s.clear();

		tx.rollback();
		s.close();
	}
	
	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				PartyAffiliate.class,
				Party.class,
				Trousers.class,
				TrousersZip.class,
				Customer.class,
				Ticket.class,
				Discount.class,
				Passport.class,
				Client.class,
				Address.class,
				Computer.class,
				SerialNumber.class,
				Body.class,
				Heart.class,
				Owner.class,
				OwnerAddress.class
		};
	}

	@Override
	protected String[] getXmlFiles() {
		return new String[] { "org/hibernate/test/annotations/onetoone/orm.xml" };
	}
}


/**
 * Verifies that generated 'select' statement has desired number of joins 
 * @author Sharath Reddy
 *
 */
class JoinCounter extends EmptyInterceptor {
	 
	private static final long serialVersionUID = -3689681272273261051L;
	
	private int expectedNumberOfJoins = 0;
			
	public JoinCounter(int val) {
		super();
		this.expectedNumberOfJoins = val;
	}

	public String onPrepareStatement(String sql) {
		int numberOfJoins = 0;
		if (sql.startsWith("select") & !sql.contains("nextval")) {
			 numberOfJoins = count(sql, "join");
			 assertEquals( expectedNumberOfJoins, numberOfJoins );
		}
						
		return sql;
	 }
	
	 /**
	   * Count the number of instances of substring within a string.
	   *
	   * @param string     String to look for substring in.
	   * @param substring  Sub-string to look for.
	   * @return           Count of substrings in string.
	   */
	  private int count(final String string, final String substring)
	  {
	     int count = 0;
	     int idx = 0;

	     while ((idx = string.indexOf(substring, idx)) != -1)
	     {
	        idx++;
	        count++;
	     }

	     return count;
	  }
	
}
