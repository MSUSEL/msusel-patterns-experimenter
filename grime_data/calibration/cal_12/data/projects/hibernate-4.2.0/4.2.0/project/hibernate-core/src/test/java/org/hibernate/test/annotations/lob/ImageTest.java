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
package org.hibernate.test.annotations.lob;

import junit.framework.AssertionFailedError;
import org.junit.Assert;
import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.dialect.Sybase11Dialect;
import org.hibernate.dialect.SybaseASE15Dialect;
import org.hibernate.dialect.SybaseDialect;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * Tests eager materialization and mutation of data mapped by
 * {@link org.hibernate.type.ImageType}.
 *
 * @author Gail Badner
 */
@RequiresDialect( { SybaseASE15Dialect.class, SQLServerDialect.class, SybaseDialect.class, Sybase11Dialect.class })
public class ImageTest extends BaseCoreFunctionalTestCase {
	private static final int ARRAY_SIZE = 10000;

	@Test
	public void testBoundedLongByteArrayAccess() {
		byte[] original = buildRecursively(ARRAY_SIZE, true);
		byte[] changed = buildRecursively(ARRAY_SIZE, false);

		Session s = openSession();
		s.beginTransaction();
		ImageHolder entity = new ImageHolder();
		s.save(entity);
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = (ImageHolder) s.get(ImageHolder.class, entity.getId());
		Assert.assertNull( entity.getLongByteArray() );
		Assert.assertNull( entity.getDog() );
		Assert.assertNull( entity.getPicByteArray() );
		entity.setLongByteArray(original);
		Dog dog = new Dog();
		dog.setName("rabbit");
		entity.setDog(dog);
		entity.setPicByteArray(wrapPrimitive(original));
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = (ImageHolder) s.get(ImageHolder.class, entity.getId());
		Assert.assertEquals( ARRAY_SIZE, entity.getLongByteArray().length );
		assertEquals(original, entity.getLongByteArray());
		Assert.assertEquals( ARRAY_SIZE, entity.getPicByteArray().length );
		assertEquals(original, unwrapNonPrimitive(entity.getPicByteArray()));
		Assert.assertNotNull( entity.getDog() );
		Assert.assertEquals( dog.getName(), entity.getDog().getName() );
		entity.setLongByteArray(changed);
		entity.setPicByteArray(wrapPrimitive(changed));
		dog.setName("papa");
		entity.setDog(dog);
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = (ImageHolder) s.get(ImageHolder.class, entity.getId());
		Assert.assertEquals( ARRAY_SIZE, entity.getLongByteArray().length );
		assertEquals(changed, entity.getLongByteArray());
		Assert.assertEquals( ARRAY_SIZE, entity.getPicByteArray().length );
		assertEquals(changed, unwrapNonPrimitive(entity.getPicByteArray()));
		Assert.assertNotNull( entity.getDog() );
		Assert.assertEquals( dog.getName(), entity.getDog().getName() );
		entity.setLongByteArray(null);
		entity.setPicByteArray(null);
		entity.setDog(null);
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = (ImageHolder) s.get(ImageHolder.class, entity.getId());
		Assert.assertNull( entity.getLongByteArray() );
		Assert.assertNull( entity.getDog() );
		Assert.assertNull( entity.getPicByteArray() );
		s.delete(entity);
		s.getTransaction().commit();
		s.close();
	}

	private Byte[] wrapPrimitive(byte[] bytes) {
		int length = bytes.length;
		Byte[] result = new Byte[length];
		for (int index = 0; index < length; index++) {
			result[index] = Byte.valueOf( bytes[index] );
		}
		return result;
	}

	private byte[] unwrapNonPrimitive(Byte[] bytes) {
		int length = bytes.length;
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = bytes[i].byteValue();
		}
		return result;
	}

	private byte[] buildRecursively(int size, boolean on) {
		byte[] data = new byte[size];
		data[0] = mask(on);
		for (int i = 0; i < size; i++) {
			data[i] = mask(on);
			on = !on;
		}
		return data;
	}

	private byte mask(boolean on) {
		return on ? (byte) 1 : (byte) 0;
	}

	public static void assertEquals(byte[] val1, byte[] val2) {
		if (!ArrayHelper.isEquals( val1, val2 )) {
			throw new AssertionFailedError("byte arrays did not match");
		}
	}

	@Override
	protected String[] getAnnotatedPackages() {
		return new String[] { "org.hibernate.test.annotations.lob" };
	}

	@Override
    public Class<?>[] getAnnotatedClasses() {
		return new Class[] { ImageHolder.class };
	}

}