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
package org.hibernate.bytecode.instrumentation.spi;

import org.hibernate.engine.spi.SessionImplementor;

/**
 * Contract for field interception handlers.
 *
 * @author Steve Ebersole
 */
public interface FieldInterceptor {

	/**
	 * Use to associate the entity to which we are bound to the given session.
	 *
	 * @param session The session to which we are now associated.
	 */
	public void setSession(SessionImplementor session);

	/**
	 * Is the entity to which we are bound completely initialized?
	 *
	 * @return True if the entity is initialized; otherwise false.
	 */
	public boolean isInitialized();

	/**
	 * The the given field initialized for the entity to which we are bound?
	 *
	 * @param field The name of the field to check
	 * @return True if the given field is initialized; otherwise false.
	 */
	public boolean isInitialized(String field);

	/**
	 * Forcefully mark the entity as being dirty.
	 */
	public void dirty();

	/**
	 * Is the entity considered dirty?
	 *
	 * @return True if the entity is dirty; otherwise false.
	 */
	public boolean isDirty();

	/**
	 * Clear the internal dirty flag.
	 */
	public void clearDirty();
}
