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
package org.hibernate.test.cfg.persister;

import org.junit.Test;

import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.persister.spi.PersisterClassResolver;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class PersisterClassProviderTest extends BaseUnitTestCase {
	@Test
	public void testPersisterClassProvider() throws Exception {

		Configuration cfg = new Configuration();
		cfg.addAnnotatedClass( Gate.class );
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
				.applySettings( cfg.getProperties() )
				.buildServiceRegistry();
		//no exception as the GoofyPersisterClassProvider is not set
		SessionFactory sessionFactory = cfg.buildSessionFactory( serviceRegistry );
		sessionFactory.close();
		ServiceRegistryBuilder.destroy( serviceRegistry );

		serviceRegistry = new ServiceRegistryBuilder()
				.applySettings( cfg.getProperties() )
				.addService( PersisterClassResolver.class, new GoofyPersisterClassProvider() )
				.buildServiceRegistry();
		cfg = new Configuration();
		cfg.addAnnotatedClass( Gate.class );
		try {
			sessionFactory = cfg.buildSessionFactory( serviceRegistry );
			sessionFactory.close();
            fail("The entity persister should be overridden");
		}
		catch ( MappingException e ) {
			assertEquals(
					"The entity persister should be overridden",
					GoofyPersisterClassProvider.NoopEntityPersister.class,
					( (GoofyException) e.getCause() ).getValue()
			);
		}
		finally {
			ServiceRegistryBuilder.destroy( serviceRegistry );
		}

		cfg = new Configuration();
		cfg.addAnnotatedClass( Portal.class );
		cfg.addAnnotatedClass( Window.class );
		serviceRegistry = new ServiceRegistryBuilder()
				.applySettings( cfg.getProperties() )
				.addService( PersisterClassResolver.class, new GoofyPersisterClassProvider() )
				.buildServiceRegistry();
		try {
			sessionFactory = cfg.buildSessionFactory( serviceRegistry );
			sessionFactory.close();
            fail("The collection persister should be overridden but not the entity persister");
		}
		catch ( MappingException e ) {
			assertEquals(
					"The collection persister should be overridden but not the entity persister",
					GoofyPersisterClassProvider.NoopCollectionPersister.class,
					( (GoofyException) e.getCause() ).getValue() );
		}
		finally {
			ServiceRegistryBuilder.destroy( serviceRegistry );
		}


        cfg = new Configuration();
		cfg.addAnnotatedClass( Tree.class );
		cfg.addAnnotatedClass( Palmtree.class );
		serviceRegistry = new ServiceRegistryBuilder()
				.applySettings( cfg.getProperties() )
				.addService( PersisterClassResolver.class, new GoofyPersisterClassProvider() )
				.buildServiceRegistry();
		try {
			sessionFactory = cfg.buildSessionFactory( serviceRegistry );
			sessionFactory.close();
            fail("The entity persisters should be overridden in a class hierarchy");
		}
		catch ( MappingException e ) {
			assertEquals(
					"The entity persisters should be overridden in a class hierarchy",
					GoofyPersisterClassProvider.NoopEntityPersister.class,
					( (GoofyException) e.getCause() ).getValue() );
		}
		finally {
			ServiceRegistryBuilder.destroy( serviceRegistry );
		}
	}
}
