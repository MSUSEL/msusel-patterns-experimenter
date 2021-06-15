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
package org.hibernate.service.jdbc.dialect.internal;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.logging.Logger;

import org.hibernate.dialect.Dialect;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.service.jdbc.dialect.spi.DialectResolver;

/**
 * A {@link DialectResolver} implementation which coordinates resolution by delegating to sub-resolvers.
 *
 * @author Tomoto Shimizu Washio
 * @author Steve Ebersole
 */
public class DialectResolverSet implements DialectResolver {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, DialectResolverSet.class.getName());

	private List<DialectResolver> resolvers;

	public DialectResolverSet() {
		this( new ArrayList<DialectResolver>() );
	}

	public DialectResolverSet(List<DialectResolver> resolvers) {
		this.resolvers = resolvers;
	}

	public DialectResolverSet(DialectResolver... resolvers) {
		this( Arrays.asList( resolvers ) );
	}

	public Dialect resolveDialect(DatabaseMetaData metaData) throws JDBCConnectionException {
		for ( DialectResolver resolver : resolvers ) {
			try {
				Dialect dialect = resolver.resolveDialect( metaData );
				if ( dialect != null ) {
					return dialect;
				}
			}
			catch ( JDBCConnectionException e ) {
				throw e;
			}
			catch ( Exception e ) {
                LOG.exceptionInSubResolver(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Add a resolver at the end of the underlying resolver list.  The resolver added by this method is at lower
	 * priority than any other existing resolvers.
	 *
	 * @param resolver The resolver to add.
	 */
	public void addResolver(DialectResolver resolver) {
		resolvers.add( resolver );
	}

	/**
	 * Add a resolver at the beginning of the underlying resolver list.  The resolver added by this method is at higher
	 * priority than any other existing resolvers.
	 *
	 * @param resolver The resolver to add.
	 */
	public void addResolverAtFirst(DialectResolver resolver) {
		resolvers.add( 0, resolver );
	}
}
