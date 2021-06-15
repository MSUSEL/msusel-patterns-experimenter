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
package org.hibernate.context.spi;

/**
 * A callback registered with the {@link org.hibernate.SessionFactory} that is responsible for resolving the
 * current tenant identifier for use with {@link CurrentSessionContext} and
 * {@link org.hibernate.SessionFactory#getCurrentSession()}
 *
 * @author Steve Ebersole
 */
public interface CurrentTenantIdentifierResolver {
	/**
	 * Resolve the current tenant identifier.
	 * 
	 * @return The current tenant identifier
	 */
	public String resolveCurrentTenantIdentifier();

	/**
	 * Should we validate that the tenant identifier on "current sessions" that already exist when
	 * {@link CurrentSessionContext#currentSession()} is called matches the value returned here from
	 * {@link #resolveCurrentTenantIdentifier()}?
	 * 
	 * @return {@code true} indicates that the extra validation will be performed; {@code false} indicates it will not.
	 *
	 * @see org.hibernate.context.TenantIdentifierMismatchException
	 */
	public boolean validateExistingCurrentSessions();
}
