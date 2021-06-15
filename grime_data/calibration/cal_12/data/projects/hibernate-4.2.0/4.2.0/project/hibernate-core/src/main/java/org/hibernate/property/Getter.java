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
package org.hibernate.property;
import java.io.Serializable;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * Gets values of a particular property
 *
 * @author Gavin King
 */
public interface Getter extends Serializable {
	/**
	 * Get the property value from the given instance .
	 * @param owner The instance containing the value to be retreived.
	 * @return The extracted value.
	 * @throws HibernateException
	 */
	public Object get(Object owner) throws HibernateException;

	/**
	 * Get the property value from the given owner instance.
	 *
	 * @param owner The instance containing the value to be retreived.
	 * @param mergeMap a map of merged persistent instances to detached instances
	 * @param session The session from which this request originated.
	 * @return The extracted value.
	 * @throws HibernateException
	 */
	public Object getForInsert(Object owner, Map mergeMap, SessionImplementor session) 
	throws HibernateException;

	/**
	 * Retrieve the member to which this property maps.  This might be the
	 * field or it might be the getter method.
	 *
	 * @return The mapped member.
	 */
	public Member getMember();

	/**
	 * Retrieve the declared Java type
	 *
	 * @return The declared java type.
	 */
	public Class getReturnType();

	/**
	 * Retrieve the getter-method name.
	 * <p/>
	 * Optional operation (return null)
	 *
	 * @return The name of the getter method, or null.
	 */
	public String getMethodName();

	/**
	 * Retrieve the getter-method.
	 * <p/>
	 * Optional operation (return null)
	 *
	 * @return The getter method, or null.
	 */
	public Method getMethod();
}
