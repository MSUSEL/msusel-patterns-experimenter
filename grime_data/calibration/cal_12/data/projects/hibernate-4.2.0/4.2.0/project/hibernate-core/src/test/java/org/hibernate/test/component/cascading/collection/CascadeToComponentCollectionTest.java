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
package org.hibernate.test.component.cascading.collection;

import java.util.Locale;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ebersole
 */
public class CascadeToComponentCollectionTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "component/cascading/collection/Mappings.hbm.xml" };
	}

	@Test
	public void testMerging() {
		// step1, we create a definition with one value
		Session session = openSession();
		session.beginTransaction();
		Definition definition = new Definition();
		Value value1 = new Value( definition );
		value1.getLocalizedStrings().addString( new Locale( "en_US" ), "hello" );
		session.persist( definition );
		session.getTransaction().commit();
		session.close();

		// step2, we verify that the definition has one value; then we detach it
		session = openSession();
		session.beginTransaction();
		definition = ( Definition ) session.get( Definition.class, definition.getId() );
		assertEquals( 1, definition.getValues().size() );
		session.getTransaction().commit();
		session.close();

		// step3, we add a new value during detachment
		Value value2 = new Value( definition );
		value2.getLocalizedStrings().addString( new Locale( "es" ), "hola" );

		// step4 we merge the definition
		session = openSession();
		session.beginTransaction();
		session.merge( definition );
		session.getTransaction().commit();
		session.close();

		// step5, final test
		session = openSession();
		session.beginTransaction();
		definition = ( Definition ) session.get( Definition.class, definition.getId() );
		assertEquals( 2, definition.getValues().size() );
		for ( Object o : definition.getValues() ) {
			assertEquals( 1, ((Value) o).getLocalizedStrings().getStringsCopy().size() );
		}
		session.getTransaction().commit();
		session.close();
	}

	@SuppressWarnings( {"UnusedDeclaration"})
	@Test
	public void testMergingOriginallyNullComponent() {
		// step1, we create a definition with one value, but with a null component
		Session session = openSession();
		session.beginTransaction();
		Definition definition = new Definition();
		Value value1 = new Value( definition );
		session.persist( definition );
		session.getTransaction().commit();
		session.close();

		// step2, we verify that the definition has one value; then we detach it
		session = openSession();
		session.beginTransaction();
		definition = ( Definition ) session.get( Definition.class, definition.getId() );
		assertEquals( 1, definition.getValues().size() );
		session.getTransaction().commit();
		session.close();

		// step3, we add a new value during detachment
		( ( Value ) definition.getValues().iterator().next() ).getLocalizedStrings().addString( new Locale( "en_US" ), "hello" );
		Value value2 = new Value( definition );
		value2.getLocalizedStrings().addString( new Locale( "es" ), "hola" );

		// step4 we merge the definition
		session = openSession();
		session.beginTransaction();
		session.merge( definition );
		session.getTransaction().commit();
		session.close();

		// step5, final test
		session = openSession();
		session.beginTransaction();
		definition = ( Definition ) session.get( Definition.class, definition.getId() );
		assertEquals( 2, definition.getValues().size() );
		for ( Object o : definition.getValues() ) {
			assertEquals( 1, ((Value) o).getLocalizedStrings().getStringsCopy().size() );
		}
		session.getTransaction().commit();
		session.close();
	}
}
