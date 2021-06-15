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
package org.hibernate.service;

/**
 * A registry of {@link Service services}.
 *
 * @author Steve Ebersole
 */
public interface ServiceRegistry {
	/**
	 * Retrieve this registry's parent registry.
	 * 
	 * @return The parent registry.  May be null.
	 */
	public ServiceRegistry getParentServiceRegistry();

	/**
	 * Retrieve a service by role.  If service is not found, but a {@link org.hibernate.service.spi.BasicServiceInitiator} is registered for
	 * this service role, the service will be initialized and returned.
	 * <p/>
	 * NOTE: We cannot return {@code <R extends Service<T>>} here because the service might come from the parent...
	 * 
	 * @param serviceRole The service role
	 * @param <R> The service role type
	 *
	 * @return The requested service.
	 *
	 * @throws UnknownServiceException Indicates the service was not known.
	 */
	public <R extends Service> R getService(Class<R> serviceRole);
}
