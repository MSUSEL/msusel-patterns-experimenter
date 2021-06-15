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
package org.hibernate.test.enums;

import java.util.Properties;

import org.hibernate.internal.util.SerializationHelper;
import org.hibernate.type.EnumType;
import org.hibernate.usertype.DynamicParameterizedType;

import org.junit.Test;

import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Steve Ebersole
 */
public class TestEnumTypeSerialization extends BaseUnitTestCase {
	@Test
	public void testSerializability() {
		{
			// test ordinal mapping
			EnumType enumType = new EnumType();
			Properties properties = new Properties();
			properties.put( EnumType.ENUM, UnspecifiedEnumTypeEntity.E1.class.getName() );
			enumType.setParameterValues( properties );
			assertTrue( enumType.isOrdinal() );
			SerializationHelper.clone( enumType );
		}

		{
			// test named mapping
			EnumType enumType = new EnumType();
			Properties properties = new Properties();
			properties.put( EnumType.ENUM, UnspecifiedEnumTypeEntity.E1.class.getName() );
			properties.put( EnumType.NAMED, "true" );
			enumType.setParameterValues( properties );
			assertFalse( enumType.isOrdinal() );
			SerializationHelper.clone( enumType );
		}
	}
}
