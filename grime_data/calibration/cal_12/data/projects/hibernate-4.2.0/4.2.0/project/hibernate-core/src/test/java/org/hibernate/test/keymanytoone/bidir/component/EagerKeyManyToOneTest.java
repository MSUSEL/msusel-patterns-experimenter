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
package org.hibernate.test.keymanytoone.bidir.component;

import org.junit.Test;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.internal.DefaultLoadEventListener;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.LoadEvent;
import org.hibernate.event.spi.LoadEventListener;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.BootstrapServiceRegistryBuilder;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Steve Ebersole
 */
@SuppressWarnings( {"unchecked"})
public class EagerKeyManyToOneTest extends BaseCoreFunctionalTestCase {
	@Override
    public String[] getMappings() {
		return new String[] { "keymanytoone/bidir/component/EagerMapping.hbm.xml" };
	}

	@Override
    public void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( Environment.GENERATE_STATISTICS, "true" );
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
                        serviceRegistry.getService( EventListenerRegistry.class ).prependListeners(EventType.LOAD,
                                                                                                   new CustomLoadListener());
                    }

					@Override
					public void disintegrate(
							SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
					}
				}
		);
	}

	@Test
	public void testSaveCascadedToKeyManyToOne() {
		sessionFactory().getStatistics().clear();

		// test cascading a save to an association with a key-many-to-one which refers to a
		// just saved entity
		Session s = openSession();
		s.beginTransaction();
		Customer cust = new Customer( "Acme, Inc." );
		Order order = new Order( new Order.Id( cust, 1 ) );
		cust.getOrders().add( order );
		s.save( cust );
		s.flush();
		assertEquals( 2, sessionFactory().getStatistics().getEntityInsertCount() );
		s.delete( cust );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testLoadingStrategies() {
		sessionFactory().getStatistics().clear();

		Session s = openSession();
		s.beginTransaction();
		Customer cust = new Customer( "Acme, Inc." );
		Order order = new Order( new Order.Id( cust, 1 ) );
		cust.getOrders().add( order );
		s.save( cust );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();

		cust = ( Customer ) s.createQuery( "from Customer" ).uniqueResult();
		assertEquals( 1, cust.getOrders().size() );
		s.clear();

		cust = ( Customer ) s.createQuery( "from Customer c join fetch c.orders" ).uniqueResult();
		assertEquals( 1, cust.getOrders().size() );
		s.clear();

		cust = ( Customer ) s.createQuery( "from Customer c join fetch c.orders as o join fetch o.id.customer" ).uniqueResult();
		assertEquals( 1, cust.getOrders().size() );
		s.clear();

		cust = ( Customer ) s.createCriteria( Customer.class ).uniqueResult();
		assertEquals( 1, cust.getOrders().size() );
		s.clear();

		s.delete( cust );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	@TestForIssue( jiraKey = "HHH-2277")
	public void testLoadEntityWithEagerFetchingToKeyManyToOneReferenceBackToSelf() {
		sessionFactory().getStatistics().clear();

		// long winded method name to say that this is a test specifically for HHH-2277 ;)
		// essentially we have a bidirectional association where one side of the
		// association is actually part of a composite PK.
		//
		// The way these are mapped causes the problem because both sides
		// are defined as eager which leads to the infinite loop; if only
		// one side is marked as eager, then all is ok.  In other words the
		// problem arises when both pieces of instance data are coming from
		// the same result set.  This is because no "entry" can be placed
		// into the persistence context for the association with the
		// composite key because we are in the process of trying to build
		// the composite-id instance
		Session s = openSession();
		s.beginTransaction();
		Customer cust = new Customer( "Acme, Inc." );
		Order order = new Order( new Order.Id( cust, 1 ) );
		cust.getOrders().add( order );
		s.save( cust );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		try {
			cust = ( Customer ) s.get( Customer.class, cust.getId() );
		}
		catch( OverflowCondition overflow ) {
			fail( "get()/load() caused overflow condition" );
		}
		s.delete( cust );
		s.getTransaction().commit();
		s.close();
	}

	private static class OverflowCondition extends RuntimeException {
	}

	private static class CustomLoadListener extends DefaultLoadEventListener {
		private int internalLoadCount = 0;
		@Override
        public void onLoad(LoadEvent event, LoadType loadType) throws HibernateException {
			if ( LoadEventListener.INTERNAL_LOAD_EAGER.getName().equals( loadType.getName() ) ) {
				internalLoadCount++;
				if ( internalLoadCount > 10 ) {
					throw new OverflowCondition();
				}
			}
			super.onLoad( event, loadType );
			internalLoadCount--;
		}
	}
}
