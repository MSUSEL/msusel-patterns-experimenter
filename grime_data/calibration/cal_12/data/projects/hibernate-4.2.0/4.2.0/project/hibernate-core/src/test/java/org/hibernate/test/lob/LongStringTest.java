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
package org.hibernate.test.lob;
import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.dialect.SybaseASE15Dialect;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests eager materialization and mutation of long strings.
 *
 * @author Steve Ebersole
 */
@SuppressWarnings( {"UnusedDeclaration"})
public abstract class LongStringTest extends BaseCoreFunctionalTestCase {
	private static final int LONG_STRING_SIZE = 10000;

	@Test
	public void testBoundedLongStringAccess() {
		String original = buildRecursively( LONG_STRING_SIZE, 'x' );
		String changed = buildRecursively( LONG_STRING_SIZE, 'y' );
		String empty = "";

		Session s = openSession();
		s.beginTransaction();
		LongStringHolder entity = new LongStringHolder();
		s.save( entity );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = ( LongStringHolder ) s.get( LongStringHolder.class, entity.getId() );
		assertNull( entity.getLongString() );
		entity.setLongString( original );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = ( LongStringHolder ) s.get( LongStringHolder.class, entity.getId() );
		assertEquals( LONG_STRING_SIZE, entity.getLongString().length() );
		assertEquals( original, entity.getLongString() );
		entity.setLongString( changed );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = ( LongStringHolder ) s.get( LongStringHolder.class, entity.getId() );
		assertEquals( LONG_STRING_SIZE, entity.getLongString().length() );
		assertEquals( changed, entity.getLongString() );
		entity.setLongString( null );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = ( LongStringHolder ) s.get( LongStringHolder.class, entity.getId() );
		assertNull( entity.getLongString() );
		entity.setLongString( empty );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = ( LongStringHolder ) s.get( LongStringHolder.class, entity.getId() );
		if ( entity.getLongString() != null ) {
            if(getDialect() instanceof SybaseASE15Dialect){
                //Sybase uses a single blank to denote an empty string (this is by design). So, when inserting an empty string '', it is interpreted as single blank ' '.
                assertEquals( empty.length(), entity.getLongString().trim().length() );
                assertEquals( empty, entity.getLongString().trim() );
            }else{
			    assertEquals( empty.length(), entity.getLongString().length() );
                assertEquals( empty, entity.getLongString() );
            }
		}
		s.delete( entity );
		s.getTransaction().commit();
		s.close();
	}

	private String buildRecursively(int size, char baseChar) {
		StringBuilder buff = new StringBuilder();
		for( int i = 0; i < size; i++ ) {
			buff.append( baseChar );
		}
		return buff.toString();
	}
}