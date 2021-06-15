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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.pojo.BasicLazyInitializer;
import org.hibernate.type.CompositeType;

/**
 * A Javassist-based lazy initializer proxy.
 *
 * @author Muga Nishizawa
 */
public class JavassistLazyInitializer extends BasicLazyInitializer implements MethodHandler {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, JavassistLazyInitializer.class.getName());

	private static final MethodFilter FINALIZE_FILTER = new MethodFilter() {
		public boolean isHandled(Method m) {
			// skip finalize methods
			return !( m.getParameterTypes().length == 0 && m.getName().equals( "finalize" ) );
		}
	};

	private Class[] interfaces;
	private boolean constructed = false;

	private JavassistLazyInitializer(
			final String entityName,
			final Class persistentClass,
			final Class[] interfaces,
			final Serializable id,
			final Method getIdentifierMethod,
			final Method setIdentifierMethod,
			final CompositeType componentIdType,
			final SessionImplementor session,
			final boolean overridesEquals) {
		super( entityName, persistentClass, id, getIdentifierMethod, setIdentifierMethod, componentIdType, session, overridesEquals );
		this.interfaces = interfaces;
	}

	public static HibernateProxy getProxy(
			final String entityName,
			final Class persistentClass,
			final Class[] interfaces,
			final Method getIdentifierMethod,
			final Method setIdentifierMethod,
			CompositeType componentIdType,
			final Serializable id,
			final SessionImplementor session) throws HibernateException {
		// note: interface is assumed to already contain HibernateProxy.class
		try {
			final JavassistLazyInitializer instance = new JavassistLazyInitializer(
					entityName,
					persistentClass,
					interfaces,
					id,
					getIdentifierMethod,
					setIdentifierMethod,
					componentIdType,
					session,
					ReflectHelper.overridesEquals(persistentClass)
			);
			ProxyFactory factory = new ProxyFactory();
			factory.setSuperclass( interfaces.length == 1 ? persistentClass : null );
			factory.setInterfaces( interfaces );
			factory.setFilter( FINALIZE_FILTER );
			Class cl = factory.createClass();
			final HibernateProxy proxy = ( HibernateProxy ) cl.newInstance();
			( ( ProxyObject ) proxy ).setHandler( instance );
			instance.constructed = true;
			return proxy;
		}
		catch ( Throwable t ) {
			LOG.error(LOG.javassistEnhancementFailed(entityName), t);
			throw new HibernateException(LOG.javassistEnhancementFailed(entityName), t);
		}
	}

	public static HibernateProxy getProxy(
			final Class factory,
			final String entityName,
			final Class persistentClass,
			final Class[] interfaces,
			final Method getIdentifierMethod,
			final Method setIdentifierMethod,
			final CompositeType componentIdType,
			final Serializable id,
			final SessionImplementor session,
			final boolean classOverridesEquals) throws HibernateException {

		final JavassistLazyInitializer instance = new JavassistLazyInitializer(
				entityName,
				persistentClass,
				interfaces, id,
				getIdentifierMethod,
				setIdentifierMethod,
				componentIdType,
				session,
				classOverridesEquals
		);

		final HibernateProxy proxy;
		try {
			proxy = ( HibernateProxy ) factory.newInstance();
		}
		catch ( Exception e ) {
			throw new HibernateException(
					"Javassist Enhancement failed: "
					+ persistentClass.getName(), e
			);
		}
		( ( ProxyObject ) proxy ).setHandler( instance );
		instance.constructed = true;
		return proxy;
	}

	public static Class getProxyFactory(
			Class persistentClass,
			Class[] interfaces) throws HibernateException {
		// note: interfaces is assumed to already contain HibernateProxy.class

		try {
			ProxyFactory factory = new ProxyFactory();
			factory.setSuperclass( interfaces.length == 1 ? persistentClass : null );
			factory.setInterfaces( interfaces );
			factory.setFilter( FINALIZE_FILTER );
			return factory.createClass();
		}
		catch ( Throwable t ) {
			LOG.error(LOG.javassistEnhancementFailed(persistentClass.getName()), t);
			throw new HibernateException(LOG.javassistEnhancementFailed(persistentClass.getName()), t);
		}
	}

	public Object invoke(
			final Object proxy,
			final Method thisMethod,
			final Method proceed,
			final Object[] args) throws Throwable {
		if ( this.constructed ) {
			Object result;
			try {
				result = this.invoke( thisMethod, args, proxy );
			}
			catch ( Throwable t ) {
				throw new Exception( t.getCause() );
			}
			if ( result == INVOKE_IMPLEMENTATION ) {
				Object target = getImplementation();
				final Object returnValue;
				try {
					if ( ReflectHelper.isPublic( persistentClass, thisMethod ) ) {
						if ( !thisMethod.getDeclaringClass().isInstance( target ) ) {
							throw new ClassCastException( target.getClass().getName() );
						}
						returnValue = thisMethod.invoke( target, args );
					}
					else {
						if ( !thisMethod.isAccessible() ) {
							thisMethod.setAccessible( true );
						}
						returnValue = thisMethod.invoke( target, args );
					}
					return returnValue == target ? proxy : returnValue;
				}
				catch ( InvocationTargetException ite ) {
					throw ite.getTargetException();
				}
			}
			else {
				return result;
			}
		}
		else {
			// while constructor is running
			if ( thisMethod.getName().equals( "getHibernateLazyInitializer" ) ) {
				return this;
			}
			else {
				return proceed.invoke( proxy, args );
			}
		}
	}

	@Override
	protected Object serializableProxy() {
		return new SerializableProxy(
				getEntityName(),
				persistentClass,
				interfaces,
				getIdentifier(),
				( isReadOnlySettingAvailable() ? Boolean.valueOf( isReadOnly() ) : isReadOnlyBeforeAttachedToSession() ),
				getIdentifierMethod,
				setIdentifierMethod,
				componentIdType
		);
	}
}
