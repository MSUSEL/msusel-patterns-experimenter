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
import java.util.NoSuchElementException;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.engine.HibernateIterator;
import org.hibernate.event.spi.EventSource;
import org.hibernate.hql.internal.HolderInstantiator;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;

/**
 * An implementation of <tt>java.util.Iterator</tt> that is
 * returned by <tt>iterate()</tt> query execution methods.
 * @author Gavin King
 */
public final class IteratorImpl implements HibernateIterator {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, IteratorImpl.class.getName());

	private ResultSet rs;
	private final EventSource session;
	private boolean readOnly;
	private final Type[] types;
	private final boolean single;
	private Object currentResult;
	private boolean hasNext;
	private final String[][] names;
	private PreparedStatement ps;
	private HolderInstantiator holderInstantiator;

	public IteratorImpl(
	        ResultSet rs,
	        PreparedStatement ps,
	        EventSource sess,
	        boolean readOnly,
	        Type[] types,
	        String[][] columnNames,
	        HolderInstantiator holderInstantiator)
	throws HibernateException, SQLException {

		this.rs=rs;
		this.ps=ps;
		this.session = sess;
		this.readOnly = readOnly;
		this.types = types;
		this.names = columnNames;
		this.holderInstantiator = holderInstantiator;

		single = types.length==1;

		postNext();
	}

	public void close() throws JDBCException {
		if (ps!=null) {
			LOG.debug("Closing iterator");
			session.getTransactionCoordinator().getJdbcCoordinator().release( ps );
			ps = null;
			rs = null;
			hasNext = false;
			try {
				session.getPersistenceContext().getLoadContexts().cleanup( rs );
			}
			catch( Throwable ignore ) {
				// ignore this error for now
                LOG.debugf("Exception trying to cleanup load context : %s", ignore.getMessage());
			}
		}
	}

	private void postNext() throws SQLException {
		LOG.debug("Attempting to retrieve next results");
		this.hasNext = rs.next();
		if (!hasNext) {
			LOG.debug("Exhausted results");
			close();
		} else LOG.debug("Retrieved next results");
	}

	public boolean hasNext() {
		return hasNext;
	}

	public Object next() throws HibernateException {
		if ( !hasNext ) throw new NoSuchElementException("No more results");
		boolean sessionDefaultReadOnlyOrig = session.isDefaultReadOnly();
		session.setDefaultReadOnly( readOnly );
		try {
			boolean isHolder = holderInstantiator.isRequired();

			LOG.debugf( "Assembling results" );
			if ( single && !isHolder ) {
				currentResult = types[0].nullSafeGet( rs, names[0], session, null );
			}
			else {
				Object[] currentResults = new Object[types.length];
				for (int i=0; i<types.length; i++) {
					currentResults[i] = types[i].nullSafeGet( rs, names[i], session, null );
				}

				if (isHolder) {
					currentResult = holderInstantiator.instantiate(currentResults);
				}
				else {
					currentResult = currentResults;
				}
			}

			postNext();
			LOG.debugf( "Returning current results" );
			return currentResult;
		}
		catch (SQLException sqle) {
			throw session.getFactory().getSQLExceptionHelper().convert(
					sqle,
					"could not get next iterator result"
				);
		}
		finally {
			session.setDefaultReadOnly( sessionDefaultReadOnlyOrig );
		}
	}

	public void remove() {
		if (!single) {
			throw new UnsupportedOperationException("Not a single column hibernate query result set");
		}
		if (currentResult==null) {
			throw new IllegalStateException("Called Iterator.remove() before next()");
		}
		if ( !( types[0] instanceof EntityType ) ) {
			throw new UnsupportedOperationException("Not an entity");
		}

		session.delete(
				( (EntityType) types[0] ).getAssociatedEntityName(),
				currentResult,
				false,
		        null
			);
	}
}
