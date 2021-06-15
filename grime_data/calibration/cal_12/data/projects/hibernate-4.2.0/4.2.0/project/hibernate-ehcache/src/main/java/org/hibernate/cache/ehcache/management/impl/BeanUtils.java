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
package org.hibernate.cache.ehcache.management.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Reflective utilities for dealing with backward-incompatible change to statistics types in Hibernate 3.5.
 *
 * @author gkeim
 */
public class BeanUtils {
	/**
	 * Return the named getter method on the bean or null if not found.
	 *
	 * @param bean
	 * @param propertyName
	 *
	 * @return the named getter method
	 */
	private static Method getMethod(Object bean, String propertyName) {
		StringBuilder sb = new StringBuilder( "get" ).append( Character.toUpperCase( propertyName.charAt( 0 ) ) );
		if ( propertyName.length() > 1 ) {
			sb.append( propertyName.substring( 1 ) );
		}
		String getterName = sb.toString();
		for ( Method m : bean.getClass().getMethods() ) {
			if ( getterName.equals( m.getName() ) && m.getParameterTypes().length == 0 ) {
				return m;
			}
		}
		return null;
	}

	/**
	 * Return the named field on the bean or null if not found.
	 *
	 * @param bean
	 * @param propertyName
	 *
	 * @return the named field
	 */
	private static Field getField(Object bean, String propertyName) {
		for ( Field f : bean.getClass().getDeclaredFields() ) {
			if ( propertyName.equals( f.getName() ) ) {
				return f;
			}
		}
		return null;
	}

	private static void validateArgs(Object bean, String propertyName) {
		if ( bean == null ) {
			throw new IllegalArgumentException( "bean is null" );
		}
		if ( propertyName == null ) {
			throw new IllegalArgumentException( "propertyName is null" );
		}
		if ( propertyName.trim().length() == 0 ) {
			throw new IllegalArgumentException( "propertyName is empty" );
		}
	}

	/**
	 * Retrieve a named bean property value.
	 *
	 * @param bean bean
	 * @param propertyName
	 *
	 * @return the property value
	 */
	public static Object getBeanProperty(Object bean, String propertyName) {
		validateArgs( bean, propertyName );

		// try getters first
		Method getter = getMethod( bean, propertyName );
		if ( getter != null ) {
			try {
				return getter.invoke( bean );
			}
			catch ( Exception e ) {
				/**/
			}
		}

		// then try fields
		Field field = getField( bean, propertyName );
		if ( field != null ) {
			try {
				field.setAccessible( true );
				return field.get( bean );
			}
			catch ( Exception e ) {
				/**/
			}
		}

		return null;
	}

	/**
	 * Retrieve a Long bean property value.
	 *
	 * @param bean bean
	 * @param propertyName
	 *
	 * @return long value
	 *
	 * @throws NoSuchFieldException
	 */
	public static long getLongBeanProperty(final Object bean, final String propertyName) throws NoSuchFieldException {
		validateArgs( bean, propertyName );
		Object o = getBeanProperty( bean, propertyName );
		if ( o == null ) {
			throw new NoSuchFieldException( propertyName );
		}
		else if ( !( o instanceof Number ) ) {
			throw new IllegalArgumentException( propertyName + " not an Number" );
		}
		return ( (Number) o ).longValue();
	}
}
