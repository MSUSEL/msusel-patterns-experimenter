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

import junit.framework.AssertionFailedError;
import org.junit.Assert;
import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertNull;

/**
 * Tests eager materialization and mutation of long byte arrays.
 *
 * @author Steve Ebersole
 */
public abstract class LongByteArrayTest extends BaseCoreFunctionalTestCase {
	private static final int ARRAY_SIZE = 10000;

	@Test
	public void testBoundedLongByteArrayAccess() {
		byte[] original = buildRecursively( ARRAY_SIZE, true );
		byte[] changed = buildRecursively( ARRAY_SIZE, false );
		byte[] empty = new byte[] {};

		Session s = openSession();
		s.beginTransaction();
		LongByteArrayHolder entity = new LongByteArrayHolder();
		s.save( entity );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = ( LongByteArrayHolder ) s.get( LongByteArrayHolder.class, entity.getId() );
		assertNull( entity.getLongByteArray() );
		entity.setLongByteArray( original );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = ( LongByteArrayHolder ) s.get( LongByteArrayHolder.class, entity.getId() );
		Assert.assertEquals( ARRAY_SIZE, entity.getLongByteArray().length );
		assertEquals( original, entity.getLongByteArray() );
		entity.setLongByteArray( changed );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = ( LongByteArrayHolder ) s.get( LongByteArrayHolder.class, entity.getId() );
		Assert.assertEquals( ARRAY_SIZE, entity.getLongByteArray().length );
		assertEquals( changed, entity.getLongByteArray() );
		entity.setLongByteArray( null );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = ( LongByteArrayHolder ) s.get( LongByteArrayHolder.class, entity.getId() );
		assertNull( entity.getLongByteArray() );
		entity.setLongByteArray( empty );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = ( LongByteArrayHolder ) s.get( LongByteArrayHolder.class, entity.getId() );
		if ( entity.getLongByteArray() != null ) {
			Assert.assertEquals( empty.length, entity.getLongByteArray().length );
			assertEquals( empty, entity.getLongByteArray() );
		}
		s.delete( entity );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testSaving() {
		byte[] value = buildRecursively( ARRAY_SIZE, true );

		Session s = openSession();
		s.beginTransaction();
		LongByteArrayHolder entity = new LongByteArrayHolder();
		entity.setLongByteArray( value );
		s.persist( entity );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = ( LongByteArrayHolder ) s.get( LongByteArrayHolder.class, entity.getId() );
		Assert.assertEquals( ARRAY_SIZE, entity.getLongByteArray().length );
		assertEquals( value, entity.getLongByteArray() );
		s.delete( entity );
		s.getTransaction().commit();
		s.close();
	}

	private byte[] buildRecursively(int size, boolean on) {
		byte[] data = new byte[size];
		data[0] = mask( on );
		for ( int i = 0; i < size; i++ ) {
			data[i] = mask( on );
			on = !on;
		}
		return data;
	}

	private byte mask(boolean on) {
		return on ? ( byte ) 1 : ( byte ) 0;
	}

	public static void assertEquals(byte[] val1, byte[] val2) {
		if ( !ArrayHelper.isEquals( val1, val2 ) ) {
			throw new AssertionFailedError( "byte arrays did not match" );
		}
	}
}