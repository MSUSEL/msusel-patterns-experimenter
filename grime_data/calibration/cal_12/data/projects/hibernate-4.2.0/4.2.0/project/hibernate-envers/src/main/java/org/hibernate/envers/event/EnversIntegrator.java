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
package org.hibernate.envers.event;

import org.jboss.logging.Logger;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

/**
 * Provides integration for Envers into Hibernate, which mainly means registering the proper event listeners.
 *
 * @author Steve Ebersole
 */
public class EnversIntegrator implements Integrator {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, EnversIntegrator.class.getName() );

	public static final String AUTO_REGISTER = "hibernate.listeners.envers.autoRegister";

	@Override
	public void integrate(
			Configuration configuration,
			SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry) {
		final boolean autoRegister = ConfigurationHelper.getBoolean( AUTO_REGISTER, configuration.getProperties(), true );
		if ( !autoRegister ) {
			LOG.debug( "Skipping Envers listener auto registration" );
			return;
		}

		EventListenerRegistry listenerRegistry = serviceRegistry.getService( EventListenerRegistry.class );
		listenerRegistry.addDuplicationStrategy( EnversListenerDuplicationStrategy.INSTANCE );

		final AuditConfiguration enversConfiguration = AuditConfiguration.getFor( configuration, serviceRegistry.getService( ClassLoaderService.class ) );

        if (enversConfiguration.getEntCfg().hasAuditedEntities()) {
		    listenerRegistry.appendListeners( EventType.POST_DELETE, new EnversPostDeleteEventListenerImpl( enversConfiguration ) );
		    listenerRegistry.appendListeners( EventType.POST_INSERT, new EnversPostInsertEventListenerImpl( enversConfiguration ) );
		    listenerRegistry.appendListeners( EventType.POST_UPDATE, new EnversPostUpdateEventListenerImpl( enversConfiguration ) );
		    listenerRegistry.appendListeners( EventType.POST_COLLECTION_RECREATE, new EnversPostCollectionRecreateEventListenerImpl( enversConfiguration ) );
		    listenerRegistry.appendListeners( EventType.PRE_COLLECTION_REMOVE, new EnversPreCollectionRemoveEventListenerImpl( enversConfiguration ) );
		    listenerRegistry.appendListeners( EventType.PRE_COLLECTION_UPDATE, new EnversPreCollectionUpdateEventListenerImpl( enversConfiguration ) );
        }
	}

	@Override
	public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
		// nothing to do afaik
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hibernate.integrator.spi.Integrator#integrate(org.hibernate.metamodel.source.MetadataImplementor, org.hibernate.engine.spi.SessionFactoryImplementor, org.hibernate.service.spi.SessionFactoryServiceRegistry)
	 */
	@Override
	public void integrate( MetadataImplementor metadata,
	                       SessionFactoryImplementor sessionFactory,
	                       SessionFactoryServiceRegistry serviceRegistry ) {
	    // TODO: implement
	}
}
