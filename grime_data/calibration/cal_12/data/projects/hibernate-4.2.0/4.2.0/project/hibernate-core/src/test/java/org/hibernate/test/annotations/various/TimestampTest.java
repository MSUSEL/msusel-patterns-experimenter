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
package org.hibernate.test.annotations.various;

import org.junit.Test;

import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.hibernate.type.DbTimestampType;
import org.hibernate.type.TimestampType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for the @Timestamp annotation.
 *
 * @author Hardy Ferentschik
 */
public class TimestampTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testTimestampSourceIsVM() throws Exception {
		assertTimestampSource( VMTimestamped.class, TimestampType.class );
	}

	@Test
	public void testTimestampSourceIsDB() throws Exception {
		assertTimestampSource( DBTimestamped.class, DbTimestampType.class );
	}

	private void assertTimestampSource(Class<?> clazz, Class<?> expectedTypeClass) throws Exception {
		buildConfiguration();
		ClassMetadata meta = sessionFactory().getClassMetadata( clazz );
		assertTrue( "Entity is annotated with @Timestamp and should hence be versioned", meta.isVersioned() );

		PersistentClass persistentClass = configuration().getClassMapping( clazz.getName() );
		assertNotNull( persistentClass );
		Property versionProperty = persistentClass.getVersion();
		assertNotNull( versionProperty );
		assertEquals( "Wrong timestamp type", expectedTypeClass, versionProperty.getType().getClass() );
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				VMTimestamped.class, DBTimestamped.class
		};
	}
}
