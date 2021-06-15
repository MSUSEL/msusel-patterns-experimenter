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
package org.hibernate.engine.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jboss.logging.Logger;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.ClassLoaderHelper;

/**
 * A proxy for a ResultSet delegate, responsible for locally caching the columnName-to-columnIndex resolution that
 * has been found to be inefficient in a few vendor's drivers (i.e., Oracle and Postgres).
 *
 * @author Steve Ebersole
 * @author Gail Badner
 */
public class ResultSetWrapperProxy implements InvocationHandler {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, ResultSetWrapperProxy.class.getName());
	private static final Class[] PROXY_INTERFACES = new Class[] { ResultSet.class };
	private static final SqlExceptionHelper sqlExceptionHelper = new SqlExceptionHelper();

	private final ResultSet rs;
	private final ColumnNameCache columnNameCache;

	private ResultSetWrapperProxy(ResultSet rs, ColumnNameCache columnNameCache) {
		this.rs = rs;
		this.columnNameCache = columnNameCache;
	}

	/**
	 * Generates a proxy wrapping the ResultSet.
	 *
	 * @param resultSet The resultSet to wrap.
	 * @param columnNameCache The cache storing data for converting column names to column indexes.
	 * @return The generated proxy.
	 */
	public static ResultSet generateProxy(ResultSet resultSet, ColumnNameCache columnNameCache) {
		return ( ResultSet ) Proxy.newProxyInstance(
				getProxyClassLoader(),
				PROXY_INTERFACES,
				new ResultSetWrapperProxy( resultSet, columnNameCache )
		);
	}

	/**
	 * Determines the appropriate class loader to which the generated proxy
	 * should be scoped.
	 *
	 * @return The class loader appropriate for proxy construction.
	 */
	public static ClassLoader getProxyClassLoader() {
		ClassLoader cl = ClassLoaderHelper.getContextClassLoader();
		if ( cl == null ) {
			cl = ResultSet.class.getClassLoader();
		}
		return cl;
	}

	@Override
	@SuppressWarnings( {"UnnecessaryBoxing"})
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if ( "findColumn".equals( method.getName() ) ) {
			return Integer.valueOf( findColumn( ( String ) args[0] ) );
		}

		if ( isFirstArgColumnLabel( method, args ) ) {
			try {
				int columnIndex = findColumn( ( String ) args[0] );
				return invokeMethod(
						locateCorrespondingColumnIndexMethod( method ), buildColumnIndexMethodArgs( args, columnIndex )
				);
			}
			catch ( SQLException ex ) {
				StringBuilder buf = new StringBuilder()
						.append( "Exception getting column index for column: [" )
						.append( args[0] )
						.append( "].\nReverting to using: [" )
						.append( args[0] )
						.append( "] as first argument for method: [" )
						.append( method )
						.append( "]" );
				sqlExceptionHelper.logExceptions( ex, buf.toString() );
			}
			catch ( NoSuchMethodException ex ) {
				LOG.unableToSwitchToMethodUsingColumnIndex( method );
			}
		}
		return invokeMethod( method, args );
	}

	/**
	 * Locate the column index corresponding to the given column name via the cache.
	 *
	 * @param columnName The column name to resolve into an index.
	 * @return The column index corresponding to the given column name.
	 * @throws SQLException if the ResultSet object does not contain columnName or a database access error occurs
	 */
	private int findColumn(String columnName) throws SQLException {
		return columnNameCache.getIndexForColumnName( columnName, rs );
	}

	private boolean isFirstArgColumnLabel(Method method, Object args[]) {
		// method name should start with either get or update
		if ( ! ( method.getName().startsWith( "get" ) || method.getName().startsWith( "update" ) ) ) {
			return false;
		}

		// method should have arguments, and have same number as incoming arguments
		if ( ! ( method.getParameterTypes().length > 0 && args.length == method.getParameterTypes().length ) ) {
			return false;
		}

		// The first argument should be a String (the column name)
		//noinspection RedundantIfStatement
		if ( ! ( String.class.isInstance( args[0] ) && method.getParameterTypes()[0].equals( String.class ) ) ) {
			return false;
		}

		return true;
	}

	/**
	 * For a given {@link ResultSet} method passed a column name, locate the corresponding method passed the same
	 * parameters but the column index.
	 *
	 * @param columnNameMethod The method passed the column name
	 * @return The corresponding method passed the column index.
	 * @throws NoSuchMethodException Should never happen, but...
	 */
	private Method locateCorrespondingColumnIndexMethod(Method columnNameMethod) throws NoSuchMethodException {
		Class actualParameterTypes[] = new Class[columnNameMethod.getParameterTypes().length];
		actualParameterTypes[0] = int.class;
		System.arraycopy(
				columnNameMethod.getParameterTypes(),
				1,
				actualParameterTypes,
				1,
				columnNameMethod.getParameterTypes().length - 1
		);
		return columnNameMethod.getDeclaringClass().getMethod( columnNameMethod.getName(), actualParameterTypes );
	}

	@SuppressWarnings( {"UnnecessaryBoxing"})
	private Object[] buildColumnIndexMethodArgs(Object[] incomingArgs, int columnIndex) {
		Object actualArgs[] = new Object[incomingArgs.length];
		actualArgs[0] = Integer.valueOf( columnIndex );
		System.arraycopy( incomingArgs, 1, actualArgs, 1, incomingArgs.length - 1 );
		return actualArgs;
	}

	private Object invokeMethod(Method method, Object args[]) throws Throwable {
		try {
			return method.invoke( rs, args );
		}
		catch ( InvocationTargetException e ) {
			throw e.getTargetException();
		}
	}
}
