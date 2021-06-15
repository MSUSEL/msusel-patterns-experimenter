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
package org.hibernate;

import java.sql.Connection;

/**
 * Specialized {@link SessionBuilder} with access to stuff from another session
 *
 * @author Steve Ebersole
 */
public interface SharedSessionBuilder extends SessionBuilder {
	/**
	 * Signifies the interceptor from the original session should be used to create the new session
	 *
	 * @return {@code this}, for method chaining
	 */
	public SharedSessionBuilder interceptor();

	/**
	 * Signifies that the connection from the original session should be used to create the new session
	 *
	 * @return {@code this}, for method chaining
	 */
	public SharedSessionBuilder connection();

	/**
	 * Signifies that the connection release mode from the original session should be used to create the new session
	 *
	 * @return {@code this}, for method chaining
	 */
	public SharedSessionBuilder connectionReleaseMode();

	/**
	 * Signifies that the autoJoinTransaction flag from the original session should be used to create the new session
	 *
	 * @return {@code this}, for method chaining
	 */
	public SharedSessionBuilder autoJoinTransactions();

	/**
	 * Signifies that the autoClose flag from the original session should be used to create the new session
	 *
	 * @return {@code this}, for method chaining
	 *
	 * @deprecated For same reasons as {@link SessionBuilder#autoClose(boolean)} was deprecated.  However, shared
	 * session builders can use {@link #autoClose(boolean)} since they do not "inherit" the owner.
	 */
	@Deprecated
	public SharedSessionBuilder autoClose();

	/**
	 * Signifies that the flushBeforeCompletion flag from the original session should be used to create the new session
	 *
	 * @return {@code this}, for method chaining
	 */
	public SharedSessionBuilder flushBeforeCompletion();

	/**
	 * Signifies that the transaction context from the original session should be used to create the new session
	 *
	 * @return {@code this}, for method chaining
	 */
	public SharedSessionBuilder transactionContext();

	@Override
	SharedSessionBuilder interceptor(Interceptor interceptor);

	@Override
	SharedSessionBuilder noInterceptor();

	@Override
	SharedSessionBuilder connection(Connection connection);

	@Override
	SharedSessionBuilder connectionReleaseMode(ConnectionReleaseMode connectionReleaseMode);

	@Override
	SharedSessionBuilder autoJoinTransactions(boolean autoJoinTransactions);

	@Override
	SharedSessionBuilder autoClose(boolean autoClose);

	@Override
	SharedSessionBuilder flushBeforeCompletion(boolean flushBeforeCompletion);
}
