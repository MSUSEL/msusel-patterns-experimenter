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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.ScrollableResults;
import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.HolderInstantiator;
import org.hibernate.loader.Loader;
import org.hibernate.type.Type;

/**
 * Implementation of the <tt>ScrollableResults</tt> interface
 * @author Gavin King
 */
public class ScrollableResultsImpl extends AbstractScrollableResults implements ScrollableResults {

	private Object[] currentRow;

	public ScrollableResultsImpl(
	        ResultSet rs,
	        PreparedStatement ps,
	        SessionImplementor sess,
	        Loader loader,
	        QueryParameters queryParameters,
	        Type[] types, HolderInstantiator holderInstantiator) throws MappingException {
		super( rs, ps, sess, loader, queryParameters, types, holderInstantiator );
	}

	protected Object[] getCurrentRow() {
		return currentRow;
	}

	/**
	 * @see org.hibernate.ScrollableResults#scroll(int)
	 */
	public boolean scroll(int i) throws HibernateException {
		try {
			boolean result = getResultSet().relative(i);
			prepareCurrentRow(result);
			return result;
		}
		catch (SQLException sqle) {
			throw getSession().getFactory().getSQLExceptionHelper().convert(
					sqle,
					"could not advance using scroll()"
				);
		}
	}

	/**
	 * @see org.hibernate.ScrollableResults#first()
	 */
	public boolean first() throws HibernateException {
		try {
			boolean result = getResultSet().first();
			prepareCurrentRow(result);
			return result;
		}
		catch (SQLException sqle) {
			throw getSession().getFactory().getSQLExceptionHelper().convert(
					sqle,
					"could not advance using first()"
				);
		}
	}

	/**
	 * @see org.hibernate.ScrollableResults#last()
	 */
	public boolean last() throws HibernateException {
		try {
			boolean result = getResultSet().last();
			prepareCurrentRow(result);
			return result;
		}
		catch (SQLException sqle) {
			throw getSession().getFactory().getSQLExceptionHelper().convert(
					sqle,
					"could not advance using last()"
				);
		}
	}

	/**
	 * @see org.hibernate.ScrollableResults#next()
	 */
	public boolean next() throws HibernateException {
		try {
			boolean result = getResultSet().next();
			prepareCurrentRow(result);
			return result;
		}
		catch (SQLException sqle) {
			throw getSession().getFactory().getSQLExceptionHelper().convert(
					sqle,
					"could not advance using next()"
				);
		}
	}

	/**
	 * @see org.hibernate.ScrollableResults#previous()
	 */
	public boolean previous() throws HibernateException {
		try {
			boolean result = getResultSet().previous();
			prepareCurrentRow(result);
			return result;
		}
		catch (SQLException sqle) {
			throw getSession().getFactory().getSQLExceptionHelper().convert(
					sqle,
					"could not advance using previous()"
				);
		}
	}

	/**
	 * @see org.hibernate.ScrollableResults#afterLast()
	 */
	public void afterLast() throws HibernateException {
		try {
			getResultSet().afterLast();
		}
		catch (SQLException sqle) {
			throw getSession().getFactory().getSQLExceptionHelper().convert(
					sqle,
					"exception calling afterLast()"
				);
		}
	}

	/**
	 * @see org.hibernate.ScrollableResults#beforeFirst()
	 */
	public void beforeFirst() throws HibernateException {
		try {
			getResultSet().beforeFirst();
		}
		catch (SQLException sqle) {
			throw getSession().getFactory().getSQLExceptionHelper().convert(
					sqle,
					"exception calling beforeFirst()"
				);
		}
	}

	/**
	 * @see org.hibernate.ScrollableResults#isFirst()
	 */
	public boolean isFirst() throws HibernateException {
		try {
			return getResultSet().isFirst();
		}
		catch (SQLException sqle) {
			throw getSession().getFactory().getSQLExceptionHelper().convert(
					sqle,
					"exception calling isFirst()"
				);
		}
	}

	/**
	 * @see org.hibernate.ScrollableResults#isLast()
	 */
	public boolean isLast() throws HibernateException {
		try {
			return getResultSet().isLast();
		}
		catch (SQLException sqle) {
			throw getSession().getFactory().getSQLExceptionHelper().convert(
					sqle,
					"exception calling isLast()"
				);
		}
	}

	public int getRowNumber() throws HibernateException {
		try {
			return getResultSet().getRow()-1;
		}
		catch (SQLException sqle) {
			throw getSession().getFactory().getSQLExceptionHelper().convert(
					sqle,
					"exception calling getRow()"
				);
		}
	}

	public boolean setRowNumber(int rowNumber) throws HibernateException {
		if (rowNumber>=0) rowNumber++;
		try {
			boolean result = getResultSet().absolute(rowNumber);
			prepareCurrentRow(result);
			return result;
		}
		catch (SQLException sqle) {
			throw getSession().getFactory().getSQLExceptionHelper().convert(
					sqle,
					"could not advance using absolute()"
				);
		}
	}

	private void prepareCurrentRow(boolean underlyingScrollSuccessful) 
	throws HibernateException {
		
		if (!underlyingScrollSuccessful) {
			currentRow = null;
			return;
		}

		Object result = getLoader().loadSingleRow(
				getResultSet(),
				getSession(),
				getQueryParameters(),
				false
		);
		if ( result != null && result.getClass().isArray() ) {
			currentRow = (Object[]) result;
		}
		else {
			currentRow = new Object[] { result };
		}

		if ( getHolderInstantiator() != null ) {
			currentRow = new Object[] { getHolderInstantiator().instantiate(currentRow) };
		}

		afterScrollOperation();
	}

}
