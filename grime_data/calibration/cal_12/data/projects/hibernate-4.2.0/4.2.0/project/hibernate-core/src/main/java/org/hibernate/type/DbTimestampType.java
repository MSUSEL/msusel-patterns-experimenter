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
package org.hibernate.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CoreMessageLogger;
import org.jboss.logging.Logger;

/**
 * <tt>dbtimestamp</tt>: An extension of {@link TimestampType} which
 * maps to the database's current timestamp, rather than the jvm's
 * current timestamp.
 * <p/>
 * Note: May/may-not cause issues on dialects which do not properly support
 * a true notion of timestamp (Oracle < 8, for example, where only its DATE
 * datatype is supported).  Depends on the frequency of DML operations...
 *
 * @author Steve Ebersole
 */
public class DbTimestampType extends TimestampType {
	public static final DbTimestampType INSTANCE = new DbTimestampType();

	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, DbTimestampType.class.getName() );

	@Override
	public String getName() {
		return "dbtimestamp";
	}

	@Override
	public String[] getRegistrationKeys() {
		return new String[] { getName() };
	}

	@Override
	public Date seed(SessionImplementor session) {
		if ( session == null ) {
			LOG.trace( "Incoming session was null; using current jvm time" );
			return super.seed( session );
		}
		else if ( !session.getFactory().getDialect().supportsCurrentTimestampSelection() ) {
			LOG.debug( "Falling back to vm-based timestamp, as dialect does not support current timestamp selection" );
			return super.seed( session );
		}
		else {
			return getCurrentTimestamp( session );
		}
	}

	private Date getCurrentTimestamp(SessionImplementor session) {
		Dialect dialect = session.getFactory().getDialect();
		String timestampSelectString = dialect.getCurrentTimestampSelectString();
        if (dialect.isCurrentTimestampSelectStringCallable()) return useCallableStatement(timestampSelectString, session);
        return usePreparedStatement(timestampSelectString, session);
	}

	private Timestamp usePreparedStatement(String timestampSelectString, SessionImplementor session) {
		PreparedStatement ps = null;
		try {
			ps = session.getTransactionCoordinator()
					.getJdbcCoordinator()
					.getStatementPreparer()
					.prepareStatement( timestampSelectString, false );
			ResultSet rs = session.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().extract( ps );
			rs.next();
			Timestamp ts = rs.getTimestamp( 1 );
			if ( LOG.isTraceEnabled() ) {
				LOG.tracev( "Current timestamp retreived from db : {0} (nanos={1}, time={2})", ts, ts.getNanos(), ts.getTime() );
			}
			return ts;
		}
		catch( SQLException e ) {
			throw session.getFactory().getSQLExceptionHelper().convert(
			        e,
			        "could not select current db timestamp",
			        timestampSelectString
			);
		}
		finally {
			if ( ps != null ) {
				session.getTransactionCoordinator().getJdbcCoordinator().release( ps );
			}
		}
	}

	private Timestamp useCallableStatement(String callString, SessionImplementor session) {
		CallableStatement cs = null;
		try {
			cs = (CallableStatement) session.getTransactionCoordinator()
					.getJdbcCoordinator()
					.getStatementPreparer()
					.prepareStatement( callString, true );
			cs.registerOutParameter( 1, java.sql.Types.TIMESTAMP );
			session.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().execute( cs );
			Timestamp ts = cs.getTimestamp( 1 );
			if ( LOG.isTraceEnabled() ) {
				LOG.tracev( "Current timestamp retreived from db : {0} (nanos={1}, time={2})", ts, ts.getNanos(), ts.getTime() );
			}
			return ts;
		}
		catch( SQLException e ) {
			throw session.getFactory().getSQLExceptionHelper().convert(
			        e,
			        "could not call current db timestamp function",
			        callString
			);
		}
		finally {
			if ( cs != null ) {
				session.getTransactionCoordinator().getJdbcCoordinator().release( cs );
			}
		}
	}
}
