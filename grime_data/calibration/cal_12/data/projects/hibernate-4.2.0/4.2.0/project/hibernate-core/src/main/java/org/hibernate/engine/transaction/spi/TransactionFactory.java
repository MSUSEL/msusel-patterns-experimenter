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
package org.hibernate.engine.transaction.spi;

import org.hibernate.ConnectionReleaseMode;
import org.hibernate.service.Service;

/**
 * Contract for transaction creation, as well as providing metadata and contextual information about that creation.
 *
 * @author Steve Ebersole
 */
public interface TransactionFactory<T extends TransactionImplementor> extends Service {
	/**
	 * Construct a transaction instance compatible with this strategy.
	 *
	 * @param coordinator The coordinator for this transaction
	 *
	 * @return The appropriate transaction instance.
	 *
	 * @throws org.hibernate.HibernateException Indicates a problem constructing the transaction.
	 */
	public T createTransaction(TransactionCoordinator coordinator);

	/**
	 * Can the transactions created from this strategy act as the driver?  In other words can the user actually manage
	 * transactions with this strategy?
	 *
	 * @return {@literal true} if the transaction strategy represented by this factory can act as the driver callback;
	 * {@literal false} otherwise.
	 */
	public boolean canBeDriver();

	/**
	 * Should we attempt to register JTA transaction {@link javax.transaction.Synchronization synchronizations}.
	 * <p/>
	 * In other words, is this strategy JTA-based?
	 *
	 * @return {@literal true} if the transaction strategy represented by this factory is compatible with registering
	 * {@link javax.transaction.Synchronization synchronizations}; {@literal false} otherwise.
	 */
	public boolean compatibleWithJtaSynchronization();

	/**
	 * Can the underlying transaction represented by the passed Hibernate {@link TransactionImplementor} be joined?
	 *
	 * @param transactionCoordinator The transaction coordinator
	 * @param transaction The current Hibernate transaction
	 *
	 * @return {@literal true} is the transaction can be joined; {@literal false} otherwise.
	 */
	public boolean isJoinableJtaTransaction(TransactionCoordinator transactionCoordinator, T transaction);

	/**
	 * Get the default connection release mode.
	 *
	 * @return The default release mode associated with this strategy
	 */
	public ConnectionReleaseMode getDefaultReleaseMode();

}

