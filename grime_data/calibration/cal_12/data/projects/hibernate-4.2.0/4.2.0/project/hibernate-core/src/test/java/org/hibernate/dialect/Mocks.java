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
package org.hibernate.dialect;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author Steve Ebersole
 */
@SuppressWarnings( {"UnnecessaryBoxing"})
public class Mocks {

	public static Connection createConnection(String databaseName, int majorVersion) {
		return createConnection( databaseName, majorVersion, -9999 );
	}

	public static Connection createConnection(String databaseName, int majorVersion, int minorVersion) {
		DatabaseMetaDataHandler metadataHandler = new DatabaseMetaDataHandler( databaseName, majorVersion, minorVersion );
		ConnectionHandler connectionHandler = new ConnectionHandler();

		DatabaseMetaData metadataProxy = ( DatabaseMetaData ) Proxy.newProxyInstance(
				ClassLoader.getSystemClassLoader(),
				new Class[] { DatabaseMetaData.class },
				metadataHandler
		);

		Connection connectionProxy = ( Connection ) Proxy.newProxyInstance(
				ClassLoader.getSystemClassLoader(),
				new Class[] { Connection.class },
				connectionHandler
		);

		metadataHandler.setConnectionProxy( connectionProxy );
		connectionHandler.setMetadataProxy( metadataProxy );

		return connectionProxy;
	}

	private static class ConnectionHandler implements InvocationHandler {
		private DatabaseMetaData metadataProxy;

		public void setMetadataProxy(DatabaseMetaData metadataProxy) {
			this.metadataProxy = metadataProxy;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			final String methodName = method.getName();
			if ( "getMetaData".equals( methodName ) ) {
				return metadataProxy;
			}

			if ( "toString".equals( methodName ) ) {
				return "Connection proxy [@" + hashCode() + "]";
			}

			if ( "hashCode".equals( methodName ) ) {
				return Integer.valueOf( this.hashCode() );
			}

			if ( canThrowSQLException( method ) ) {
				throw new SQLException();
			}
			else {
				throw new UnsupportedOperationException();
			}
		}
	}

	private static class DatabaseMetaDataHandler implements InvocationHandler {
		private final String databaseName;
		private final int majorVersion;
		private final int minorVersion;

		private Connection connectionProxy;

		public void setConnectionProxy(Connection connectionProxy) {
			this.connectionProxy = connectionProxy;
		}

		private DatabaseMetaDataHandler(String databaseName, int majorVersion) {
			this( databaseName, majorVersion, -9999 );
		}

		private DatabaseMetaDataHandler(String databaseName, int majorVersion, int minorVersion) {
			this.databaseName = databaseName;
			this.majorVersion = majorVersion;
			this.minorVersion = minorVersion;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			final String methodName = method.getName();
			if ( "getDatabaseProductName".equals( methodName ) ) {
				return databaseName;
			}

			if ( "getDatabaseMajorVersion".equals( methodName ) ) {
				return Integer.valueOf( majorVersion );
			}

			if ( "getDatabaseMinorVersion".equals( methodName ) ) {
				return Integer.valueOf( minorVersion );
			}

			if ( "getConnection".equals( methodName ) ) {
				return connectionProxy;
			}

			if ( "toString".equals( methodName ) ) {
				return "DatabaseMetaData proxy [db-name=" + databaseName + ", version=" + majorVersion + "]";
			}

			if ( "hashCode".equals( methodName ) ) {
				return new Integer( this.hashCode() );
			}

			if ( canThrowSQLException( method ) ) {
				throw new SQLException();
			}
			else {
				throw new UnsupportedOperationException();
			}
		}
	}

	private static boolean canThrowSQLException(Method method) {
		final Class[] exceptions = method.getExceptionTypes();
		for ( Class exceptionType : exceptions ) {
			if ( SQLException.class.isAssignableFrom( exceptionType ) ) {
				return true;
			}
		}
		return false;
	}
}
