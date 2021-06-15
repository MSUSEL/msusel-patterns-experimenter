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
package org.hibernate.engine.transaction.synchronization.spi;

import java.io.Serializable;

import org.hibernate.engine.transaction.spi.TransactionCoordinator;

/**
 * A pluggable strategy for defining how the {@link javax.transaction.Synchronization} registered by Hibernate determines
 * whether to perform a managed flush.  An exceptions from either this delegate or the subsequent flush are routed
 * through the sister strategy {@link ExceptionMapper}.
 *
 * @author Steve Ebersole
 */
public interface ManagedFlushChecker  extends Serializable {
	/**
	 * Check whether we should perform the managed flush
	 *
	 * @param coordinator The transaction coordinator
	 * @param jtaStatus The status of the current JTA transaction.
	 *
	 * @return True to indicate to perform the managed flush; false otherwise.
	 */
	public boolean shouldDoManagedFlush(TransactionCoordinator coordinator, int jtaStatus);
}
