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
import java.sql.SQLException;
import java.util.Properties;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.insert.AbstractReturningDelegate;
import org.hibernate.id.insert.IdentifierGeneratingInsert;
import org.hibernate.id.insert.InsertGeneratedIdentifierDelegate;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.sql.Insert;
import org.hibernate.type.Type;

/**
 * A generator which combines sequence generation with immediate retrieval
 * through JDBC3 {@link java.sql.Connection#prepareStatement(String, String[]) getGeneratedKeys}.
 * In this respect it works much like ANSI-SQL IDENTITY generation.
 * <p/>
 * This generator only known to work with newer Oracle drivers compiled for
 * JDK 1.4 (JDBC3).
 * <p/>
 * Note: Due to a bug in Oracle drivers, sql comments on these insert statements
 * are completely disabled.
 *
 * @author Steve Ebersole
 */
public class SequenceIdentityGenerator
		extends SequenceGenerator
		implements PostInsertIdentifierGenerator {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class,
			SequenceIdentityGenerator.class.getName()
	);

	@Override
    public Serializable generate(SessionImplementor s, Object obj) {
		return IdentifierGeneratorHelper.POST_INSERT_INDICATOR;
	}

	public InsertGeneratedIdentifierDelegate getInsertGeneratedIdentifierDelegate(
			PostInsertIdentityPersister persister,
	        Dialect dialect,
	        boolean isGetGeneratedKeysEnabled) throws HibernateException {
		return new Delegate( persister, dialect, getSequenceName() );
	}

	@Override
    public void configure(Type type, Properties params, Dialect dialect) throws MappingException {
		super.configure( type, params, dialect );
	}

	public static class Delegate extends AbstractReturningDelegate {
		private final Dialect dialect;
		private final String sequenceNextValFragment;
		private final String[] keyColumns;

		public Delegate(PostInsertIdentityPersister persister, Dialect dialect, String sequenceName) {
			super( persister );
			this.dialect = dialect;
			this.sequenceNextValFragment = dialect.getSelectSequenceNextValString( sequenceName );
			this.keyColumns = getPersister().getRootTableKeyColumnNames();
			if ( keyColumns.length > 1 ) {
				throw new HibernateException( "sequence-identity generator cannot be used with with multi-column keys" );
			}
		}

		public IdentifierGeneratingInsert prepareIdentifierGeneratingInsert() {
			NoCommentsInsert insert = new NoCommentsInsert( dialect );
			insert.addColumn( getPersister().getRootTableKeyColumnNames()[0], sequenceNextValFragment );
			return insert;
		}

		@Override
        protected PreparedStatement prepare(String insertSQL, SessionImplementor session) throws SQLException {
			return session.getTransactionCoordinator().getJdbcCoordinator().getStatementPreparer().prepareStatement( insertSQL, keyColumns );
		}

		@Override
		protected Serializable executeAndExtract(PreparedStatement insert, SessionImplementor session) throws SQLException {
						session.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().executeUpdate( insert );
			return IdentifierGeneratorHelper.getGeneratedIdentity(
					insert.getGeneratedKeys(),
					getPersister().getRootTableKeyColumnNames()[0],
					getPersister().getIdentifierType()
			);
		}
	}

	public static class NoCommentsInsert extends IdentifierGeneratingInsert {
		public NoCommentsInsert(Dialect dialect) {
			super( dialect );
		}

		@Override
        public Insert setComment(String comment) {
			// don't allow comments on these insert statements as comments totally
			// blow up the Oracle getGeneratedKeys "support" :(
			LOG.disallowingInsertStatementComment();
			return this;
		}
	}
}
