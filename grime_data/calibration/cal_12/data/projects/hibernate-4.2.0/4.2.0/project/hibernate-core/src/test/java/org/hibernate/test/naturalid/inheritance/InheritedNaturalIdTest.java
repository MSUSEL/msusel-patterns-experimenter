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
package org.hibernate.test.naturalid.inheritance;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * @author Steve Ebersole
 */
public class InheritedNaturalIdTest extends BaseCoreFunctionalTestCase {
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { Principal.class, User.class };
	}

	@Test
	public void testIt() {
		Session s = openSession();
		s.beginTransaction();
		s.save(  new User( "steve" ) );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.bySimpleNaturalId( Principal.class ).load( "steve" );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.bySimpleNaturalId( User.class ).load( "steve" );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.delete( s.bySimpleNaturalId( User.class ).load( "steve" ) );
		s.getTransaction().commit();
		s.close();
	}


	@Test
	public void testSubclassModifieablNaturalId() {
		Session s = openSession();
		s.beginTransaction();
		s.save( new User( "steve" ) );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		Principal p = (Principal) s.bySimpleNaturalId( Principal.class ).load( "steve" );
		assertNotNull( p );
		User u = (User) s.bySimpleNaturalId( User.class ).load( "steve" );
		assertNotNull( u );
		assertSame( p, u );

		// change the natural id
		u.setUid( "sebersole" );
		s.flush();

		// make sure we can no longer access the info based on the old natural id value
		assertNull( s.bySimpleNaturalId( Principal.class ).load( "steve" ) );
		assertNull( s.bySimpleNaturalId( User.class ).load( "steve" ) );

		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.delete( u );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testSubclassDeleteNaturalId() {
		Session s = openSession();
		s.beginTransaction();
		s.save( new User( "steve" ) );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		Principal p = (Principal) s.bySimpleNaturalId( Principal.class ).load( "steve" );
		assertNotNull( p );

		s.delete( p );
		s.flush();

//		assertNull( s.bySimpleNaturalId( Principal.class ).load( "steve" ) );
		assertNull( s.bySimpleNaturalId( User.class ).load( "steve" ) );

		s.getTransaction().commit();
		s.close();
	}
}
