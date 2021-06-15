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

import static org.junit.Assert.assertNull;

/**
 * Tests eager materialization and mutation of long strings.
 *
 * @author Steve Ebersole
 */
@RequiresDialect({SybaseASE15Dialect.class,SQLServerDialect.class,SybaseDialect.class,Sybase11Dialect.class})
public class TextTest extends BaseCoreFunctionalTestCase {

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { LongStringHolder.class };
	}

	private static final int LONG_STRING_SIZE = 10000;

	@Test
	public void testBoundedLongStringAccess() {
		String original = buildRecursively(LONG_STRING_SIZE, 'x');
		String changed = buildRecursively(LONG_STRING_SIZE, 'y');

		Session s = openSession();
		s.beginTransaction();
		LongStringHolder entity = new LongStringHolder();
		s.save(entity);
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = (LongStringHolder) s.get(LongStringHolder.class, entity
				.getId());
		assertNull(entity.getLongString());
		assertNull(entity.getName());
		assertNull(entity.getWhatEver());
		entity.setLongString(original);
		entity.setName(original.toCharArray());
		entity.setWhatEver(wrapPrimitive(original.toCharArray()));
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = (LongStringHolder) s.get(LongStringHolder.class, entity
				.getId());
		Assert.assertEquals( LONG_STRING_SIZE, entity.getLongString().length() );
		Assert.assertEquals( original, entity.getLongString() );
		Assert.assertNotNull( entity.getName() );
		Assert.assertEquals( LONG_STRING_SIZE, entity.getName().length );
		assertEquals( original.toCharArray(), entity.getName() );
		Assert.assertNotNull( entity.getWhatEver() );
		Assert.assertEquals( LONG_STRING_SIZE, entity.getWhatEver().length );
		assertEquals( original.toCharArray(), unwrapNonPrimitive( entity.getWhatEver() ) );
		entity.setLongString(changed);
		entity.setName(changed.toCharArray());
		entity.setWhatEver(wrapPrimitive(changed.toCharArray()));
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = (LongStringHolder) s.get(LongStringHolder.class, entity
				.getId());
		Assert.assertEquals( LONG_STRING_SIZE, entity.getLongString().length() );
		Assert.assertEquals( changed, entity.getLongString() );
		Assert.assertNotNull( entity.getName() );
		Assert.assertEquals( LONG_STRING_SIZE, entity.getName().length );
		assertEquals( changed.toCharArray(), entity.getName() );
		Assert.assertNotNull( entity.getWhatEver() );
		Assert.assertEquals( LONG_STRING_SIZE, entity.getWhatEver().length );
		assertEquals( changed.toCharArray(), unwrapNonPrimitive( entity.getWhatEver() ) );
		entity.setLongString(null);
		entity.setName(null);
		entity.setWhatEver(null);
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		entity = (LongStringHolder) s.get(LongStringHolder.class, entity
				.getId());
		assertNull(entity.getLongString());
		assertNull(entity.getName());
		assertNull(entity.getWhatEver());
		s.delete(entity);
		s.getTransaction().commit();
		s.close();
	}

	public static void assertEquals(char[] val1, char[] val2) {
		if (!ArrayHelper.isEquals( val1, val2 )) {
			throw new AssertionFailedError("byte arrays did not match");
		}
	}

	private String buildRecursively(int size, char baseChar) {
		StringBuilder buff = new StringBuilder();
		for (int i = 0; i < size; i++) {
			buff.append(baseChar);
		}
		return buff.toString();
	}

	private Character[] wrapPrimitive(char[] bytes) {
		int length = bytes.length;
		Character[] result = new Character[length];
		for (int index = 0; index < length; index++) {
			result[index] = Character.valueOf(bytes[index]);
		}
		return result;
	}

	private char[] unwrapNonPrimitive(Character[] bytes) {
		int length = bytes.length;
		char[] result = new char[length];
		for (int i = 0; i < length; i++) {
			result[i] = bytes[i].charValue();
		}
		return result;
	}

	@Override
	protected String[] getAnnotatedPackages() {
		return new String[] { "org.hibernate.test.annotations.lob" };
	}

}
