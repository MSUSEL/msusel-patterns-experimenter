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
package org.hibernate.proxy.pojo.javassist;
import java.io.Serializable;
import java.lang.reflect.Method;

import org.hibernate.HibernateException;
import org.hibernate.proxy.AbstractSerializableProxy;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.CompositeType;

/**
 * Serializable placeholder for Javassist proxies
 */
public final class SerializableProxy extends AbstractSerializableProxy {

	private Class persistentClass;
	private Class[] interfaces;
	private Class getIdentifierMethodClass;
	private Class setIdentifierMethodClass;
	private String getIdentifierMethodName;
	private String setIdentifierMethodName;
	private Class[] setIdentifierMethodParams;
	private CompositeType componentIdType;

	public SerializableProxy() {
	}

	public SerializableProxy(
			final String entityName,
			final Class persistentClass,
			final Class[] interfaces,
			final Serializable id,
			final Boolean readOnly,
			final Method getIdentifierMethod,
			final Method setIdentifierMethod,
			CompositeType componentIdType) {
		super( entityName, id, readOnly );
		this.persistentClass = persistentClass;
		this.interfaces = interfaces;
		if (getIdentifierMethod!=null) {
			getIdentifierMethodClass = getIdentifierMethod.getDeclaringClass();
			getIdentifierMethodName = getIdentifierMethod.getName();
		}
		if (setIdentifierMethod!=null) {
			setIdentifierMethodClass = setIdentifierMethod.getDeclaringClass();
			setIdentifierMethodName = setIdentifierMethod.getName();
			setIdentifierMethodParams = setIdentifierMethod.getParameterTypes();
		}
		this.componentIdType = componentIdType;
	}

	private Object readResolve() {
		try {
			HibernateProxy proxy = JavassistLazyInitializer.getProxy(
				getEntityName(),
				persistentClass,
				interfaces,
				getIdentifierMethodName==null
						? null
						: getIdentifierMethodClass.getDeclaredMethod( getIdentifierMethodName, (Class[]) null ),
				setIdentifierMethodName==null
						? null 
						: setIdentifierMethodClass.getDeclaredMethod(setIdentifierMethodName, setIdentifierMethodParams),
				componentIdType,
				getId(),
				null
			);
			setReadOnlyBeforeAttachedToSession( ( JavassistLazyInitializer ) proxy.getHibernateLazyInitializer() );
			return proxy;
		}
		catch (NoSuchMethodException nsme) {
			throw new HibernateException("could not create proxy for entity: " + getEntityName(), nsme);
		}
	}
}
