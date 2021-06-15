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
import org.hibernate.testing.SkipForDialect;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests of {@link org.hibernate.type.SerializableType}
 * 
 * @author Steve Ebersole
 */
public class SerializableTypeTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "lob/SerializableMappings.hbm.xml" };
	}

	public String getCacheConcurrencyStrategy() {
		return null;
	}

	@Test
    @SkipForDialect( value = SybaseASE15Dialect.class, jiraKey = "HHH-6425")
	public void testNewSerializableType() {
		final String initialPayloadText = "Initial payload";
		final String changedPayloadText = "Changed payload";
		final String empty = "";

		Session s = openSession();
		s.beginTransaction();
		SerializableHolder holder = new SerializableHolder();
		s.save( holder );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		holder = ( SerializableHolder ) s.get( SerializableHolder.class, holder.getId() );
		assertNull( holder.getSerialData() );
		holder.setSerialData( new SerializableData( initialPayloadText ) );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		holder = ( SerializableHolder ) s.get( SerializableHolder.class, holder.getId() );
		SerializableData serialData = ( SerializableData ) holder.getSerialData();
		assertEquals( initialPayloadText, serialData.getPayload() );
		holder.setSerialData( new SerializableData( changedPayloadText ) );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		holder = ( SerializableHolder ) s.get( SerializableHolder.class, holder.getId() );
		serialData = ( SerializableData ) holder.getSerialData();
		assertEquals( changedPayloadText, serialData.getPayload() );
		holder.setSerialData( null );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		holder = ( SerializableHolder ) s.get( SerializableHolder.class, holder.getId() );
		assertNull( holder.getSerialData() );
		holder.setSerialData( new SerializableData( empty ) );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		holder = ( SerializableHolder ) s.get( SerializableHolder.class, holder.getId() );
		serialData = ( SerializableData ) holder.getSerialData();
		assertEquals( empty, serialData.getPayload() );
		s.delete( holder );
		s.getTransaction().commit();
		s.close();
	}

}
