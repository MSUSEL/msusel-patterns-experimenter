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
package org.hibernate.test.annotations.derivedidentities.bidirectional;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.FailureExpected;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OneToOneWithDerivedIdentityTest extends BaseCoreFunctionalTestCase {
	@Test
	@FailureExpected(jiraKey = "HHH-5695")
	public void testInsertFooAndBarWithDerivedId() {
		Session s = openSession();
		s.beginTransaction();
		Bar bar = new Bar();
		bar.setDetails( "Some details" );
		Foo foo = new Foo();
		foo.setBar( bar );
		bar.setFoo( foo );
		s.persist( foo );
		s.flush();
		assertNotNull( foo.getId() );
		assertEquals( foo.getId(), bar.getFoo().getId() );

		s.clear();
		Bar newBar = ( Bar ) s.createQuery( "SELECT b FROM Bar b WHERE b.foo.id = :id" )
				.setParameter( "id", foo.getId() )
				.uniqueResult();
		assertNotNull( newBar );
		assertEquals( "Some details", newBar.getDetails() );
		s.getTransaction().rollback();
		s.close();
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				Foo.class,
				Bar.class
		};
	}

}
