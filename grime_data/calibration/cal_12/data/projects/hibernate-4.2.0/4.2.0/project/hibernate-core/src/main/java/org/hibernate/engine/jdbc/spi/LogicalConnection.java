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
package org.hibernate.engine.jdbc.spi;

import java.io.Serializable;
import java.sql.Connection;

/**
 * LogicalConnection contract
 *
 * @author Steve Ebersole
 */
public interface LogicalConnection extends Serializable {
	/**
	 * Is this logical connection open?  Another phraseology sometimes used is: "are we
	 * logically connected"?
	 *
	 * @return True if logically connected; false otherwise.
	 */
	public boolean isOpen();

	/**
	 * Is this logical connection instance "physically" connected.  Meaning
	 * do we currently internally have a cached connection.
	 *
	 * @return True if physically connected; false otherwise.
	 */
	public boolean isPhysicallyConnected();

	/**
	 * Retrieves the connection currently "logically" managed by this LogicalConnectionImpl.
	 * <p/>
	 * Note, that we may need to obtain a connection to return here if a
	 * connection has either not yet been obtained (non-UserSuppliedConnectionProvider)
	 * or has previously been aggressively released.
	 *
	 * @return The current Connection.
	 */
	public Connection getConnection();

	/**
	 * Release the underlying connection and clean up any other resources associated
	 * with this logical connection.
	 * <p/>
	 * This leaves the logical connection in a "no longer usable" state.
	 *
	 * @return The application-supplied connection, or {@code null} if Hibernate was managing connection.
	 */
	public Connection close();
}
