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
import java.lang.reflect.Method;
import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.Property;
import org.hibernate.property.Getter;
import org.hibernate.property.Setter;
import org.hibernate.tuple.Instantiator;

/**
 * Support for tuplizers relating to components.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public abstract class AbstractComponentTuplizer implements ComponentTuplizer {
	protected final Getter[] getters;
	protected final Setter[] setters;
	protected final int propertySpan;
	protected final Instantiator instantiator;
	protected final boolean hasCustomAccessors;

	protected abstract Instantiator buildInstantiator(Component component);
	protected abstract Getter buildGetter(Component component, Property prop);
	protected abstract Setter buildSetter(Component component, Property prop);

	protected AbstractComponentTuplizer(Component component) {
		propertySpan = component.getPropertySpan();
		getters = new Getter[propertySpan];
		setters = new Setter[propertySpan];

		Iterator iter = component.getPropertyIterator();
		boolean foundCustomAccessor=false;
		int i = 0;
		while ( iter.hasNext() ) {
			Property prop = ( Property ) iter.next();
			getters[i] = buildGetter( component, prop );
			setters[i] = buildSetter( component, prop );
			if ( !prop.isBasicPropertyAccessor() ) {
				foundCustomAccessor = true;
			}
			i++;
		}
		hasCustomAccessors = foundCustomAccessor;
		instantiator = buildInstantiator( component );
	}

	public Object getPropertyValue(Object component, int i) throws HibernateException {
		return getters[i].get( component );
	}

	public Object[] getPropertyValues(Object component) throws HibernateException {
		Object[] values = new Object[propertySpan];
		for ( int i = 0; i < propertySpan; i++ ) {
			values[i] = getPropertyValue( component, i );
		}
		return values;
	}

	public boolean isInstance(Object object) {
		return instantiator.isInstance(object);
	}

	public void setPropertyValues(Object component, Object[] values) throws HibernateException {
		for ( int i = 0; i < propertySpan; i++ ) {
			setters[i].set( component, values[i], null );
		}
	}

	/**
	* This method does not populate the component parent
	*/
	public Object instantiate() throws HibernateException {
		return instantiator.instantiate();
	}

	public Object getParent(Object component) {
		return null;
	}

	public boolean hasParentProperty() {
		return false;
	}

	public boolean isMethodOf(Method method) {
		return false;
	}

	public void setParent(Object component, Object parent, SessionFactoryImplementor factory) {
		throw new UnsupportedOperationException();
	}

	public Getter getGetter(int i) {
		return getters[i];
	}
}
