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
package org.hibernate.internal.util.jndi;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.hibernate.cfg.Environment;

/**
 * Helper for dealing with JNDI.
 *
 * @deprecated As JNDI access should get routed through {@link org.hibernate.service.jndi.spi.JndiService}
 */
@Deprecated
public final class JndiHelper {
	private JndiHelper() {
	}

	/**
	 * Given a hodgepodge of properties, extract out the ones relevant for JNDI interaction.
	 *
	 * @param configurationValues The map of config values
	 *
	 * @return The extracted JNDI specific properties.
	 */
	@SuppressWarnings({ "unchecked" })
	public static Properties extractJndiProperties(Map configurationValues) {
		final Properties jndiProperties = new Properties();

		for ( Map.Entry entry : (Set<Map.Entry>) configurationValues.entrySet() ) {
			if ( !String.class.isInstance( entry.getKey() ) ) {
				continue;
			}
			final String propertyName = (String) entry.getKey();
			final Object propertyValue = entry.getValue();
			if ( propertyName.startsWith( Environment.JNDI_PREFIX ) ) {
				// write the IntialContextFactory class and provider url to the result only if they are
				// non-null; this allows the environmental defaults (if any) to remain in effect
				if ( Environment.JNDI_CLASS.equals( propertyName ) ) {
					if ( propertyValue != null ) {
						jndiProperties.put( Context.INITIAL_CONTEXT_FACTORY, propertyValue );
					}
				}
				else if ( Environment.JNDI_URL.equals( propertyName ) ) {
					if ( propertyValue != null ) {
						jndiProperties.put( Context.PROVIDER_URL, propertyValue );
					}
				}
				else {
					final String passThruPropertyname = propertyName.substring( Environment.JNDI_PREFIX.length() + 1 );
					jndiProperties.put( passThruPropertyname, propertyValue );
				}
			}
		}

		return jndiProperties;
	}

	public static InitialContext getInitialContext(Properties props) throws NamingException {
		Hashtable hash = extractJndiProperties(props);
		return hash.size()==0 ?
				new InitialContext() :
				new InitialContext(hash);
	}

	/**
	 * Bind val to name in ctx, and make sure that all intermediate contexts exist.
	 *
	 * @param ctx the root context
	 * @param name the name as a string
	 * @param val the object to be bound
	 *
	 * @throws NamingException Indicates a problem performing the bind.
	 */
	public static void bind(Context ctx, String name, Object val) throws NamingException {
		try {
			ctx.rebind(name, val);
		}
		catch (Exception e) {
			Name n = ctx.getNameParser("").parse(name);
			while ( n.size() > 1 ) {
				String ctxName = n.get(0);

				Context subctx=null;
				try {
					subctx = (Context) ctx.lookup(ctxName);
				}
				catch (NameNotFoundException ignore) {
				}

				if (subctx!=null) {
					ctx = subctx;
				}
				else {
					ctx = ctx.createSubcontext(ctxName);
				}
				n = n.getSuffix(1);
			}
			ctx.rebind(n, val);
		}
	}
}

