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
package org.hibernate.internal;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.event.NamespaceChangeListener;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.spi.ObjectFactory;

import org.jboss.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.service.jndi.JndiException;
import org.hibernate.service.jndi.JndiNameException;
import org.hibernate.service.jndi.spi.JndiService;

/**
 * A registry of all {@link SessionFactory} instances for the same classloader as this class.
 *
 * This registry is used for serialization/deserialization as well as JNDI binding.
 *
 * @author Steve Ebersole
 */
public class SessionFactoryRegistry {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class,
			SessionFactoryRegistry.class.getName()
	);

	public static final SessionFactoryRegistry INSTANCE = new SessionFactoryRegistry();

	private final ConcurrentHashMap<String, SessionFactory> sessionFactoryMap = new ConcurrentHashMap<String, SessionFactory>();
	private final ConcurrentHashMap<String,String> nameUuidXref = new ConcurrentHashMap<String, String>();

	public SessionFactoryRegistry() {
		LOG.debugf( "Initializing SessionFactoryRegistry : %s", this );
	}

	public void addSessionFactory(
			String uuid,
			String name,
			boolean isNameAlsoJndiName,
			SessionFactory instance,
			JndiService jndiService) {
		if ( uuid == null ) {
			throw new IllegalArgumentException( "SessionFactory UUID cannot be null" );
		}

        LOG.debugf( "Registering SessionFactory: %s (%s)", uuid, name == null ? "<unnamed>" : name );
		sessionFactoryMap.put( uuid, instance );
		if ( name != null ) {
			nameUuidXref.put( name, uuid );
		}

		if ( name == null || ! isNameAlsoJndiName ) {
			LOG.debug( "Not binding SessionFactory to JNDI, no JNDI name configured" );
			return;
		}

		LOG.debugf( "Attempting to bind SessionFactory [%s] to JNDI", name );

		try {
			jndiService.bind( name, instance );
			LOG.factoryBoundToJndiName( name );
			try {
				jndiService.addListener( name, LISTENER );
			}
			catch (Exception e) {
				LOG.couldNotBindJndiListener();
			}
		}
		catch (JndiNameException e) {
			LOG.invalidJndiName( name, e );
		}
		catch (JndiException e) {
			LOG.unableToBindFactoryToJndi( e );
		}
	}

	public void removeSessionFactory(
			String uuid,
			String name,
			boolean isNameAlsoJndiName,
			JndiService jndiService) {
		if ( name != null ) {
			nameUuidXref.remove( name );

			if ( isNameAlsoJndiName ) {
				try {
					LOG.tracef( "Unbinding SessionFactory from JNDI : %s", name );
					jndiService.unbind( name );
					LOG.factoryUnboundFromJndiName( name );
				}
				catch ( JndiNameException e ) {
					LOG.invalidJndiName( name, e );
				}
				catch ( JndiException e ) {
					LOG.unableToUnbindFactoryFromJndi( e );
				}
			}
		}

		sessionFactoryMap.remove( uuid );
	}

	public SessionFactory getNamedSessionFactory(String name) {
        LOG.debugf( "Lookup: name=%s", name );
		final String uuid = nameUuidXref.get( name );
		return getSessionFactory( uuid );
	}

	public SessionFactory getSessionFactory(String uuid) {
		LOG.debugf( "Lookup: uid=%s", uuid );
		final SessionFactory sessionFactory = sessionFactoryMap.get( uuid );
		if ( sessionFactory == null && LOG.isDebugEnabled() ) {
			LOG.debugf( "Not found: %s", uuid );
			LOG.debugf( sessionFactoryMap.toString() );
		}
		return sessionFactory;
	}

	/**
	 * Implementation of {@literal JNDI} {@link javax.naming.event.NamespaceChangeListener} contract to listener for context events
	 * and react accordingly if necessary
	 */
	private final NamespaceChangeListener LISTENER = new NamespaceChangeListener() {
		@Override
		public void objectAdded(NamingEvent evt) {
            LOG.debugf("A factory was successfully bound to name: %s", evt.getNewBinding().getName());
		}

		@Override
		public void objectRemoved(NamingEvent evt) {
			final String jndiName = evt.getOldBinding().getName();
            LOG.factoryUnboundFromName( jndiName );

			final String uuid = nameUuidXref.remove( jndiName );
			if ( uuid == null ) {
				// serious problem... but not sure what to do yet
			}
			sessionFactoryMap.remove( uuid );
		}

		@Override
		public void objectRenamed(NamingEvent evt) {
			final String oldJndiName = evt.getOldBinding().getName();
			final String newJndiName = evt.getNewBinding().getName();

            LOG.factoryJndiRename( oldJndiName, newJndiName );

			final String uuid = nameUuidXref.remove( oldJndiName );
			nameUuidXref.put( newJndiName, uuid );
		}

		@Override
		public void namingExceptionThrown(NamingExceptionEvent evt) {
			//noinspection ThrowableResultOfMethodCallIgnored
            LOG.namingExceptionAccessingFactory(evt.getException());
		}
	};

	public static class ObjectFactoryImpl implements ObjectFactory {
		@Override
		public Object getObjectInstance(Object reference, Name name, Context nameCtx, Hashtable<?, ?> environment)
				throws Exception {
			LOG.debugf( "JNDI lookup: %s", name );
			final String uuid = (String) ( (Reference) reference ).get( 0 ).getContent();
			LOG.tracef( "Resolved to UUID = %s", uuid );
			return INSTANCE.getSessionFactory( uuid );
		}
	}
}
