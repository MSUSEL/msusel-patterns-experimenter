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
package org.hibernate.connection;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.service.jdbc.connections.internal.ConnectionProviderInitiator;
import org.hibernate.testing.junit4.BaseUnitTestCase;

/**
 * @author kbow
 */
public class PropertiesTest extends BaseUnitTestCase {
	@Test
	public void testProperties() throws Exception {
		final Properties props = new Properties();

		props.put("rpt.1.hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
		props.put("rpt.2.hibernate.connection.driver_class", "org.apache.derby.jdbc.ClientDriver");
		props.put("rpt.3.hibernate.connection.url", "jdbc:derby://localhost:1527/db/reports.db");
		props.put("rpt.4.hibernate.connection.username", "sa");
		props.put("rpt.5.hibernate.connection.password_enc", "76f271db3661fd50082e68d4b953fbee");
		props.put("rpt.6.hibernate.connection.password_enc", "76f271db3661fd50082e68d4b953fbee");
		props.put("hibernate.connection.create", "true");

		final Properties outputProps = ConnectionProviderInitiator.getConnectionProperties( props );
		Assert.assertEquals( 1, outputProps.size() );
		Assert.assertEquals( "true", outputProps.get( "create" ) );
	}

}
