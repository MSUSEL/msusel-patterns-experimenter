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
package org.hibernate.proxy;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.CompositeType;

/**
 * Contract for run-time, proxy-based lazy initialization proxies.
 *
 * @author Gavin King
 */
public interface ProxyFactory {

	/**
	 * Called immediately after instantiation of this factory.
	 * <p/>
	 * Essentially equivalent to constructor injection, but contracted
	 * here via interface.
	 *
	 * @param entityName The name of the entity for which this factory should
	 * generate proxies.
	 * @param persistentClass The entity class for which to generate proxies;
	 * not always the same as the entityName.
	 * @param interfaces The interfaces to expose in the generated proxy;
	 * {@link HibernateProxy} is already included in this collection.
	 * @param getIdentifierMethod Reference to the identifier getter method;
	 * invocation on this method should not force initialization
	 * @param setIdentifierMethod Reference to the identifier setter method;
	 * invocation on this method should not force initialization
	 * @param componentIdType For composite identifier types, a reference to
	 * the {@link org.hibernate.type.ComponentType type} of the identifier
	 * property; again accessing the id should generally not cause
	 * initialization - but need to bear in mind <key-many-to-one/>
	 * mappings.
	 * @throws HibernateException Indicates a problem completing post
	 * instantiation initialization.
	 */
	public void postInstantiate(
			String entityName,
			Class persistentClass,
			Set interfaces,
			Method getIdentifierMethod,
			Method setIdentifierMethod,
			CompositeType componentIdType) throws HibernateException;

	/**
	 * Create a new proxy instance
	 *
	 * @param id The id value for the proxy to be generated.
	 * @param session The session to which the generated proxy will be
	 * associated.
	 * @return The generated proxy.
	 * @throws HibernateException Indicates problems generating the requested
	 * proxy.
	 */
	public HibernateProxy getProxy(Serializable id,SessionImplementor session) throws HibernateException;

}
