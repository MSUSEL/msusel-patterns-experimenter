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
package org.hibernate.service.jta.platform.internal;

import javax.transaction.Synchronization;
import javax.transaction.TransactionSynchronizationRegistry;

import org.hibernate.engine.transaction.internal.jta.JtaStatusHelper;

/**
 * Implementation of the {@link JtaSynchronizationStrategy} contract based on using a
 * {@link TransactionSynchronizationRegistry}
 *
 * @author Steve Ebersole
 */
public class SynchronizationRegistryBasedSynchronizationStrategy implements JtaSynchronizationStrategy {
	private final SynchronizationRegistryAccess synchronizationRegistryAccess;

	public SynchronizationRegistryBasedSynchronizationStrategy(SynchronizationRegistryAccess synchronizationRegistryAccess) {
		this.synchronizationRegistryAccess = synchronizationRegistryAccess;
	}

	@Override
	public void registerSynchronization(Synchronization synchronization) {
		synchronizationRegistryAccess.getSynchronizationRegistry().registerInterposedSynchronization(
				synchronization
		);
	}

	@Override
	public boolean canRegisterSynchronization() {
		final TransactionSynchronizationRegistry registry = synchronizationRegistryAccess.getSynchronizationRegistry();
		return JtaStatusHelper.isActive( registry.getTransactionStatus() ) && ! registry.getRollbackOnly();
	}
}
