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

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.Collections;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.engine.query.spi.HQLQueryPlan;
import org.hibernate.engine.spi.SessionFactoryImplementor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Steve Ebersole
 */
@TestForIssue( jiraKey = "HHH-7757" )
public class TupleSupportTest extends BaseUnitTestCase {
	@Entity( name = "TheEntity" )
	public static class TheEntity {
		@Id
		private Long id;
		@Embedded
		private TheComposite compositeValue;
	}

	@Embeddable
	public static class TheComposite {
		private String thing1;
		private String thing2;

		public TheComposite() {
		}

		public TheComposite(String thing1, String thing2) {
			this.thing1 = thing1;
			this.thing2 = thing2;
		}
	}

	private SessionFactory sessionFactory;

	@Before
	public void buildSessionFactory() {
		Configuration cfg = new Configuration()
				.addAnnotatedClass( TheEntity.class );
		cfg.getProperties().put( AvailableSettings.DIALECT, NoTupleSupportDialect.class.getName() );
		cfg.getProperties().put( AvailableSettings.HBM2DDL_AUTO, "create-drop" );
		sessionFactory = cfg.buildSessionFactory();
	}

	@After
	public void releaseSessionFactory() {
		sessionFactory.close();
	}

	@Test
	public void testImplicitTupleNotEquals() {
		final String hql = "from TheEntity e where e.compositeValue <> :p1";
		HQLQueryPlan queryPlan = ( (SessionFactoryImplementor) sessionFactory ).getQueryPlanCache()
				.getHQLQueryPlan( hql, false, Collections.emptyMap() );

		assertEquals( 1, queryPlan.getSqlStrings().length );
		System.out.println( " SQL : " + queryPlan.getSqlStrings()[0] );
		assertTrue( queryPlan.getSqlStrings()[0].contains( "<>" ) );
	}

	@Test
	public void testImplicitTupleNotInList() {
		final String hql = "from TheEntity e where e.compositeValue not in (:p1,:p2)";
		HQLQueryPlan queryPlan = ( (SessionFactoryImplementor) sessionFactory ).getQueryPlanCache()
				.getHQLQueryPlan( hql, false, Collections.emptyMap() );

		assertEquals( 1, queryPlan.getSqlStrings().length );
		System.out.println( " SQL : " + queryPlan.getSqlStrings()[0] );
		assertTrue( queryPlan.getSqlStrings()[0].contains( "<>" ) );
	}

	public static class NoTupleSupportDialect extends H2Dialect {
		@Override
		public boolean supportsRowValueConstructorSyntax() {
			return false;
		}

		@Override
		public boolean supportsRowValueConstructorSyntaxInInList() {
			return false;
		}
	}
}
