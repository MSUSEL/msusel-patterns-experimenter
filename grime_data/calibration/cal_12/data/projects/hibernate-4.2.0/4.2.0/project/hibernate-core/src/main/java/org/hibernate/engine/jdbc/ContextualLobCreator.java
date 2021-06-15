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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;

/**
 * {@link LobCreator} implementation using contextual creation against the JDBC {@link java.sql.Connection} class's LOB creation
 * methods.
 *
 * @author Steve Ebersole
 * @author Gail Badner
 */
public class ContextualLobCreator extends AbstractLobCreator implements LobCreator {
	private LobCreationContext lobCreationContext;

	public ContextualLobCreator(LobCreationContext lobCreationContext) {
		this.lobCreationContext = lobCreationContext;
	}

	/**
	 * Create the basic contextual BLOB reference.
	 *
	 * @return The created BLOB reference.
	 */
	public Blob createBlob() {
		return lobCreationContext.execute( CREATE_BLOB_CALLBACK );
	}

	@Override
	public Blob createBlob(byte[] bytes) {
		try {
			Blob blob = createBlob();
			blob.setBytes( 1, bytes );
			return blob;
		}
		catch ( SQLException e ) {
			throw new JDBCException( "Unable to set BLOB bytes after creation", e );
		}
	}

	@Override
	public Blob createBlob(InputStream inputStream, long length) {
		// IMPL NOTE : it is inefficient to use JDBC LOB locator creation to create a LOB
		// backed by a given stream.  So just wrap the stream (which is what the NonContextualLobCreator does).
		return NonContextualLobCreator.INSTANCE.createBlob( inputStream, length );
	}

	/**
	 * Create the basic contextual CLOB reference.
	 *
	 * @return The created CLOB reference.
	 */
	public Clob createClob() {
		return lobCreationContext.execute( CREATE_CLOB_CALLBACK );
	}

	@Override
	public Clob createClob(String string) {
		try {
			Clob clob = createClob();
			clob.setString( 1, string );
			return clob;
		}
		catch ( SQLException e ) {
			throw new JDBCException( "Unable to set CLOB string after creation", e );
		}
	}

	@Override
	public Clob createClob(Reader reader, long length) {
		// IMPL NOTE : it is inefficient to use JDBC LOB locator creation to create a LOB
		// backed by a given stream.  So just wrap the stream (which is what the NonContextualLobCreator does).
		return NonContextualLobCreator.INSTANCE.createClob( reader, length );
	}

	/**
	 * Create the basic contextual NCLOB reference.
	 *
	 * @return The created NCLOB reference.
	 */
	public NClob createNClob() {
		return lobCreationContext.execute( CREATE_NCLOB_CALLBACK );
	}

	@Override
	public NClob createNClob(String string) {
		try {
			NClob nclob = createNClob();
			nclob.setString( 1, string );
			return nclob;
		}
		catch ( SQLException e ) {
			throw new JDBCException( "Unable to set NCLOB string after creation", e );
		}
	}

	@Override
	public NClob createNClob(Reader reader, long length) {
		// IMPL NOTE : it is inefficient to use JDBC LOB locator creation to create a LOB
		// backed by a given stream.  So just wrap the stream (which is what the NonContextualLobCreator does).
		return NonContextualLobCreator.INSTANCE.createNClob( reader, length );
	}

	public static final LobCreationContext.Callback<Blob> CREATE_BLOB_CALLBACK = new LobCreationContext.Callback<Blob>() {
		public Blob executeOnConnection(Connection connection) throws SQLException {
			return connection.createBlob();
		}
	};

	public static final LobCreationContext.Callback<Clob> CREATE_CLOB_CALLBACK = new LobCreationContext.Callback<Clob>() {
		public Clob executeOnConnection(Connection connection) throws SQLException {
			return connection.createClob();
		}
	};

	public static final LobCreationContext.Callback<NClob> CREATE_NCLOB_CALLBACK = new LobCreationContext.Callback<NClob>() {
		public NClob executeOnConnection(Connection connection) throws SQLException {
			return connection.createNClob();
		}
	};
}
