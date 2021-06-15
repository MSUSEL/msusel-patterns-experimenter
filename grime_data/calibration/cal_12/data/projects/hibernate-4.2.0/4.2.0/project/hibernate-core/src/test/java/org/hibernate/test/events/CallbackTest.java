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
package org.hibernate.test.events;

import java.util.Set;

import org.junit.Test;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.DeleteEvent;
import org.hibernate.event.spi.DeleteEventListener;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.BootstrapServiceRegistryBuilder;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;


/**
 * CallbackTest implementation
 *
 * @author Steve Ebersole
 */
public class CallbackTest extends BaseCoreFunctionalTestCase {
	private TestingObserver observer = new TestingObserver();
	private TestingListener listener = new TestingListener();

	@Override
    public String[] getMappings() {
		return NO_MAPPINGS;
	}

	@Override
    public void configure(Configuration cfg) {
		cfg.setSessionFactoryObserver( observer );
	}

	@Override
	protected void prepareBootstrapRegistryBuilder(BootstrapServiceRegistryBuilder builder) {
		super.prepareBootstrapRegistryBuilder( builder );
		builder.with(
				new Integrator() {

				    @Override
					public void integrate(
							Configuration configuration,
							SessionFactoryImplementor sessionFactory,
							SessionFactoryServiceRegistry serviceRegistry) {
                        integrate(serviceRegistry);
					}

                    @Override
				    public void integrate( MetadataImplementor metadata,
				                           SessionFactoryImplementor sessionFactory,
				                           SessionFactoryServiceRegistry serviceRegistry ) {
                        integrate(serviceRegistry);
				    }

                    private void integrate( SessionFactoryServiceRegistry serviceRegistry ) {
                        serviceRegistry.getService( EventListenerRegistry.class ).setListeners(EventType.DELETE, listener);
                        listener.initialize();
                    }

					@Override
					public void disintegrate(
							SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
						listener.cleanup();
					}
				}
		);
	}

	@Test
	public void testCallbacks() {
		assertEquals( "observer not notified of creation", 1, observer.creationCount );
		assertEquals( "listener not notified of creation", 1, listener.initCount );

		sessionFactory().close();

		assertEquals( "observer not notified of close", 1, observer.closedCount );
		assertEquals( "listener not notified of close", 1, listener.destoryCount );
	}

	private static class TestingObserver implements SessionFactoryObserver {
		private int creationCount = 0;
		private int closedCount = 0;

		public void sessionFactoryCreated(SessionFactory factory) {
			creationCount++;
		}

		public void sessionFactoryClosed(SessionFactory factory) {
			closedCount++;
		}
	}

	private static class TestingListener implements DeleteEventListener {
		private int initCount = 0;
		private int destoryCount = 0;

		public void initialize() {
			initCount++;
		}

		public void cleanup() {
			destoryCount++;
		}

		public void onDelete(DeleteEvent event) throws HibernateException {
		}

		public void onDelete(DeleteEvent event, Set transientEntities) throws HibernateException {
		}
	}
}
