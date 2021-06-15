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

import javax.transaction.Synchronization;

import org.hibernate.engine.transaction.spi.LocalStatus;

/**
 * Defines the contract for abstracting applications from the configured underlying means of transaction management.
 * Allows the application to define units of work, while maintaining abstraction from the underlying transaction
 * implementation (eg. JTA, JDBC).
 * <p/>
 * A transaction is associated with a {@link Session} and is usually initiated by a call to
 * {@link org.hibernate.Session#beginTransaction()}.  A single session might span multiple transactions since
 * the notion of a session (a conversation between the application and the datastore) is of coarser granularity than
 * the notion of a transaction.  However, it is intended that there be at most one uncommitted transaction associated
 * with a particular {@link Session} at any time.
 * <p/>
 * Implementers are not intended to be thread-safe.
 *
 * @author Anton van Straaten
 * @author Steve Ebersole
 */
public interface Transaction {
	/**
	 * Is this transaction the initiator of any underlying transaction?
	 *
	 * @return {@code true} if this transaction initiated the underlying transaction; {@code false} otherwise.
	 */
	public boolean isInitiator();

	/**
	 * Begin this transaction.  No-op if the transaction has already been begun.  Note that this is not necessarily
	 * symmetrical since usually multiple calls to {@link #commit} or {@link #rollback} will error.
	 *
	 * @throws HibernateException Indicates a problem beginning the transaction.
	 */
	public void begin();

	/**
	 * Commit this transaction.  This might entail a number of things depending on the context:<ul>
	 *     <li>
	 *         If this transaction is the {@link #isInitiator initiator}, {@link Session#flush} the {@link Session}
	 *         with which it is associated (unless {@link Session} is in {@link FlushMode#MANUAL}).
	 *     </li>
	 *     <li>
	 *         If this transaction is the {@link #isInitiator initiator}, commit the underlying transaction.
	 *     </li>
	 *     <li>
	 *         Coordinate various callbacks
	 *     </li>
	 * </ul>
	 *
	 * @throws HibernateException Indicates a problem committing the transaction.
	 */
	public void commit();

	/**
	 * Rollback this transaction.  Either rolls back the underlying transaction or ensures it cannot later commit
	 * (depending on the actual underlying strategy).
	 *
	 * @throws HibernateException Indicates a problem rolling back the transaction.
	 */
	public void rollback();

	/**
	 * Get the current local status of this transaction.
	 * <p/>
	 * This only accounts for the local view of the transaction status.  In other words it does not check the status
	 * of the actual underlying transaction.
	 *
	 * @return The current local status.
	 */
	public LocalStatus getLocalStatus();

	/**
	 * Is this transaction still active?
	 * <p/>
	 * Answers on a best effort basis.  For example, in the case of JDBC based transactions we cannot know that a
	 * transaction is active when it is initiated directly through the JDBC {@link java.sql.Connection}, only when
	 * it is initiated from here.
	 *
	 * @return {@code true} if the transaction is still active; {@code false} otherwise.
	 *
	 * @throws HibernateException Indicates a problem checking the transaction status.
	 */
	public boolean isActive();

	/**
	 * Is Hibernate participating in the underlying transaction?
	 * <p/>
	 * Generally speaking this will be the same as {@link #isActive()}.
	 * 
	 * @return {@code true} if Hibernate is known to be participating in the underlying transaction; {@code false}
	 * otherwise.
	 */
	public boolean isParticipating();

	/**
	 * Was this transaction committed?
	 * <p/>
	 * Answers on a best effort basis.  For example, in the case of JDBC based transactions we cannot know that a
	 * transaction was committed when the commit was performed directly through the JDBC {@link java.sql.Connection},
	 * only when the commit was done from this.
	 *
	 * @return {@code true} if the transaction is rolled back; {@code false} otherwise.
	 *
	 * @throws HibernateException Indicates a problem checking the transaction status.
	 */
	@SuppressWarnings( {"UnusedDeclaration"})
	public boolean wasCommitted();

	/**
	 * Was this transaction rolled back or set to rollback only?
	 * <p/>
	 * Answers on a best effort basis.  For example, in the case of JDBC based transactions we cannot know that a
	 * transaction was rolled back when rollback was performed directly through the JDBC {@link java.sql.Connection},
	 * only when it was rolled back  from here.
	 *
	 * @return {@literal true} if the transaction is rolled back; {@literal false} otherwise.
	 *
	 * @throws HibernateException Indicates a problem checking the transaction status.
	 */
	@SuppressWarnings( {"UnusedDeclaration"})
	public boolean wasRolledBack();

	/**
	 * Register a user synchronization callback for this transaction.
	 *
	 * @param synchronization The Synchronization callback to register.
	 *
	 * @throws HibernateException Indicates a problem registering the synchronization.
	 */
	public void registerSynchronization(Synchronization synchronization) throws HibernateException;

	/**
	 * Set the transaction timeout for any transaction started by a subsequent call to {@link #begin} on this instance.
	 *
	 * @param seconds The number of seconds before a timeout.
	 */
	public void setTimeout(int seconds);

	/**
	 * Retrieve the transaction timeout set for this transaction.  A negative indicates no timeout has been set.
	 *
	 * @return The timeout, in seconds.
	 */
	public int getTimeout();
}
