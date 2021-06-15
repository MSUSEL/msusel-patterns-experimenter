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
package org.hibernate.test.bidi;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Gavin King
 */
@RequiresDialectFeature(
		value = DialectChecks.SupportsExistsInSelectCheck.class,
		comment = "dialect does not support exist predicates in the select clause"
)
public class AuctionTest2 extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "bidi/Auction2.hbm.xml" };
	}

	@Test
	public void testLazy() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Auction a = new Auction();
		a.setDescription( "an auction for something" );
		a.setEnd( new Date() );
		Bid b = new Bid();
		b.setAmount( new BigDecimal( 123.34 ).setScale( 19, BigDecimal.ROUND_DOWN ) );
		b.setSuccessful( true );
		b.setDatetime( new Date() );
		b.setItem( a );
		a.getBids().add( b );
		a.setSuccessfulBid( b );
		s.persist( b );
		t.commit();
		s.close();

		Long aid = a.getId();
		Long bid = b.getId();

		s = openSession();
		t = s.beginTransaction();
		b = ( Bid ) s.load( Bid.class, bid );
		assertFalse( Hibernate.isInitialized( b ) );
		a = ( Auction ) s.get( Auction.class, aid );
		assertFalse( Hibernate.isInitialized( a.getBids() ) );
		assertFalse( Hibernate.isInitialized( a.getSuccessfulBid() ) );
		assertSame( a.getBids().iterator().next(), b );
		assertSame( b, a.getSuccessfulBid() );
		assertTrue( Hibernate.isInitialized( b ) );
		assertTrue( b.isSuccessful() );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		b = ( Bid ) s.load( Bid.class, bid );
		assertFalse( Hibernate.isInitialized( b ) );
		a = ( Auction ) s.createQuery( "from Auction a left join fetch a.bids" ).uniqueResult();
		assertTrue( Hibernate.isInitialized( b ) );
		assertTrue( Hibernate.isInitialized( a.getBids() ) );
		assertSame( b, a.getSuccessfulBid() );
		assertSame( a.getBids().iterator().next(), b );
		assertTrue( b.isSuccessful() );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		b = ( Bid ) s.load( Bid.class, bid );
		a = ( Auction ) s.load( Auction.class, aid );
		assertFalse( Hibernate.isInitialized( b ) );
		assertFalse( Hibernate.isInitialized( a ) );
		s.createQuery( "from Auction a left join fetch a.successfulBid" ).list();
		assertTrue( Hibernate.isInitialized( b ) );
		assertTrue( Hibernate.isInitialized( a ) );
		assertSame( b, a.getSuccessfulBid() );
		assertFalse( Hibernate.isInitialized( a.getBids() ) );
		assertSame( a.getBids().iterator().next(), b );
		assertTrue( b.isSuccessful() );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		b = ( Bid ) s.load( Bid.class, bid );
		a = ( Auction ) s.load( Auction.class, aid );
		assertFalse( Hibernate.isInitialized( b ) );
		assertFalse( Hibernate.isInitialized( a ) );
		assertSame( s.get( Bid.class, bid ), b );
		assertTrue( Hibernate.isInitialized( b ) );
		assertSame( s.get( Auction.class, aid ), a );
		assertTrue( Hibernate.isInitialized( a ) );
		assertSame( b, a.getSuccessfulBid() );
		assertFalse( Hibernate.isInitialized( a.getBids() ) );
		assertSame( a.getBids().iterator().next(), b );
		assertTrue( b.isSuccessful() );
		t.commit();
		s.close();
	}

}
