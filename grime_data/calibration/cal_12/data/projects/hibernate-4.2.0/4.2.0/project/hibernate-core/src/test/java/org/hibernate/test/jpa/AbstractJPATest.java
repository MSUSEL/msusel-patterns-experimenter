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
package org.hibernate.test.jpa;

import java.io.Serializable;
import java.util.IdentityHashMap;
import javax.persistence.EntityNotFoundException;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.spi.CascadingAction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.internal.DefaultAutoFlushEventListener;
import org.hibernate.event.internal.DefaultFlushEntityEventListener;
import org.hibernate.event.internal.DefaultFlushEventListener;
import org.hibernate.event.internal.DefaultPersistEventListener;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.AutoFlushEventListener;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.FlushEntityEventListener;
import org.hibernate.event.spi.FlushEventListener;
import org.hibernate.event.spi.PersistEventListener;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.proxy.EntityNotFoundDelegate;
import org.hibernate.service.BootstrapServiceRegistryBuilder;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * An abstract test for all JPA spec related tests.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractJPATest extends BaseCoreFunctionalTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "jpa/Part.hbm.xml", "jpa/Item.hbm.xml", "jpa/MyEntity.hbm.xml" };
	}

	@Override
	public void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( Environment.JPAQL_STRICT_COMPLIANCE, "true" );
		cfg.setProperty( Environment.USE_SECOND_LEVEL_CACHE, "false" );
		cfg.setEntityNotFoundDelegate( new JPAEntityNotFoundDelegate() );
	}

	@Override
	protected void prepareBootstrapRegistryBuilder(BootstrapServiceRegistryBuilder builder) {
		builder.with(
				new Integrator() {

					@Override
					public void integrate(
							Configuration configuration,
							SessionFactoryImplementor sessionFactory,
							SessionFactoryServiceRegistry serviceRegistry) {
						integrate( serviceRegistry );
					}

					@Override
					public void integrate(
							MetadataImplementor metadata,
							SessionFactoryImplementor sessionFactory,
							SessionFactoryServiceRegistry serviceRegistry) {
						integrate( serviceRegistry );
					}

					private void integrate(SessionFactoryServiceRegistry serviceRegistry) {
						EventListenerRegistry eventListenerRegistry = serviceRegistry.getService( EventListenerRegistry.class );
						eventListenerRegistry.setListeners( EventType.PERSIST, buildPersistEventListeners() );
						eventListenerRegistry.setListeners(
								EventType.PERSIST_ONFLUSH, buildPersisOnFlushEventListeners()
						);
						eventListenerRegistry.setListeners( EventType.AUTO_FLUSH, buildAutoFlushEventListeners() );
						eventListenerRegistry.setListeners( EventType.FLUSH, buildFlushEventListeners() );
						eventListenerRegistry.setListeners( EventType.FLUSH_ENTITY, buildFlushEntityEventListeners() );
					}

					@Override
					public void disintegrate(
							SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
					}
				}
		);
	}

	@Override
	public String getCacheConcurrencyStrategy() {
		// no second level caching
		return null;
	}


	// mimic specific exception aspects of the JPA environment ~~~~~~~~~~~~~~~~

	private static class JPAEntityNotFoundDelegate implements EntityNotFoundDelegate {
		public void handleEntityNotFound(String entityName, Serializable id) {
			throw new EntityNotFoundException("Unable to find " + entityName  + " with id " + id);
		}
	}

	// mimic specific event aspects of the JPA environment ~~~~~~~~~~~~~~~~~~~~

	protected PersistEventListener[] buildPersistEventListeners() {
		return new PersistEventListener[] { new JPAPersistEventListener() };
	}

	protected PersistEventListener[] buildPersisOnFlushEventListeners() {
		return new PersistEventListener[] { new JPAPersistOnFlushEventListener() };
	}

	protected AutoFlushEventListener[] buildAutoFlushEventListeners() {
		return new AutoFlushEventListener[] { JPAAutoFlushEventListener.INSTANCE };
	}

	protected FlushEventListener[] buildFlushEventListeners() {
		return new FlushEventListener[] { JPAFlushEventListener.INSTANCE };
	}

	protected FlushEntityEventListener[] buildFlushEntityEventListeners() {
		return new FlushEntityEventListener[] { new JPAFlushEntityEventListener() };
	}

	public static class JPAPersistEventListener extends DefaultPersistEventListener {
		// overridden in JPA impl for entity callbacks...
	}

	public static class JPAPersistOnFlushEventListener extends JPAPersistEventListener {
		@Override
        protected CascadingAction getCascadeAction() {
			return CascadingAction.PERSIST_ON_FLUSH;
		}
	}

	public static class JPAAutoFlushEventListener extends DefaultAutoFlushEventListener {
		// not sure why EM code has this ...
		public static final AutoFlushEventListener INSTANCE = new JPAAutoFlushEventListener();

		@Override
        protected CascadingAction getCascadingAction() {
			return CascadingAction.PERSIST_ON_FLUSH;
		}

		@Override
        protected Object getAnything() {
			return new IdentityHashMap( 10 );
		}
	}

	public static class JPAFlushEventListener extends DefaultFlushEventListener {
		// not sure why EM code has this ...
		public static final FlushEventListener INSTANCE = new JPAFlushEventListener();

		@Override
        protected CascadingAction getCascadingAction() {
			return CascadingAction.PERSIST_ON_FLUSH;
		}

		@Override
        protected Object getAnything() {
			return new IdentityHashMap( 10 );
		}
	}

	public static class JPAFlushEntityEventListener extends DefaultFlushEntityEventListener {
		// in JPA, used mainly for preUpdate callbacks...
	}
}
