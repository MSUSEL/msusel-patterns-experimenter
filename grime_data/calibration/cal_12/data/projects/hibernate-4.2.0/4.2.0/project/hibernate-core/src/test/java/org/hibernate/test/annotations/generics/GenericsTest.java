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
package org.hibernate.test.annotations.generics;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author Emmanuel Bernard
 */
public class GenericsTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testManyToOneGenerics() throws Exception {
		Paper white = new Paper();
		white.setName( "WhiteA4" );
		PaperType type = new PaperType();
		type.setName( "A4" );
		SomeGuy me = new SomeGuy();
		white.setType( type );
		white.setOwner( me );
		Price price = new Price();
		price.setAmount( new Double( 1 ) );
		price.setCurrency( "Euro" );
		white.setValue( price );
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		s.persist( type );
		s.persist( price );
		s.persist( me );
		s.persist( white );
		tx.commit();
		//s.close();
		s = openSession();
		tx = s.beginTransaction();
		white = (Paper) s.get( Paper.class, white.getId() );
		s.delete( white.getType() );
		s.delete( white.getOwner() );
		s.delete( white.getValue() );
		s.delete( white );
		tx.commit();
		//s.close();
	}

	@Override
	protected void configure(Configuration cfg) {
		cfg.setProperty( Environment.AUTO_CLOSE_SESSION, "true" );
		super.configure( cfg );
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[]{
				Paper.class,
				PaperType.class,
				SomeGuy.class,
				Price.class,
				WildEntity.class,

				//test at deployment only test unbound property when default field access is used
				Dummy.class
		};
	}
}
