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
package org.hibernate.test.event.collection.detached;

import java.util.List;

import org.hibernate.Session;

import org.junit.Test;

import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ebersole
 */
@TestForIssue( jiraKey = "HHH-7928" )
public class BadMergeHandlingTest extends BaseCoreFunctionalTestCase {

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { Character.class, Alias.class };
	}

	@Test
	@TestForIssue( jiraKey = "HHH-7928" )
	public void testMergeAndHold() {
		Session s = openSession();
		s.beginTransaction();

		Character paul = new Character( 1, "Paul Atreides" );
		s.persist( paul );
		Character paulo = new Character( 2, "Paulo Atreides" );
		s.persist( paulo );

		Alias alias1 = new Alias( 1, "Paul Muad'Dib" );
		s.persist( alias1 );

		Alias alias2 = new Alias( 2, "Usul" );
		s.persist( alias2 );

		Alias alias3 = new Alias( 3, "The Preacher" );
		s.persist( alias3 );

		s.getTransaction().commit();
		s.close();

		// set up relationships
		s = openSession();
		s.beginTransaction();

		// customer 1
		alias1.getCharacters().add( paul );
		s.merge( alias1 );
		alias2.getCharacters().add( paul );
		s.merge( alias2 );
		alias3.getCharacters().add( paul );
		s.merge( alias3 );

		s.flush();

		// customer 2
		alias1.getCharacters().add( paulo );
		s.merge( alias1 );
		alias2.getCharacters().add( paulo );
		s.merge( alias2 );
		alias3.getCharacters().add( paulo );
		s.merge( alias3 );
		s.flush();

		s.getTransaction().commit();
		s.close();

		// now try to read them back (I guess)
		s = openSession();
		s.beginTransaction();
		List results = s.createQuery( "select c from Character c join c.aliases a where a.alias = :aParam" )
				.setParameter( "aParam", "Usul" )
				.list();
		assertEquals( 2, results.size() );
		s.getTransaction().commit();
		s.close();
	}

}
