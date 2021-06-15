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
package org.hibernate.test.annotations.xml.ejb3;

import org.jboss.byteman.contrib.bmunit.BMRule;
import org.jboss.byteman.contrib.bmunit.BMRules;
import org.jboss.byteman.contrib.bmunit.BMUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.byteman.BytemanHelper;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;

@TestForIssue(jiraKey = "HHH-6271")
@RunWith(BMUnitRunner.class)
public class OrmVersion1SupportedTest extends BaseCoreFunctionalTestCase {
	@Test
	@BMRules(rules = {
			@BMRule(targetClass = "org.hibernate.internal.CoreMessageLogger_$logger",
					targetMethod = "parsingXmlError",
					helper = "org.hibernate.testing.byteman.BytemanHelper",
					action = "countInvocation()",
					name = "testOrm1Support"),
			@BMRule(targetClass = "org.hibernate.internal.CoreMessageLogger_$logger",
					targetMethod = "parsingXmlErrorForFile",
					helper = "org.hibernate.testing.byteman.BytemanHelper",
					action = "countInvocation()",
					name = "testOrm1Support")
	})
	public void testOrm1Support() {
		// need to call buildSessionFactory, because this test is not using org.hibernate.testing.junit4.CustomRunner
		buildSessionFactory();

		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Light light = new Light();
		light.name = "the light at the end of the tunnel";
		s.persist( light );
		s.flush();
		s.clear();

		assertEquals( 1, s.getNamedQuery( "find.the.light" ).list().size() );
		tx.rollback();
		s.close();

		assertEquals( "HHH00196 should not be called", 0, BytemanHelper.getAndResetInvocationCount() );
	}

	@Override
	protected String[] getXmlFiles() {
		return new String[] { "org/hibernate/test/annotations/xml/ejb3/orm2.xml" };
	}
}
