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
package org.hibernate.test.querycache;

import org.junit.Test;

import org.hibernate.CacheMode;
import org.hibernate.testing.FailureExpected;

/**
 * @author Gail Badner
 */
public class HqlQueryCacheIgnoreResultTransformerTest extends AbstractQueryCacheResultTransformerTest {
	@Override
	protected CacheMode getQueryCacheMode() {
		return CacheMode.IGNORE;
	}

	@Override
	protected void runTest(HqlExecutor hqlExecutor, CriteriaExecutor criteriaExecutor, ResultChecker checker, boolean isSingleResult)
		throws Exception {
		createData();
		if ( hqlExecutor != null ) {
			runTest( hqlExecutor, checker, isSingleResult );
		}
		deleteData();
	}

	@Test
	@Override
	@FailureExpected( jiraKey = "N/A", message = "HQL query using Transformers.ALIAS_TO_ENTITY_MAP with no projection" )
	public void testAliasToEntityMapNoProjectionList() throws Exception {
		super.testAliasToEntityMapNoProjectionList();
	}

	@Test
	@Override
	@FailureExpected( jiraKey = "N/A", message = "HQL query using Transformers.ALIAS_TO_ENTITY_MAP with no projection" )
	public void testAliasToEntityMapNoProjectionMultiAndNullList() throws Exception {
		super.testAliasToEntityMapNoProjectionMultiAndNullList();
	}

	@Test
	@Override
	@FailureExpected( jiraKey = "N/A", message = "HQL query using Transformers.ALIAS_TO_ENTITY_MAP with no projection" )
	public void testAliasToEntityMapNoProjectionNullAndNonNullAliasList() throws Exception {
		super.testAliasToEntityMapNoProjectionNullAndNonNullAliasList();
	}

	@Test
	@Override
	@FailureExpected( jiraKey = "HHH-3345", message = "HQL query using 'select new' and 'join fetch'" )
	public void testMultiSelectNewMapUsingAliasesWithFetchJoinList() throws Exception {
		super.testMultiSelectNewMapUsingAliasesWithFetchJoinList();
	}

}
