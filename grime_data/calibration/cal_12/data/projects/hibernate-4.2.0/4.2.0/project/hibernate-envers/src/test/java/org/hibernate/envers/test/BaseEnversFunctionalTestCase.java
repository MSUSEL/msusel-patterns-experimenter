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
package org.hibernate.envers.test;

import java.util.Arrays;
import java.util.List;

import org.hibernate.cfg.Configuration;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author Strong Liu (stliu@hibernate.org)
 */
@RunWith(EnversRunner.class)
public abstract class BaseEnversFunctionalTestCase extends BaseCoreFunctionalTestCase {
	private String auditStrategy;

	@Parameterized.Parameters
	public static List<Object[]> data() {
		return Arrays.asList(
				new Object[] { null },
				new Object[] { "org.hibernate.envers.strategy.ValidityAuditStrategy" }
		);
	}

	public void setTestData(Object[] data) {
		auditStrategy = (String) data[0];
	}

	public String getAuditStrategy() {
		return auditStrategy;
	}

	protected Session getSession() {
		if ( session == null || !session.isOpen() ) {
			return openSession();
		}
		return session;
	}

	protected AuditReader getAuditReader(){
		return AuditReaderFactory.get( getSession() );
	}

    @Override
    protected Configuration constructConfiguration() {
        Configuration configuration = super.constructConfiguration();
        configuration.setProperty("org.hibernate.envers.use_revision_entity_with_native_id", "false");
        return configuration;
    }

    @Override
	protected String getBaseForMappings() {
		return "";
	}
}
