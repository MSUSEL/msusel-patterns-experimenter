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
package org.hibernate.test.annotations.identifiercollection;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Emmanuel Bernard
 */
public class IdentifierCollectionTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testIdBag() throws Exception {
		Passport passport = new Passport();
		passport.setName( "Emmanuel Bernard" );
		Stamp canada = new Stamp();
		canada.setCountry( "Canada" );
		passport.getStamps().add( canada );
		passport.getVisaStamp().add( canada );
		Stamp norway = new Stamp();
		norway.setCountry( "Norway" );
		passport.getStamps().add( norway );
		passport.getStamps().add(canada);
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		s.persist( passport );
		s.flush();
		//s.clear();
		passport = (Passport) s.get( Passport.class, passport.getId() );
		int canadaCount = 0;
		for ( Stamp stamp : passport.getStamps() ) {
			if ( "Canada".equals( stamp.getCountry() ) ) canadaCount++;
		}
		assertEquals( 2, canadaCount );
		assertEquals( 1, passport.getVisaStamp().size() );
		tx.rollback();
		s.close();
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				Passport.class,
				Stamp.class
		};
	}
}
