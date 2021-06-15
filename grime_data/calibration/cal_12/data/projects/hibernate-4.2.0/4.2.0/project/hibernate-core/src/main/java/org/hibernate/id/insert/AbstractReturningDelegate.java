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
package org.hibernate.id.insert;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.PostInsertIdentityPersister;
import org.hibernate.pretty.MessageHelper;

/**
 * Abstract InsertGeneratedIdentifierDelegate implementation where the
 * underlying strategy causes the enerated identitifer to be returned as an
 * effect of performing the insert statement.  Thus, there is no need for an
 * additional sql statement to determine the generated identitifer.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractReturningDelegate implements InsertGeneratedIdentifierDelegate {
	private final PostInsertIdentityPersister persister;

	public AbstractReturningDelegate(PostInsertIdentityPersister persister) {
		this.persister = persister;
	}

	public final Serializable performInsert(
			String insertSQL,
			SessionImplementor session,
			Binder binder) {
		try {
			// prepare and execute the insert
			PreparedStatement insert = prepare( insertSQL, session );
			try {
				binder.bindValues( insert );
				return executeAndExtract( insert, session );
			}
			finally {
				releaseStatement( insert, session );
			}
		}
		catch ( SQLException sqle ) {
			throw session.getFactory().getSQLExceptionHelper().convert(
			        sqle,
			        "could not insert: " + MessageHelper.infoString( persister ),
			        insertSQL
				);
		}
	}

	protected PostInsertIdentityPersister getPersister() {
		return persister;
	}

	protected abstract PreparedStatement prepare(String insertSQL, SessionImplementor session) throws SQLException;

	protected abstract Serializable executeAndExtract(PreparedStatement insert, SessionImplementor session) throws SQLException;

	protected void releaseStatement(PreparedStatement insert, SessionImplementor session) throws SQLException {
		session.getTransactionCoordinator().getJdbcCoordinator().release( insert );
	}
}
