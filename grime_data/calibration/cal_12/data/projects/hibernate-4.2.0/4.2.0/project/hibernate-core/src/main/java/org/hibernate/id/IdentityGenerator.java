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
package org.hibernate.id;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.AssertionFailure;
import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.insert.AbstractReturningDelegate;
import org.hibernate.id.insert.AbstractSelectingDelegate;
import org.hibernate.id.insert.IdentifierGeneratingInsert;
import org.hibernate.id.insert.InsertGeneratedIdentifierDelegate;
import org.hibernate.id.insert.InsertSelectIdentityInsert;

/**
 * A generator for use with ANSI-SQL IDENTITY columns used as the primary key.
 * The IdentityGenerator for autoincrement/identity key generation.
 * <br><br>
 * Indicates to the <tt>Session</tt> that identity (ie. identity/autoincrement
 * column) key generation should be used.
 *
 * @author Christoph Sturm
 */
public class IdentityGenerator extends AbstractPostInsertGenerator {

	public InsertGeneratedIdentifierDelegate getInsertGeneratedIdentifierDelegate(
			PostInsertIdentityPersister persister,
	        Dialect dialect,
	        boolean isGetGeneratedKeysEnabled) throws HibernateException {
		if ( isGetGeneratedKeysEnabled ) {
			return new GetGeneratedKeysDelegate( persister, dialect );
		}
		else if ( dialect.supportsInsertSelectIdentity() ) {
			return new InsertSelectDelegate( persister, dialect );
		}
		else {
			return new BasicDelegate( persister, dialect );
		}
	}

	/**
	 * Delegate for dealing with IDENTITY columns using JDBC3 getGeneratedKeys
	 */
	public static class GetGeneratedKeysDelegate
			extends AbstractReturningDelegate
			implements InsertGeneratedIdentifierDelegate {
		private final PostInsertIdentityPersister persister;
		private final Dialect dialect;

		public GetGeneratedKeysDelegate(PostInsertIdentityPersister persister, Dialect dialect) {
			super( persister );
			this.persister = persister;
			this.dialect = dialect;
		}

		public IdentifierGeneratingInsert prepareIdentifierGeneratingInsert() {
			IdentifierGeneratingInsert insert = new IdentifierGeneratingInsert( dialect );
			insert.addIdentityColumn( persister.getRootTableKeyColumnNames()[0] );
			return insert;
		}

		protected PreparedStatement prepare(String insertSQL, SessionImplementor session) throws SQLException {
			return session.getTransactionCoordinator()
					.getJdbcCoordinator()
					.getStatementPreparer()
					.prepareStatement( insertSQL, PreparedStatement.RETURN_GENERATED_KEYS );
		}

		public Serializable executeAndExtract(PreparedStatement insert, SessionImplementor session) throws SQLException {
			session.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().executeUpdate( insert );
			ResultSet rs = null;
			try {
				rs = insert.getGeneratedKeys();
				return IdentifierGeneratorHelper.getGeneratedIdentity(
						rs,
						persister.getRootTableKeyColumnNames()[0],
						persister.getIdentifierType()
				);
			}
			finally {
				if ( rs != null ) {
					session.getTransactionCoordinator().getJdbcCoordinator().release( rs );
				}
			}
		}
	}

	/**
	 * Delegate for dealing with IDENTITY columns where the dialect supports returning
	 * the generated IDENTITY value directly from the insert statement.
	 */
	public static class InsertSelectDelegate
			extends AbstractReturningDelegate
			implements InsertGeneratedIdentifierDelegate {
		private final PostInsertIdentityPersister persister;
		private final Dialect dialect;

		public InsertSelectDelegate(PostInsertIdentityPersister persister, Dialect dialect) {
			super( persister );
			this.persister = persister;
			this.dialect = dialect;
		}

		public IdentifierGeneratingInsert prepareIdentifierGeneratingInsert() {
			InsertSelectIdentityInsert insert = new InsertSelectIdentityInsert( dialect );
			insert.addIdentityColumn( persister.getRootTableKeyColumnNames()[0] );
			return insert;
		}

		protected PreparedStatement prepare(String insertSQL, SessionImplementor session) throws SQLException {
			return session.getTransactionCoordinator()
					.getJdbcCoordinator()
					.getStatementPreparer()
					.prepareStatement( insertSQL, PreparedStatement.NO_GENERATED_KEYS );
		}

		public Serializable executeAndExtract(PreparedStatement insert, SessionImplementor session) throws SQLException {
			ResultSet rs = session.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().execute( insert );
			try {
				return IdentifierGeneratorHelper.getGeneratedIdentity(
						rs,
						persister.getRootTableKeyColumnNames()[0],
						persister.getIdentifierType()
				);
			}
			finally {
				session.getTransactionCoordinator().getJdbcCoordinator().release( rs );
			}
		}

		public Serializable determineGeneratedIdentifier(SessionImplementor session, Object entity) {
			throw new AssertionFailure( "insert statement returns generated value" );
		}
	}

	/**
	 * Delegate for dealing with IDENTITY columns where the dialect requires an
	 * additional command execution to retrieve the generated IDENTITY value
	 */
	public static class BasicDelegate
			extends AbstractSelectingDelegate
			implements InsertGeneratedIdentifierDelegate {
		private final PostInsertIdentityPersister persister;
		private final Dialect dialect;

		public BasicDelegate(PostInsertIdentityPersister persister, Dialect dialect) {
			super( persister );
			this.persister = persister;
			this.dialect = dialect;
		}

		public IdentifierGeneratingInsert prepareIdentifierGeneratingInsert() {
			IdentifierGeneratingInsert insert = new IdentifierGeneratingInsert( dialect );
			insert.addIdentityColumn( persister.getRootTableKeyColumnNames()[0] );
			return insert;
		}

		protected String getSelectSQL() {
			return persister.getIdentitySelectString();
		}

		protected Serializable getResult(
				SessionImplementor session,
		        ResultSet rs,
		        Object object) throws SQLException {
			return IdentifierGeneratorHelper.getGeneratedIdentity( rs, persister.getRootTableKeyColumnNames()[0], persister.getIdentifierType() );
		}
	}

}
