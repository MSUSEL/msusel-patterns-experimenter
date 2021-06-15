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
package org.hibernate.test.annotations.cascade;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;


public class CascadeToEmbeddedManyToOneTest extends BaseCoreFunctionalTestCase {

	@Test
	public void testPersistCascadeToSetOfEmbedded() {
		Session sess = openSession();
		try {
			final Transaction trx  = sess.beginTransaction();
			try {
				final Set<PersonPair> setOfPairs = new HashSet<PersonPair>();
				setOfPairs.add(new PersonPair(new Person("PERSON NAME 1"), new Person("PERSON NAME 2")));
				sess.persist( new CodedPairSetHolder( "CODE", setOfPairs ) );
				sess.flush();
			} finally {
				trx.rollback();
			}
		} finally {
			sess.close();
		}
	}

	@Test
	public void testPersistCascadeToEmbedded() {
		Session sess = openSession();
		try {
			final Transaction trx  = sess.beginTransaction();
			try {
				PersonPair personPair = new PersonPair(new Person("PERSON NAME 1"), new Person("PERSON NAME 2"));
				sess.persist( new CodedPairHolder( "CODE", personPair ) );
				sess.flush();
			} finally {
				trx.rollback();
			}
		} finally {
			sess.close();
		}
	}
	
	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[]{
				CodedPairSetHolder.class,
				CodedPairHolder.class,
				Person.class,
				PersonPair.class
		};
	}
}
