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

/**
 * Loads an entity by its natural identifier
 * 
 * @author Eric Dalquist
 * @author Steve Ebersole
 *
 * @see org.hibernate.annotations.NaturalId
 * @see NaturalIdLoadAccess
 */
public interface SimpleNaturalIdLoadAccess {
	/**
	 * Specify the {@link org.hibernate.LockOptions} to use when retrieving the entity.
	 *
	 * @param lockOptions The lock options to use.
	 *
	 * @return {@code this}, for method chaining
	 */
	public SimpleNaturalIdLoadAccess with(LockOptions lockOptions);

	/**
	 * For entities with mutable natural ids, should Hibernate perform "synchronization" prior to performing 
	 * lookups?  The default is to perform "synchronization" (for correctness).
	 * <p/>
	 * See {@link NaturalIdLoadAccess#setSynchronizationEnabled} for detailed discussion.
	 *
	 * @param enabled Should synchronization be performed?  {@code true} indicates synchronization will be performed;
	 * {@code false} indicates it will be circumvented.
	 *
	 * @return {@code this}, for method chaining
	 */
	public SimpleNaturalIdLoadAccess setSynchronizationEnabled(boolean enabled);

	/**
	 * Return the persistent instance with the given natural id value, assuming that the instance exists. This method
	 * might return a proxied instance that is initialized on-demand, when a non-identifier method is accessed.
	 *
	 * You should not use this method to determine if an instance exists; to check for existence, use {@link #load}
	 * instead.  Use this only to retrieve an instance that you assume exists, where non-existence would be an
	 * actual error.
	 *
	 * @param naturalIdValue The value of the natural-id for the entity to retrieve
	 *
	 * @return The persistent instance or proxy, if an instance exists.  Otherwise, {@code null}.
	 */
	public Object getReference(Object naturalIdValue);

	/**
	 * Return the persistent instance with the given natural id value, or {@code null} if there is no such persistent
	 * instance.  If the instance is already associated with the session, return that instance, initializing it if
	 * needed.  This method never returns an uninitialized instance.
	 *
	 * @param naturalIdValue The value of the natural-id for the entity to retrieve
	 * 
	 * @return The persistent instance or {@code null}
	 */
	public Object load(Object naturalIdValue);

}
