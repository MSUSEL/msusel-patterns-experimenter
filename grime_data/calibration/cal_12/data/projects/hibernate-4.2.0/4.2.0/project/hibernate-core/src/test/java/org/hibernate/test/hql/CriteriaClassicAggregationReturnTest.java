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
import java.util.Collections;

import org.junit.Test;

import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.function.ClassicAvgFunction;
import org.hibernate.dialect.function.ClassicCountFunction;
import org.hibernate.dialect.function.ClassicSumFunction;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.hql.internal.classic.ClassicQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.hql.spi.QueryTranslatorFactory;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BigIntegerType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;

import static org.junit.Assert.assertEquals;

/**
 * @author Steve Ebersole
 */
public class CriteriaClassicAggregationReturnTest extends QueryTranslatorTestCase {
	@Override
	public void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.addSqlFunction( "count", new ClassicCountFunction() );
		cfg.addSqlFunction( "avg", new ClassicAvgFunction() );
		cfg.addSqlFunction( "sum", new ClassicSumFunction() );
	}

	@Test
	public void testClassicHQLAggregationReturnTypes() {
		// EJB3: COUNT returns Long
		QueryTranslatorImpl translator = createNewQueryTranslator( "select count(*) from Human h", sessionFactory() );
		assertEquals( "incorrect return type count", 1, translator.getReturnTypes().length );
		assertEquals( "incorrect return type", IntegerType.INSTANCE, translator.getReturnTypes()[0] );

		translator = createNewQueryTranslator( "select count(h.heightInches) from Human h", sessionFactory() );
		assertEquals( "incorrect return type count", 1, translator.getReturnTypes().length );
		assertEquals( "incorrect return type", IntegerType.INSTANCE, translator.getReturnTypes()[0] );

		// MAX, MIN return the type of the state-field to which they are applied.
		translator = createNewQueryTranslator( "select max(h.heightInches) from Human h", sessionFactory() );
		assertEquals( "incorrect return type count", 1, translator.getReturnTypes().length );
		assertEquals( "incorrect return type", DoubleType.INSTANCE, translator.getReturnTypes()[0] );

		translator = createNewQueryTranslator( "select max(h.id) from Human h", sessionFactory() );
		assertEquals( "incorrect return type count", 1, translator.getReturnTypes().length );
		assertEquals( "incorrect return type", LongType.INSTANCE, translator.getReturnTypes()[0] );

		// AVG returns Float integrals, and otherwise the field type.
		translator = createNewQueryTranslator( "select avg(h.heightInches) from Human h", sessionFactory() );
		assertEquals( "incorrect return type count", 1, translator.getReturnTypes().length );
		assertEquals( "incorrect return type", DoubleType.INSTANCE, translator.getReturnTypes()[0] );

		translator = createNewQueryTranslator( "select avg(h.id) from Human h", sessionFactory() );
		assertEquals( "incorrect return type count", 1, translator.getReturnTypes().length );
		assertEquals( "incorrect return type", FloatType.INSTANCE, translator.getReturnTypes()[0] );

		translator = createNewQueryTranslator( "select avg(h.bigIntegerValue) from Human h", sessionFactory() );
		assertEquals( "incorrect return type count", 1, translator.getReturnTypes().length );
		assertEquals( "incorrect return type", BigIntegerType.INSTANCE, translator.getReturnTypes()[0] );

        // SUM returns underlying type sum
 	    translator = createNewQueryTranslator( "select sum(h.id) from Human h", sessionFactory() );
		assertEquals( "incorrect return type count", 1, translator.getReturnTypes().length );
		assertEquals( "incorrect return type", LongType.INSTANCE, translator.getReturnTypes()[0] );

		translator = createNewQueryTranslator( "select sum(h.intValue) from Human h", sessionFactory() );
		assertEquals( "incorrect return type count", 1, translator.getReturnTypes().length );
		assertEquals( "incorrect return type", IntegerType.INSTANCE, translator.getReturnTypes()[0] );

		translator = createNewQueryTranslator( "select sum(h.heightInches) from Human h", sessionFactory() );
		assertEquals( "incorrect return type count", 1, translator.getReturnTypes().length );
		assertEquals( "incorrect return type", DoubleType.INSTANCE, translator.getReturnTypes()[0] );

		translator = createNewQueryTranslator( "select sum(h.floatValue) from Human h", sessionFactory() );
		assertEquals( "incorrect return type count", 1, translator.getReturnTypes().length );
		assertEquals( "incorrect return type", FloatType.INSTANCE, translator.getReturnTypes()[0] );

		translator = createNewQueryTranslator( "select sum(h.bigIntegerValue) from Human h", sessionFactory() );
		assertEquals( "incorrect return type count", 1, translator.getReturnTypes().length );
		assertEquals( "incorrect return type", BigIntegerType.INSTANCE, translator.getReturnTypes()[0] );

		translator = createNewQueryTranslator( "select sum(h.bigDecimalValue) from Human h", sessionFactory() );
		assertEquals( "incorrect return type count", 1, translator.getReturnTypes().length );
		assertEquals( "incorrect return type", BigDecimalType.INSTANCE, translator.getReturnTypes()[0] );

		// special case to test classicquery special case handling of count(*)
		String hql = "select count(*) from Human h";
		QueryTranslatorFactory classic = new ClassicQueryTranslatorFactory();
		QueryTranslator oldQueryTranslator = classic.createQueryTranslator( hql, hql, Collections.EMPTY_MAP, sessionFactory() );
		oldQueryTranslator.compile( Collections.EMPTY_MAP, true);
		assertEquals( "incorrect return type count", 1, oldQueryTranslator.getReturnTypes().length );
		assertEquals( "incorrect return type", IntegerType.INSTANCE, oldQueryTranslator.getReturnTypes()[0] );

	}
}
