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
package org.hibernate.tuple.component;
import java.io.Serializable;
import java.lang.reflect.Method;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.tuple.Tuplizer;

/**
 * Defines further responsibilities regarding tuplization based on
 * a mapped components.
 * </p>
 * ComponentTuplizer implementations should have the following constructor signature:
 *      (org.hibernate.mapping.Component)
 * 
 * @author Gavin King
 * @author Steve Ebersole
 */
public interface ComponentTuplizer extends Tuplizer, Serializable {
	/**
	 * Retreive the current value of the parent property.
	 *
	 * @param component The component instance from which to extract the parent
	 * property value.
	 * @return The current value of the parent property.
	 */
	public Object getParent(Object component);

    /**
     * Set the value of the parent property.
     *
     * @param component The component instance on which to set the parent.
     * @param parent The parent to be set on the comonent.
     * @param factory The current session factory.
     */
	public void setParent(Object component, Object parent, SessionFactoryImplementor factory);

	/**
	 * Does the component managed by this tuuplizer contain a parent property?
	 *
	 * @return True if the component does contain a parent property; false otherwise.
	 */
	public boolean hasParentProperty();

	/**
	 * Is the given method available via the managed component as a property getter?
	 *
	 * @param method The method which to check against the managed component.
	 * @return True if the managed component is available from the managed component; else false.
	 */
	public boolean isMethodOf(Method method);
}
