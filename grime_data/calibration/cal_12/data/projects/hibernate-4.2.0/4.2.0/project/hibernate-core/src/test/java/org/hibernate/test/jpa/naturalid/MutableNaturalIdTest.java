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
package org.hibernate.test.jpa.naturalid;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.hibernate.Session;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.test.jpa.AbstractJPATest;
import org.hibernate.testing.SkipForDialect;
import org.hibernate.testing.TestForIssue;
import org.junit.Test;

/**
 * @author Steve Ebersole
 */
@SkipForDialect(value = Oracle8iDialect.class,
		comment = "Oracle does not support identity key generation")
public class MutableNaturalIdTest extends AbstractJPATest {
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { Group.class, ClassWithIdentityColumn.class };
	}

	@Test
	public void testSimpleNaturalIdLoadAccessCacheWithUpdate() {
		Session s = openSession();
		s.beginTransaction();
		Group g = new Group( 1, "admin" );
		s.persist( g );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		g = (Group) s.bySimpleNaturalId( Group.class ).load( "admin" );
		assertNotNull( g );
		Group g2 = (Group) s.bySimpleNaturalId( Group.class ).getReference( "admin" );
		assertTrue( g == g2 );
		g.setName( "admins" );
		s.flush();
		g2 = (Group) s.bySimpleNaturalId( Group.class ).getReference( "admins" );
		assertTrue( g == g2 );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		s.createQuery( "delete Group" ).executeUpdate();
		s.getTransaction().commit();
		s.close();
	}
	
	@Test 
	@TestForIssue( jiraKey = "HHH-7304")
	public void testInLineSynchWithIdentityColumn() {
		Session s = openSession();
		s.beginTransaction();
		ClassWithIdentityColumn e = new ClassWithIdentityColumn();
		e.setName("Dampf");
		s.save(e);
		e.setName("Klein");
		assertNotNull(session.bySimpleNaturalId(ClassWithIdentityColumn.class).load("Klein"));

		session.getTransaction().rollback();
		session.close();
	}
}
