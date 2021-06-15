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
package org.hibernate.type;

import java.lang.reflect.Method;

import org.hibernate.EntityMode;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.CascadeStyle;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * Contract for value types to hold collections and have cascades, etc.  The notion is that of composition.  JPA terms
 * this an embeddable.
 *
 * @author Steve Ebersole
 */
public interface CompositeType extends Type {
	/**
	 * Get the types of the component properties
	 *
	 * @return The component property types.
	 */
	public Type[] getSubtypes();

	/**
	 * Get the names of the component properties
	 *
	 * @return The component property names
	 */
	public String[] getPropertyNames();

	/**
	 * Retrieve the indicators regarding which component properties are nullable.
	 * <p/>
	 * An optional operation
	 *
	 * @return nullability of component properties
	 */
	public boolean[] getPropertyNullability();

	/**
	 * Extract the values of the component properties from the given component instance
	 *
	 * @param component The component instance
	 * @param session The session from which the request originates
	 *
	 * @return The property values
	 *
	 * @throws HibernateException Indicates a problem access the property values.
	 */
	public Object[] getPropertyValues(Object component, SessionImplementor session) throws HibernateException;

	/**
	 * Extract the values of the component properties from the given component instance without access to the
	 * session.
	 * <p/>
	 * An optional operation
	 *
	 * @param component The component instance
	 * @param entityMode The entity mode
	 *
	 * @return The property values
	 *
	 * @throws HibernateException Indicates a problem access the property values.
	 */
	public Object[] getPropertyValues(Object component, EntityMode entityMode) throws HibernateException;

	/**
	 * Extract a particular component property value indicated by index.
	 *
	 * @param component The component instance
	 * @param index The index of the property whose value is to be extracted
	 * @param session The session from which the request originates.
	 *
	 * @return The extracted component property value
	 *
	 * @throws HibernateException Indicates a problem access the property value.
	 */
	public Object getPropertyValue(Object component, int index, SessionImplementor session) throws HibernateException;

	/**
	 * Inject property values onto the given component instance
	 * <p/>
	 * An optional operation
	 *
	 * @param component The component instance
	 * @param values The values to inject
	 * @param entityMode The entity mode
	 *
	 * @throws HibernateException Indicates an issue performing the injection
	 */
	public void setPropertyValues(Object component, Object[] values, EntityMode entityMode) throws HibernateException;

	/**
	 * Retrieve the cascade style of the indicated component property.
	 *
	 * @param index The property index,
	 *
	 * @return The cascade style.
	 */
	public CascadeStyle getCascadeStyle(int index);

	/**
	 * Retrieve the fetch mode of the indicated component property.
	 *
	 * @param index The property index,
	 *
	 * @return The fetch mode
	 */
	public FetchMode getFetchMode(int index);

	/**
	 * Is the given method a member of this component's class?
	 *
	 * @param method The method to check
	 *
	 * @return True if the method is a member; false otherwise.
	 */
	public boolean isMethodOf(Method method);

	/**
	 * Is this component embedded?  "embedded" indicates that the component is "virtual", that its properties are
	 * "flattened" onto its owner
	 *
	 * @return True if this component is embedded; false otherwise.
	 */
	public boolean isEmbedded();
}
