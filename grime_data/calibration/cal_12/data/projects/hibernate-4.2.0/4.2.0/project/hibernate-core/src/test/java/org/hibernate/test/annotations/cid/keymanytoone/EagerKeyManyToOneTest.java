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
package org.hibernate.test.annotations.cid.keymanytoone;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

/**
 * @author Steve Ebersole
 */
public class EagerKeyManyToOneTest extends BaseCoreFunctionalTestCase {
	public static final String CARD_ID = "cardId";
	public static final String KEY_ID = "keyId";

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { Card.class, CardField.class, Key.class, PrimaryKey.class };
	}

	@Test
	@TestForIssue( jiraKey = "HHH-4147" )
	public void testLoadEntityWithEagerFetchingToKeyManyToOneReferenceBackToSelf() {
		// based on the core testsuite test of same name in org.hibernate.test.keymanytoone.bidir.component.EagerKeyManyToOneTest
		// meant to test against regression relating to http://opensource.atlassian.com/projects/hibernate/browse/HHH-2277
		// and http://opensource.atlassian.com/projects/hibernate/browse/HHH-4147

		{
			Session s = openSession();
			s.beginTransaction();
			Card card = new Card( CARD_ID );
			Key key = new Key( KEY_ID );
			card.addField( card, key );
			s.persist( key );
			s.persist( card );
			s.getTransaction().commit();
			s.close();
		}

		{
			Session s = openSession();
			s.beginTransaction();
			try {
				Card card = (Card) s.get( Card.class, CARD_ID );
				assertEquals( 1, card.getFields().size() );
				CardField cf = card.getFields().iterator().next();
				assertSame( card, cf.getPrimaryKey().getCard() );
			}
			catch ( StackOverflowError soe ) {
				fail( "eager + key-many-to-one caused stack-overflow in annotations" );
			}
			finally {
				s.getTransaction().commit();
				s.close();
			}
		}

		{
			Session s = openSession();
			s.beginTransaction();
			Card card = (Card) s.get( Card.class, CARD_ID );
			Key key = (Key) s.get( Key.class, KEY_ID );
			s.delete( card );
			s.delete( key );
			s.getTransaction().commit();
			s.close();
		}
	}
}
