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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.ScrollableResults;
import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.HolderInstantiator;
import org.hibernate.loader.Loader;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

/**
 * Implementation of the <tt>ScrollableResults</tt> interface
 *
 * @author Steve Ebersole
 */
public abstract class AbstractScrollableResults implements ScrollableResults {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class,
                                                                       AbstractScrollableResults.class.getName());

	private final ResultSet resultSet;
	private final PreparedStatement ps;
	private final SessionImplementor session;
	private final Loader loader;
	private final QueryParameters queryParameters;
	private final Type[] types;
	private HolderInstantiator holderInstantiator;

	public AbstractScrollableResults(
	        ResultSet rs,
	        PreparedStatement ps,
	        SessionImplementor sess,
			Loader loader,
			QueryParameters queryParameters,
	        Type[] types,
	        HolderInstantiator holderInstantiator) throws MappingException {
		this.resultSet=rs;
		this.ps=ps;
		this.session = sess;
		this.loader = loader;
		this.queryParameters = queryParameters;
		this.types = types;
		this.holderInstantiator = holderInstantiator!=null && holderInstantiator.isRequired()
		        ? holderInstantiator
		        : null;
	}

	protected abstract Object[] getCurrentRow();

	protected ResultSet getResultSet() {
		return resultSet;
	}

	protected PreparedStatement getPs() {
		return ps;
	}

	protected SessionImplementor getSession() {
		return session;
	}

	protected Loader getLoader() {
		return loader;
	}

	protected QueryParameters getQueryParameters() {
		return queryParameters;
	}

	protected Type[] getTypes() {
		return types;
	}

	protected HolderInstantiator getHolderInstantiator() {
		return holderInstantiator;
	}

	public final void close() throws HibernateException {
		// not absolutely necessary, but does help with aggressive release
		//session.getJDBCContext().getConnectionManager().closeQueryStatement( ps, resultSet );
		session.getTransactionCoordinator().getJdbcCoordinator().release( ps );
		try {
			session.getPersistenceContext().getLoadContexts().cleanup( resultSet );
		}
		catch( Throwable ignore ) {
			// ignore this error for now
			if ( LOG.isTraceEnabled() ) {
				LOG.tracev( "Exception trying to cleanup load context : {0}", ignore.getMessage() );
			}
		}
	}

	public final Object[] get() throws HibernateException {
		return getCurrentRow();
	}

	public final Object get(int col) throws HibernateException {
		return getCurrentRow()[col];
	}

	/**
	 * Check that the requested type is compatible with the result type, and
	 * return the column value.  This version makes sure the the classes
	 * are identical.
	 *
	 * @param col the column
	 * @param returnType a "final" type
	 */
	protected final Object getFinal(int col, Type returnType) throws HibernateException {
		if ( holderInstantiator!=null ) {
			throw new HibernateException("query specifies a holder class");
		}

		if ( returnType.getReturnedClass()==types[col].getReturnedClass() ) {
			return get(col);
		}
		else {
			return throwInvalidColumnTypeException(col, types[col], returnType);
		}
	}

	/**
	 * Check that the requested type is compatible with the result type, and
	 * return the column value.  This version makes sure the the classes
	 * are "assignable".
	 *
	 * @param col the column
	 * @param returnType any type
	 */
	protected final Object getNonFinal(int col, Type returnType) throws HibernateException {
		if ( holderInstantiator!=null ) {
			throw new HibernateException("query specifies a holder class");
		}

		if ( returnType.getReturnedClass().isAssignableFrom( types[col].getReturnedClass() ) ) {
			return get(col);
		}
		else {
			return throwInvalidColumnTypeException(col, types[col], returnType);
		}
	}

	public final BigDecimal getBigDecimal(int col) throws HibernateException {
		return (BigDecimal) getFinal(col, StandardBasicTypes.BIG_DECIMAL);
	}

	public final BigInteger getBigInteger(int col) throws HibernateException {
		return (BigInteger) getFinal(col, StandardBasicTypes.BIG_INTEGER);
	}

	public final byte[] getBinary(int col) throws HibernateException {
		return (byte[]) getFinal(col, StandardBasicTypes.BINARY);
	}

	public final String getText(int col) throws HibernateException {
		return (String) getFinal(col, StandardBasicTypes.TEXT);
	}

	public final Blob getBlob(int col) throws HibernateException {
		return (Blob) getNonFinal(col, StandardBasicTypes.BLOB);
	}

	public final Clob getClob(int col) throws HibernateException {
		return (Clob) getNonFinal(col, StandardBasicTypes.CLOB);
	}

	public final Boolean getBoolean(int col) throws HibernateException {
		return (Boolean) getFinal(col, StandardBasicTypes.BOOLEAN);
	}

	public final Byte getByte(int col) throws HibernateException {
		return (Byte) getFinal(col, StandardBasicTypes.BYTE);
	}

	public final Character getCharacter(int col) throws HibernateException {
		return (Character) getFinal(col, StandardBasicTypes.CHARACTER);
	}

	public final Date getDate(int col) throws HibernateException {
		return (Date) getNonFinal(col, StandardBasicTypes.TIMESTAMP);
	}

	public final Calendar getCalendar(int col) throws HibernateException {
		return (Calendar) getNonFinal(col, StandardBasicTypes.CALENDAR);
	}

	public final Double getDouble(int col) throws HibernateException {
		return (Double) getFinal(col, StandardBasicTypes.DOUBLE);
	}

	public final Float getFloat(int col) throws HibernateException {
		return (Float) getFinal(col, StandardBasicTypes.FLOAT);
	}

	public final Integer getInteger(int col) throws HibernateException {
		return (Integer) getFinal(col, StandardBasicTypes.INTEGER);
	}

	public final Long getLong(int col) throws HibernateException {
		return (Long) getFinal(col, StandardBasicTypes.LONG);
	}

	public final Short getShort(int col) throws HibernateException {
		return (Short) getFinal(col, StandardBasicTypes.SHORT);
	}

	public final String getString(int col) throws HibernateException {
		return (String) getFinal(col, StandardBasicTypes.STRING);
	}

	public final Locale getLocale(int col) throws HibernateException {
		return (Locale) getFinal(col, StandardBasicTypes.LOCALE);
	}

	/*public final Currency getCurrency(int col) throws HibernateException {
		return (Currency) get(col);
	}*/

	public final TimeZone getTimeZone(int col) throws HibernateException {
		return (TimeZone) getNonFinal(col, StandardBasicTypes.TIMEZONE);
	}

	public final Type getType(int i) {
		return types[i];
	}

	private Object throwInvalidColumnTypeException(
	        int i,
	        Type type,
	        Type returnType) throws HibernateException {
		throw new HibernateException(
				"incompatible column types: " +
				type.getName() +
				", " +
				returnType.getName()
		);
	}

	protected void afterScrollOperation() {
		session.afterScrollOperation();
	}
}
