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
package org.hibernate.dialect.lock;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.persister.entity.Lockable;

/**
 * Base {@link LockingStrategy} implementation to support implementations
 * based on issuing <tt>SQL</tt> <tt>SELECT</tt> statements
 *
 * @author Steve Ebersole
 */
public abstract class AbstractSelectLockingStrategy implements LockingStrategy {
	private final Lockable lockable;
	private final LockMode lockMode;
	private final String waitForeverSql;

	protected AbstractSelectLockingStrategy(Lockable lockable, LockMode lockMode) {
		this.lockable = lockable;
		this.lockMode = lockMode;
		this.waitForeverSql = generateLockString( LockOptions.WAIT_FOREVER );
	}

	protected Lockable getLockable() {
		return lockable;
	}

	protected LockMode getLockMode() {
		return lockMode;
	}

	protected abstract String generateLockString(int lockTimeout);

	protected String determineSql(int timeout) {
		return timeout == LockOptions.WAIT_FOREVER
				? waitForeverSql
				: timeout == LockOptions.NO_WAIT
						? getNoWaitSql()
						: generateLockString( timeout );
	}

	private String noWaitSql;

	public String getNoWaitSql() {
		if ( noWaitSql == null ) {
			noWaitSql = generateLockString( LockOptions.NO_WAIT );
		}
		return noWaitSql;
	}
}
