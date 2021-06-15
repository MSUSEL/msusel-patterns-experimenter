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
package org.hibernate.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.cache.internal.RegionFactoryInitiator;
import org.hibernate.engine.jdbc.batch.internal.BatchBuilderInitiator;
import org.hibernate.engine.jdbc.internal.JdbcServicesInitiator;
import org.hibernate.engine.transaction.internal.TransactionFactoryInitiator;
import org.hibernate.id.factory.internal.MutableIdentifierGeneratorFactoryInitiator;
import org.hibernate.persister.internal.PersisterClassResolverInitiator;
import org.hibernate.persister.internal.PersisterFactoryInitiator;
import org.hibernate.service.config.internal.ConfigurationServiceInitiator;
import org.hibernate.service.internal.SessionFactoryServiceRegistryFactoryInitiator;
import org.hibernate.service.jdbc.connections.internal.ConnectionProviderInitiator;
import org.hibernate.service.jdbc.connections.internal.MultiTenantConnectionProviderInitiator;
import org.hibernate.service.jdbc.dialect.internal.DialectFactoryInitiator;
import org.hibernate.service.jdbc.dialect.internal.DialectResolverInitiator;
import org.hibernate.service.jmx.internal.JmxServiceInitiator;
import org.hibernate.service.jndi.internal.JndiServiceInitiator;
import org.hibernate.service.jta.platform.internal.JtaPlatformInitiator;
import org.hibernate.service.spi.BasicServiceInitiator;
import org.hibernate.tool.hbm2ddl.ImportSqlCommandExtractorInitiator;

/**
 * Central definition of the standard set of service initiators defined by Hibernate.
 * 
 * @author Steve Ebersole
 */
public class StandardServiceInitiators {
	public static List<BasicServiceInitiator> LIST = buildStandardServiceInitiatorList();

	private static List<BasicServiceInitiator> buildStandardServiceInitiatorList() {
		final List<BasicServiceInitiator> serviceInitiators = new ArrayList<BasicServiceInitiator>();

		serviceInitiators.add( ConfigurationServiceInitiator.INSTANCE );
		serviceInitiators.add( ImportSqlCommandExtractorInitiator.INSTANCE );

		serviceInitiators.add( JndiServiceInitiator.INSTANCE );
		serviceInitiators.add( JmxServiceInitiator.INSTANCE );

		serviceInitiators.add( PersisterClassResolverInitiator.INSTANCE );
		serviceInitiators.add( PersisterFactoryInitiator.INSTANCE );

		serviceInitiators.add( ConnectionProviderInitiator.INSTANCE );
		serviceInitiators.add( MultiTenantConnectionProviderInitiator.INSTANCE );
		serviceInitiators.add( DialectResolverInitiator.INSTANCE );
		serviceInitiators.add( DialectFactoryInitiator.INSTANCE );
		serviceInitiators.add( BatchBuilderInitiator.INSTANCE );
		serviceInitiators.add( JdbcServicesInitiator.INSTANCE );

		serviceInitiators.add( MutableIdentifierGeneratorFactoryInitiator.INSTANCE);

		serviceInitiators.add( JtaPlatformInitiator.INSTANCE );
		serviceInitiators.add( TransactionFactoryInitiator.INSTANCE );

		serviceInitiators.add( SessionFactoryServiceRegistryFactoryInitiator.INSTANCE );

		serviceInitiators.add( RegionFactoryInitiator.INSTANCE );

		return Collections.unmodifiableList( serviceInitiators );
	}
}
