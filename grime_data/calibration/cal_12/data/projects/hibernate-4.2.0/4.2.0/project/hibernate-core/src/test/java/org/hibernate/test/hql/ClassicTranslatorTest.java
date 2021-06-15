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
package org.hibernate.test.hql;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.hql.internal.classic.ClassicQueryTranslatorFactory;

/**
 * Some simple test queries using the classic translator explicitly
 * to ensure that code is not broken in changes for the new translator.
 * <p/>
 * Only really checking translation and syntax, not results.
 *
 * @author Steve Ebersole
 */
public class ClassicTranslatorTest extends QueryTranslatorTestCase {
	@Override
	public void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( Environment.QUERY_TRANSLATOR, ClassicQueryTranslatorFactory.class.getName() );
	}

	@Override
	public boolean createSchema() {
		return true;
	}

	@Override
	public boolean rebuildSessionFactoryOnError() {
		return true;
	}

	@Test
	public void testQueries() {
		Session session = openSession();
		session.beginTransaction();

		session.createQuery( "from Animal" ).list();

		session.createQuery( "select a from Animal as a" ).list();
		session.createQuery( "select a.mother from Animal as a" ).list();
		session.createQuery( "select m from Animal as a inner join a.mother as m" ).list();
		session.createQuery( "select a from Animal as a inner join fetch a.mother" ).list();

		session.createQuery( "from Animal as a where a.description = ?" ).setString( 0, "jj" ).list();
		session.createQuery( "from Animal as a where a.description = :desc" ).setString( "desc", "jr" ).list();
		session.createQuery( "from Animal as a where a.description = ? or a.description = :desc" )
				.setString( 0, "jj" )
				.setString( "desc", "jr" )
				.list();

		session.getTransaction().commit();
		session.close();
	}
}
