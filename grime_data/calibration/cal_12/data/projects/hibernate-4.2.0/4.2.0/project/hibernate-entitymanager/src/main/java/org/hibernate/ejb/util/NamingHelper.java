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
package org.hibernate.ejb.util;

import javax.naming.event.NamespaceChangeListener;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;

import org.jboss.logging.Logger;

import org.hibernate.ejb.AvailableSettings;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.internal.EntityManagerMessageLogger;
import org.hibernate.service.jndi.JndiException;
import org.hibernate.service.jndi.JndiNameException;
import org.hibernate.service.jndi.internal.JndiServiceImpl;

/**
 * @author Emmanuel Bernard
 */
public class NamingHelper {
	private NamingHelper() {}

    private static final EntityManagerMessageLogger LOG = Logger.getMessageLogger(EntityManagerMessageLogger.class, NamingHelper.class.getName());

	public static void bind(Ejb3Configuration cfg) {
		String name = cfg.getHibernateConfiguration().getProperty( AvailableSettings.CONFIGURATION_JNDI_NAME );
        if ( name == null ) {
			LOG.debug( "No JNDI name configured for binding Ejb3Configuration" );
		}
		else {
            LOG.ejb3ConfigurationName( name );

			// todo : instantiating the JndiService here is temporary until HHH-6159 is resolved.
			JndiServiceImpl jndiService = new JndiServiceImpl( cfg.getProperties() );
			try {
				jndiService.bind( name, cfg );
				LOG.boundEjb3ConfigurationToJndiName( name );
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
				LOG.unableToBindEjb3ConfigurationToJndi( e );
			}
		}
	}

	private static final NamespaceChangeListener LISTENER = new NamespaceChangeListener() {
		public void objectAdded(NamingEvent evt) {
            LOG.debugf("An Ejb3Configuration was successfully bound to name: %s", evt.getNewBinding().getName());
		}

		public void objectRemoved(NamingEvent evt) {
			String name = evt.getOldBinding().getName();
            LOG.ejb3ConfigurationUnboundFromName(name);
		}

		public void objectRenamed(NamingEvent evt) {
			String name = evt.getOldBinding().getName();
            LOG.ejb3ConfigurationRenamedFromName(name);
		}

		public void namingExceptionThrown(NamingExceptionEvent evt) {
            LOG.unableToAccessEjb3Configuration(evt.getException());
		}
	};


}
