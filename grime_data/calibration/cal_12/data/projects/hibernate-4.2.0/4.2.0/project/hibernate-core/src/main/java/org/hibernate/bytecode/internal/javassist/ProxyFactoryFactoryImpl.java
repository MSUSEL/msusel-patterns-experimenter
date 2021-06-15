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
package org.hibernate.bytecode.internal.javassist;

import java.lang.reflect.Method;
import java.util.HashMap;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyObject;

import org.hibernate.AssertionFailure;
import org.hibernate.HibernateException;
import org.hibernate.bytecode.spi.BasicProxyFactory;
import org.hibernate.bytecode.spi.ProxyFactoryFactory;
import org.hibernate.proxy.ProxyFactory;
import org.hibernate.proxy.pojo.javassist.JavassistProxyFactory;

/**
 * A factory for Javassist-based {@link ProxyFactory} instances.
 *
 * @author Steve Ebersole
 */
public class ProxyFactoryFactoryImpl implements ProxyFactoryFactory {

	/**
	 * Builds a Javassist-based proxy factory.
	 *
	 * @return a new Javassist-based proxy factory.
	 */
	public ProxyFactory buildProxyFactory() {
		return new JavassistProxyFactory();
	}

	public BasicProxyFactory buildBasicProxyFactory(Class superClass, Class[] interfaces) {
		return new BasicProxyFactoryImpl( superClass, interfaces );
	}

	private static class BasicProxyFactoryImpl implements BasicProxyFactory {
		private final Class proxyClass;

		public BasicProxyFactoryImpl(Class superClass, Class[] interfaces) {
			if ( superClass == null && ( interfaces == null || interfaces.length < 1 ) ) {
				throw new AssertionFailure( "attempting to build proxy without any superclass or interfaces" );
			}
			javassist.util.proxy.ProxyFactory factory = new javassist.util.proxy.ProxyFactory();
			factory.setFilter( FINALIZE_FILTER );
			if ( superClass != null ) {
				factory.setSuperclass( superClass );
			}
			if ( interfaces != null && interfaces.length > 0 ) {
				factory.setInterfaces( interfaces );
			}
			proxyClass = factory.createClass();
		}

		public Object getProxy() {
			try {
				ProxyObject proxy = ( ProxyObject ) proxyClass.newInstance();
				proxy.setHandler( new PassThroughHandler( proxy, proxyClass.getName() ) );
				return proxy;
			}
			catch ( Throwable t ) {
				throw new HibernateException( "Unable to instantiated proxy instance" );
			}
		}

		public boolean isInstance(Object object) {
			return proxyClass.isInstance( object );
		}
	}

	private static final MethodFilter FINALIZE_FILTER = new MethodFilter() {
		public boolean isHandled(Method m) {
			// skip finalize methods
			return !( m.getParameterTypes().length == 0 && m.getName().equals( "finalize" ) );
		}
	};

	private static class PassThroughHandler implements MethodHandler {
		private HashMap data = new HashMap();
		private final Object proxiedObject;
		private final String proxiedClassName;

		public PassThroughHandler(Object proxiedObject, String proxiedClassName) {
			this.proxiedObject = proxiedObject;
			this.proxiedClassName = proxiedClassName;
		}

		public Object invoke(
				Object object,
		        Method method,
		        Method method1,
		        Object[] args) throws Exception {
			String name = method.getName();
			if ( "toString".equals( name ) ) {
				return proxiedClassName + "@" + System.identityHashCode( object );
			}
			else if ( "equals".equals( name ) ) {
				return proxiedObject == object;
			}
			else if ( "hashCode".equals( name ) ) {
				return System.identityHashCode( object );
			}
			boolean hasGetterSignature = method.getParameterTypes().length == 0 && method.getReturnType() != null;
			boolean hasSetterSignature = method.getParameterTypes().length == 1 && ( method.getReturnType() == null || method.getReturnType() == void.class );
			if ( name.startsWith( "get" ) && hasGetterSignature ) {
				String propName = name.substring( 3 );
				return data.get( propName );
			}
			else if ( name.startsWith( "is" ) && hasGetterSignature ) {
				String propName = name.substring( 2 );
				return data.get( propName );
			}
			else if ( name.startsWith( "set" ) && hasSetterSignature) {
				String propName = name.substring( 3 );
				data.put( propName, args[0] );
				return null;
			}
			else {
				// todo : what else to do here?
				return null;
			}
		}
	}
}
