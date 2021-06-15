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
package org.hibernate.test.bytecode.enhancement;

import org.hibernate.Session;

import org.junit.Test;

import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * @author Steve Ebersole
 */
public class MostBasicEnhancementTest extends BaseCoreFunctionalTestCase {
	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { MyEntity.class };
	}

	@Test
	public void testIt() {
		Session s = openSession();
		s.beginTransaction();
		s.save( new MyEntity( 1L ) );
		s.save( new MyEntity( 2L ) );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		MyEntity myEntity = (MyEntity) s.get( MyEntity.class, 1L );
		MyEntity myEntity2 = (MyEntity) s.get( MyEntity.class, 2L );

		assertNotNull( myEntity.$$_hibernate_getEntityInstance() );
		assertSame( myEntity, myEntity.$$_hibernate_getEntityInstance() );
		assertNotNull( myEntity.$$_hibernate_getEntityEntry() );
		assertNull( myEntity.$$_hibernate_getPreviousManagedEntity() );
		assertNotNull( myEntity.$$_hibernate_getNextManagedEntity() );

		assertNotNull( myEntity2.$$_hibernate_getEntityInstance() );
		assertSame( myEntity2, myEntity2.$$_hibernate_getEntityInstance() );
		assertNotNull( myEntity2.$$_hibernate_getEntityEntry() );
		assertNotNull( myEntity2.$$_hibernate_getPreviousManagedEntity() );
		assertNull( myEntity2.$$_hibernate_getNextManagedEntity() );

		s.createQuery( "delete MyEntity" ).executeUpdate();
		s.getTransaction().commit();
		s.close();

		assertNull( myEntity.$$_hibernate_getEntityEntry() );
	}


}
