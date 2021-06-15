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
package org.hibernate.service.spi;

/**
 * Optional contract for services that wrap stuff that to which it is useful to have access.  For example, a service
 * that maintains a {@link javax.sql.DataSource} might want to expose access to the {@link javax.sql.DataSource} or
 * its {@link java.sql.Connection} instances.
 *
 * @author Steve Ebersole
 */
public interface Wrapped {
	/**
	 * Can this wrapped service be unwrapped as the indicated type?
	 *
	 * @param unwrapType The type to check.
	 *
	 * @return True/false.
	 */
	public boolean isUnwrappableAs(Class unwrapType);

	/**
	 * Unproxy the service proxy
	 *
	 * @param unwrapType The java type as which to unwrap this instance.
	 *
	 * @return The unwrapped reference
	 *
	 * @throws UnknownUnwrapTypeException if the servicebe unwrapped as the indicated type
	 */
	public <T> T unwrap(Class<T> unwrapType);
}
