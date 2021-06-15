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

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * Defines the contract for implementations which know how to scope the notion
 * of a {@link org.hibernate.SessionFactory#getCurrentSession() current session}.
 * <p/>
 * Implementations should adhere to the following:
 * <ul>
 * <li>contain a constructor accepting a single argument of type
 * {@link org.hibernate.engine.spi.SessionFactoryImplementor}
 * <li>should be thread safe
 * <li>should be fully serializable
 * </ul>
 * <p/>
 * Implementors should be aware that they are also fully responsible for
 * cleanup of any generated current-sessions.
 * <p/>
 * Note that there will be exactly one instance of the configured
 * CurrentSessionContext implementation per {@link org.hibernate.SessionFactory}.
 *
 * @author Steve Ebersole
 */
public interface CurrentSessionContext extends Serializable {
	/**
	 * Retrieve the current session according to the scoping defined
	 * by this implementation.
	 *
	 * @return The current session.
	 * @throws HibernateException Typically indicates an issue
	 * locating or creating the current session.
	 */
	public Session currentSession() throws HibernateException;
}
