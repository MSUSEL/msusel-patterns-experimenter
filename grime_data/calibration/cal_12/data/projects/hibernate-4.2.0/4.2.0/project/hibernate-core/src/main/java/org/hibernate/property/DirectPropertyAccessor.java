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
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.PropertyAccessException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.util.ReflectHelper;

/**
 * Accesses fields directly.
 * @author Gavin King
 */
public class DirectPropertyAccessor implements PropertyAccessor {

	public static final class DirectGetter implements Getter {
		private final transient Field field;
		private final Class clazz;
		private final String name;

		DirectGetter(Field field, Class clazz, String name) {
			this.field = field;
			this.clazz = clazz;
			this.name = name;
		}

		/**
		 * {@inheritDoc}
		 */
		public Object get(Object target) throws HibernateException {
			try {
				return field.get(target);
			}
			catch (Exception e) {
				throw new PropertyAccessException(e, "could not get a field value by reflection", false, clazz, name);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public Object getForInsert(Object target, Map mergeMap, SessionImplementor session) {
			return get( target );
		}

		/**
		 * {@inheritDoc}
		 */
		public Member getMember() {
			return field;
		}

		/**
		 * {@inheritDoc}
		 */
		public Method getMethod() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public String getMethodName() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public Class getReturnType() {
			return field.getType();
		}

		Object readResolve() {
			return new DirectGetter( getField(clazz, name), clazz, name );
		}
		
		public String toString() {
			return "DirectGetter(" + clazz.getName() + '.' + name + ')';
		}
	}

	public static final class DirectSetter implements Setter {
		private final transient Field field;
		private final Class clazz;
		private final String name;
		DirectSetter(Field field, Class clazz, String name) {
			this.field = field;
			this.clazz = clazz;
			this.name = name;
		}

		/**
		 * {@inheritDoc}
		 */
		public Method getMethod() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public String getMethodName() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public void set(Object target, Object value, SessionFactoryImplementor factory) throws HibernateException {
			try {
				field.set(target, value);
			}
			catch (Exception e) {
				if(value == null && field.getType().isPrimitive()) {
					throw new PropertyAccessException(
							e, 
							"Null value was assigned to a property of primitive type", 
							true, 
							clazz, 
							name
						);					
				} else {
					throw new PropertyAccessException(e, "could not set a field value by reflection", true, clazz, name);
				}
			}
		}

		public String toString() {
			return "DirectSetter(" + clazz.getName() + '.' + name + ')';
		}
		
		Object readResolve() {
			return new DirectSetter( getField(clazz, name), clazz, name );
		}
	}

	private static Field getField(Class clazz, String name) throws PropertyNotFoundException {
		if ( clazz==null || clazz==Object.class ) {
			throw new PropertyNotFoundException("field not found: " + name); 
		}
		Field field;
		try {
			field = clazz.getDeclaredField(name);
		}
		catch (NoSuchFieldException nsfe) {
			field = getField( clazz, clazz.getSuperclass(), name );
		}
		if ( !ReflectHelper.isPublic(clazz, field) ) field.setAccessible(true);
		return field;
	}

	private static Field getField(Class root, Class clazz, String name) throws PropertyNotFoundException {
		if ( clazz==null || clazz==Object.class ) {
			throw new PropertyNotFoundException("field [" + name + "] not found on " + root.getName()); 
		}
		Field field;
		try {
			field = clazz.getDeclaredField(name);
		}
		catch (NoSuchFieldException nsfe) {
			field = getField( root, clazz.getSuperclass(), name );
		}
		if ( !ReflectHelper.isPublic(clazz, field) ) field.setAccessible(true);
		return field;
	}
	
	public Getter getGetter(Class theClass, String propertyName)
		throws PropertyNotFoundException {
		return new DirectGetter( getField(theClass, propertyName), theClass, propertyName );
	}

	public Setter getSetter(Class theClass, String propertyName)
		throws PropertyNotFoundException {
		return new DirectSetter( getField(theClass, propertyName), theClass, propertyName );
	}

}
