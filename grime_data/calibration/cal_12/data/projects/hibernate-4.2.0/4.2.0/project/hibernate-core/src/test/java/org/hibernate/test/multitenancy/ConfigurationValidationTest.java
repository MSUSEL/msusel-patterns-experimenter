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
package org.hibernate.test.multitenancy;

import org.junit.Test;

import org.hibernate.ConnectionReleaseMode;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.ServiceException;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.env.ConnectionProviderBuilder;
import org.hibernate.testing.junit4.BaseUnitTestCase;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue(jiraKey = "HHH-7311")
public class ConfigurationValidationTest extends BaseUnitTestCase {
	@Test(expected = ServiceException.class)
	public void testInvalidConnectionProvider() {
		Configuration cfg = new Configuration();
		cfg.getProperties().put( Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA );
		cfg.setProperty( Environment.MULTI_TENANT_CONNECTION_PROVIDER, "class.not.present.in.classpath" );
		cfg.buildMappings();
		ServiceRegistryImplementor serviceRegistry = (ServiceRegistryImplementor) new ServiceRegistryBuilder()
				.applySettings( cfg.getProperties() ).buildServiceRegistry();
		cfg.buildSessionFactory( serviceRegistry );
	}

	@Test
	public void testReleaseMode() {
		Configuration cfg = new Configuration();
		cfg.getProperties().put( Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA );
		cfg.getProperties().put( Environment.RELEASE_CONNECTIONS, ConnectionReleaseMode.AFTER_STATEMENT.name() );
		cfg.buildMappings();

		ServiceRegistryImplementor serviceRegistry = (ServiceRegistryImplementor) new ServiceRegistryBuilder()
				.applySettings( cfg.getProperties() )
				.addService(
						MultiTenantConnectionProvider.class,
						new TestingConnectionProvider(
								new TestingConnectionProvider.NamedConnectionProviderPair(
										"acme",
										ConnectionProviderBuilder.buildConnectionProvider( "acme" )
								)
						)
				)
				.buildServiceRegistry();

		cfg.buildSessionFactory( serviceRegistry );
	}
}
