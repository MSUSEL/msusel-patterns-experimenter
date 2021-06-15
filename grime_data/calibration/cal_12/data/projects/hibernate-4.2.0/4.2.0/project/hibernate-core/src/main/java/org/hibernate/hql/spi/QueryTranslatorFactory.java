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
package org.hibernate.hql.spi;
import java.util.Map;

import org.hibernate.engine.spi.SessionFactoryImplementor;

/**
 * Facade for generation of {@link QueryTranslator} and {@link FilterTranslator} instances.
 *
 * @author Gavin King
 */
public interface QueryTranslatorFactory {
	/**
	 * Construct a {@link QueryTranslator} instance capable of translating
	 * an HQL query string.
	 *
	 * @param queryIdentifier The query-identifier (used in
	 * {@link org.hibernate.stat.QueryStatistics} collection). This is
	 * typically the same as the queryString parameter except for the case of
	 * split polymorphic queries which result in multiple physical sql
	 * queries.
	 * @param queryString The query string to be translated
	 * @param filters Currently enabled filters
	 * @param factory The session factory.
	 * @return an appropriate translator.
	 */
	public QueryTranslator createQueryTranslator(String queryIdentifier, String queryString, Map filters, SessionFactoryImplementor factory);

	/**
	 * Construct a {@link FilterTranslator} instance capable of translating
	 * an HQL filter string.
	 *
	 * @see #createQueryTranslator
	 */
	public FilterTranslator createFilterTranslator(String queryIdentifier, String queryString, Map filters, SessionFactoryImplementor factory);
}
