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
package org.hibernate.test.annotations.entity;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Emmanuel Bernard
 */
public class Java5FeaturesTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testInterface() throws Exception {
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		Race r = new Race();
		r.setId( new Integer( 1 ) );
		r.setLength( new Long( 3 ) );
		s.persist( r );
		tx.commit();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		r = (Race) s.get( Race.class, r.getId() );
		assertEquals( new Long( 3 ), r.getLength() );
		tx.commit();
		s.close();

	}

	@Test
	public void testEnums() throws Exception {
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		CommunityBid communityBid = new CommunityBid();
		communityBid.setId( new Integer( 2 ) );
		communityBid.setCommunityNote( Starred.OK );
		Bid bid = new Bid();
		bid.setId( new Integer( 1 ) );
		bid.setDescription( "My best one" );
		bid.setNote( Starred.OK );
		bid.setEditorsNote( Starred.GOOD );
		s.persist( bid );
		s.persist( communityBid );
		tx.commit();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		//bid = (Bid) s.get( Bid.class, bid.getId() );
		bid = (Bid)s.createQuery( "select b from Bid b where b.note = " +
				Starred.class.getName() + ".OK and b.editorsNote = " +
				Starred.class.getName() + ".GOOD and b.id = :id")
				.setParameter( "id", bid.getId() ).uniqueResult();
		//testing constant value
		assertEquals( Starred.OK, bid.getNote() );
		assertEquals( Starred.GOOD, bid.getEditorsNote() );
		bid = (Bid)s.createQuery( "select b from Bid b where b.note = :note" +
				 " and b.editorsNote = :editorNote " +
				" and b.id = :id")
				.setParameter( "id", bid.getId() )
				.setParameter( "note", Starred.OK )
				.setParameter( "editorNote", Starred.GOOD )
				.uniqueResult();
		//testing constant value
		assertEquals( Starred.OK, bid.getNote() );
		assertEquals( Starred.GOOD, bid.getEditorsNote() );
		bid.setNote( null );
		tx.commit();
		s.clear();
		tx = s.beginTransaction();
		bid = (Bid) s.get( Bid.class, bid.getId() );
		communityBid = (CommunityBid) s.get( CommunityBid.class, communityBid.getId() );
		assertNull( bid.getNote() );
		assertEquals( Starred.OK, communityBid.getCommunityNote() );
		s.delete( bid );
		s.clear();
		communityBid = (CommunityBid) s.createSQLQuery( "select {b.*} from Bid b where b.id = ?" )
				.addEntity( "b", CommunityBid.class )
				.setInteger( 0, communityBid.getId() ).uniqueResult();
		assertEquals( Starred.OK, communityBid.getCommunityNote() );
		s.delete( communityBid );
		tx.commit();
		s.close();
	}

	public void testAutoboxing() throws Exception {
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		Bid bid = new Bid();
		bid.setId( new Integer( 2 ) );
		bid.setDescription( "My best one" );
		bid.setNote( Starred.OK );
		bid.setEditorsNote( Starred.GOOD );
		bid.setApproved( null );
		s.persist( bid );
		tx.commit();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		bid = (Bid) s.get( Bid.class, bid.getId() );
		assertEquals( null, bid.getApproved() );
		s.delete( bid );
		tx.commit();
		s.close();
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[]{
				Race.class,
				Bid.class,
				CommunityBid.class
		};
	}
}
