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
package org.hibernate.service.jndi.spi;

import javax.naming.event.NamespaceChangeListener;

import org.hibernate.service.Service;

/**
 * Service providing simplified access to {@literal JNDI} related features needed by Hibernate.
 *
 * @author Steve Ebersole
 */
public interface JndiService extends Service {
	/**
	 * Locate an object in {@literal JNDI} by name
	 *
	 * @param jndiName The {@literal JNDI} name of the object to locate
	 *
	 * @return The object found (may be null).
	 */
	public Object locate(String jndiName);

	/**
	 * Binds a value into {@literal JNDI} by name.
	 *
	 * @param jndiName The name under which to bind the object
	 * @param value The value to bind
	 */
	public void bind(String jndiName, Object value);

	/**
	 * Unbind a value from {@literal JNDI} by name.
	 *
	 * @param jndiName The name under which the object is bound
	 */
	public void unbind(String jndiName);

	/**
	 * Adds the specified listener to the given {@literal JNDI} namespace.
	 *
	 * @param jndiName The {@literal JNDI} namespace
	 * @param listener The listener
	 */
	public void addListener(String jndiName, NamespaceChangeListener listener);
}
