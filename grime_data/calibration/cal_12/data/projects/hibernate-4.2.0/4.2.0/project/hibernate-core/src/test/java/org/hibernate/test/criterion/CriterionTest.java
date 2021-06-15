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
package org.hibernate.test.criterion;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.IrrelevantEntity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LikeExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.type.Type;

import org.junit.Test;

import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ebersole
 */
public class CriterionTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testIlikeRendering() {
		SessionFactory sf = new Configuration()
				.addAnnotatedClass( IrrelevantEntity.class )
				.setProperty( AvailableSettings.DIALECT, IlikeSupportingDialect.class.getName() )
				.setProperty( Environment.HBM2DDL_AUTO, "create-drop" )
				.buildSessionFactory();
		final Criteria criteria = sf.openSession().createCriteria( IrrelevantEntity.class );
		final CriteriaQueryTranslator translator = new CriteriaQueryTranslator( 
				(SessionFactoryImplementor) sf, 
				(CriteriaImpl) criteria, 
				IrrelevantEntity.class.getName(), 
				"a" 
		);
		final Criterion ilikeExpression = Restrictions.ilike( "name", "abc" );
		final String ilikeExpressionSqlFragment = ilikeExpression.toSqlString( criteria, translator );
		assertEquals( "a.name insensitiveLike ?", ilikeExpressionSqlFragment );
	}

	@Test
	public void testIlikeMimicing() {
		SessionFactory sf = new Configuration()
				.addAnnotatedClass( IrrelevantEntity.class )
				.setProperty( AvailableSettings.DIALECT, NonIlikeSupportingDialect.class.getName() )
				.setProperty( Environment.HBM2DDL_AUTO, "create-drop" )
				.buildSessionFactory();
		final Criteria criteria = sf.openSession().createCriteria( IrrelevantEntity.class );
		final CriteriaQueryTranslator translator = new CriteriaQueryTranslator(
				(SessionFactoryImplementor) sf,
				(CriteriaImpl) criteria,
				IrrelevantEntity.class.getName(),
				"a"
		);
		final Criterion ilikeExpression = Restrictions.ilike( "name", "abc" );
		final String ilikeExpressionSqlFragment = ilikeExpression.toSqlString( criteria, translator );
		assertEquals( "lowLowLow(a.name) like ?", ilikeExpressionSqlFragment );
	}

	public static class IlikeSupportingDialect extends Dialect {
		@Override
		public boolean supportsCaseInsensitiveLike() {
			return true;
		}

		@Override
		public String getCaseInsensitiveLike() {
			return "insensitiveLike";
		}
	}

	public static class NonIlikeSupportingDialect extends Dialect {
		@Override
		public boolean supportsCaseInsensitiveLike() {
			return false;
		}

		@Override
		public String getCaseInsensitiveLike() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getLowercaseFunction() {
			return "lowLowLow";
		}
	}
}
