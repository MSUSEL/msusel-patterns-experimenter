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
package org.hibernate.test.multitenancy.schema;

import org.junit.Assert;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import org.hibernate.testing.TestForIssue;

/**
 * SessionFactory has to use the {@link CurrentTenantIdentifierResolver} when
 * {@link SessionFactory#openSession()} is called.
 *
 * @author Stefan Schulze
 * @author Steve Ebersole
 */
@TestForIssue(jiraKey = "HHH-7306")
public class CurrentTenantResolverMultiTenancyTest extends SchemaBasedMultiTenancyTest {

	private TestCurrentTenantIdentifierResolver currentTenantResolver = new TestCurrentTenantIdentifierResolver();


	@Override
	protected Configuration buildConfiguration() {
		Configuration cfg = super.buildConfiguration();
		cfg.setCurrentTenantIdentifierResolver( currentTenantResolver );
		return cfg;
	}

	@Override
	protected Session getNewSession(String tenant) {
		currentTenantResolver.currentTenantIdentifier = tenant;
		Session session = sessionFactory.openSession();
		Assert.assertEquals( tenant, session.getTenantIdentifier() );
		return session;
	}


	private static class TestCurrentTenantIdentifierResolver implements CurrentTenantIdentifierResolver {
		private String currentTenantIdentifier;

		@Override
		public boolean validateExistingCurrentSessions() {
			return false;
		}

		@Override
		public String resolveCurrentTenantIdentifier() {
			return currentTenantIdentifier;
		}
	}
}
