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
package org.hibernate.test.idgen.biginteger.sequence;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author Steve Ebersole
 */
@RequiresDialectFeature( value = DialectChecks.SupportsSequences.class )
public class BigIntegerSequenceGeneratorTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "idgen/biginteger/sequence/Mapping.hbm.xml" };
	}

	@Test
	public void testBasics() {
		Session s = openSession();
		s.beginTransaction();
		Entity entity = new Entity( "BigInteger + sequence #1" );
		s.save( entity );
		Entity entity2 = new Entity( "BigInteger + sequence #2" );
		s.save( entity2 );
		s.getTransaction().commit();
		s.close();

// hsqldb defines different behavior for the initial select from a sequence
// then say oracle
//		assertEquals( BigInteger.valueOf( 1 ), entity.getId() );
//		assertEquals( BigInteger.valueOf( 2 ), entity2.getId() );

		s = openSession();
		s.beginTransaction();
		s.delete( entity );
		s.delete( entity2 );
		s.getTransaction().commit();
		s.close();

	}
}