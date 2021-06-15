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
package org.hibernate.service.jndi.internal;

import java.util.Hashtable;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.event.EventContext;
import javax.naming.event.NamespaceChangeListener;

import org.jboss.logging.Logger;

import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.jndi.JndiHelper;
import org.hibernate.service.jndi.JndiException;
import org.hibernate.service.jndi.JndiNameException;
import org.hibernate.service.jndi.spi.JndiService;

/**
 * Standard implementation of JNDI services.
 *
 * @author Steve Ebersole
 */
public class JndiServiceImpl implements JndiService {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, JndiServiceImpl.class.getName());

	private final Hashtable initialContextSettings;

	public JndiServiceImpl(Map configurationValues) {
		this.initialContextSettings = JndiHelper.extractJndiProperties( configurationValues );
	}

	@Override
	public Object locate(String jndiName) {
		InitialContext initialContext = buildInitialContext();
		Name name = parseName( jndiName, initialContext );
		try {
			return initialContext.lookup( name );
		}
		catch ( NamingException e ) {
			throw new JndiException( "Unable to lookup JNDI name [" + jndiName + "]", e );
		}
		finally {
			cleanUp( initialContext );
		}
	}

	private InitialContext buildInitialContext() {
		try {
			return initialContextSettings.size() == 0 ? new InitialContext() : new InitialContext( initialContextSettings );
		}
		catch ( NamingException e ) {
			throw new JndiException( "Unable to open InitialContext", e );
		}
	}

	private Name parseName(String jndiName, Context context) {
		try {
			return context.getNameParser( "" ).parse( jndiName );
		}
		catch ( InvalidNameException e ) {
			throw new JndiNameException( "JNDI name [" + jndiName + "] was not valid", e );
		}
		catch ( NamingException e ) {
			throw new JndiException( "Error parsing JNDI name [" + jndiName + "]", e );
		}
	}

	private void cleanUp(InitialContext initialContext) {
		try {
			initialContext.close();
		}
		catch ( NamingException e ) {
			LOG.unableToCloseInitialContext(e.toString());
		}
	}

	@Override
	public void bind(String jndiName, Object value) {
		InitialContext initialContext = buildInitialContext();
		Name name = parseName( jndiName, initialContext );
		try {
			bind( name, value, initialContext );
		}
		finally {
			cleanUp( initialContext );
		}
	}

	private void bind(Name name, Object value, Context context) {
		try {
			LOG.tracef( "Binding : %s", name );
			context.rebind( name, value );
		}
		catch ( Exception initialException ) {
			// We had problems doing a simple bind operation.
			if ( name.size() == 1 ) {
				// if the jndi name had only 1 component there is nothing more we can do...
				throw new JndiException( "Error performing bind [" + name + "]", initialException );
			}

			// Otherwise, there is a good chance this may have been caused by missing intermediate contexts.  So we
			// attempt to create those missing intermediate contexts and bind again
			Context intermediateContextBase = context;
			while ( name.size() > 1 ) {
				final String intermediateContextName = name.get( 0 );

				Context intermediateContext = null;
				try {
					LOG.tracev( "Intermediate lookup: {0}", intermediateContextName );
					intermediateContext = (Context) intermediateContextBase.lookup( intermediateContextName );
				}
				catch ( NameNotFoundException handledBelow ) {
					// ok as we will create it below if not found
				}
				catch ( NamingException e ) {
					throw new JndiException( "Unanticipated error doing intermediate lookup", e );
				}

				if ( intermediateContext != null ) {
					LOG.tracev( "Found intermediate context: {0}", intermediateContextName );
				}
				else {
					LOG.tracev( "Creating sub-context: {0}", intermediateContextName );
					try {
						intermediateContext = intermediateContextBase.createSubcontext( intermediateContextName );
					}
					catch ( NamingException e ) {
						throw new JndiException( "Error creating intermediate context [" + intermediateContextName + "]", e );
					}
				}
				intermediateContextBase = intermediateContext;
				name = name.getSuffix( 1 );
			}
			LOG.tracev( "Binding : {0}", name );
			try {
				intermediateContextBase.rebind( name, value );
			}
			catch ( NamingException e ) {
				throw new JndiException( "Error performing intermediate bind [" + name + "]", e );
			}
		}
		LOG.debugf( "Bound name: %s", name );
	}

	@Override
	public void unbind(String jndiName) {
		InitialContext initialContext = buildInitialContext();
		Name name = parseName( jndiName, initialContext );
		try {
			initialContext.unbind( name );
		}
		catch (Exception e) {
			throw new JndiException( "Error performing unbind [" + name + "]", e );
		}
		finally {
			cleanUp( initialContext );
		}
	}

	@Override
	public void addListener(String jndiName, NamespaceChangeListener listener) {
		InitialContext initialContext = buildInitialContext();
		Name name = parseName( jndiName, initialContext );
		try {
			( (EventContext) initialContext ).addNamingListener( name, EventContext.OBJECT_SCOPE, listener );
		}
		catch (Exception e) {
			throw new JndiException( "Unable to bind listener to namespace [" + name + "]", e );
		}
		finally {
			cleanUp( initialContext );
		}
	}

}
