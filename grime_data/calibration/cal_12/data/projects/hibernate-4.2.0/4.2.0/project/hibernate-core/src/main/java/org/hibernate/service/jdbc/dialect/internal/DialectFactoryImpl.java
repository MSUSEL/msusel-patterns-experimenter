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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.Dialect;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.classloading.spi.ClassLoadingException;
import org.hibernate.service.jdbc.dialect.spi.DialectFactory;
import org.hibernate.service.jdbc.dialect.spi.DialectResolver;
import org.hibernate.service.spi.InjectService;

/**
 * Standard implementation of the {@link DialectFactory} service.
 *
 * @author Steve Ebersole
 */
public class DialectFactoryImpl implements DialectFactory {
	private ClassLoaderService classLoaderService;

	@InjectService
	public void setClassLoaderService(ClassLoaderService classLoaderService) {
		this.classLoaderService = classLoaderService;
	}

	private DialectResolver dialectResolver;

	@InjectService
	public void setDialectResolver(DialectResolver dialectResolver) {
		this.dialectResolver = dialectResolver;
	}

	@Override
	public Dialect buildDialect(Map configValues, Connection connection) throws HibernateException {
		final String dialectName = (String) configValues.get( AvailableSettings.DIALECT );
		if ( dialectName != null ) {
			return constructDialect( dialectName );
		}
		else {
			return determineDialect( connection );
		}
	}

	private Dialect constructDialect(String dialectName) {
		try {
			return ( Dialect ) classLoaderService.classForName( dialectName ).newInstance();
		}
		catch ( ClassLoadingException e ) {
			throw new HibernateException( "Dialect class not found: " + dialectName, e );
		}
		catch ( HibernateException e ) {
			throw e;
		}
		catch ( Exception e ) {
			throw new HibernateException( "Could not instantiate dialect class", e );
		}
	}

	/**
	 * Determine the appropriate Dialect to use given the connection.
	 *
	 * @param connection The configured connection.
	 * @return The appropriate dialect instance.
	 *
	 * @throws HibernateException No connection given or no resolver could make
	 * the determination from the given connection.
	 */
	private Dialect determineDialect(Connection connection) {
		if ( connection == null ) {
			throw new HibernateException( "Connection cannot be null when 'hibernate.dialect' not set" );
		}

		try {
			final DatabaseMetaData databaseMetaData = connection.getMetaData();
			final Dialect dialect = dialectResolver.resolveDialect( databaseMetaData );

			if ( dialect == null ) {
				throw new HibernateException(
						"Unable to determine Dialect to use [name=" + databaseMetaData.getDatabaseProductName() +
								", majorVersion=" + databaseMetaData.getDatabaseMajorVersion() +
								"]; user must register resolver or explicitly set 'hibernate.dialect'"
				);
			}

			return dialect;
		}
		catch ( SQLException sqlException ) {
			throw new HibernateException(
					"Unable to access java.sql.DatabaseMetaData to determine appropriate Dialect to use",
					sqlException
			);
		}
	}
}
