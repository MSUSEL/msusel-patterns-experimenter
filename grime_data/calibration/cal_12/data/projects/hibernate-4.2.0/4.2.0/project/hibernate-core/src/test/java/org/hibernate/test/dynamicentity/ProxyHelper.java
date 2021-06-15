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
package org.hibernate.test.dynamicentity;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author <a href="mailto:steve@hibernate.org">Steve Ebersole </a>
 */
public class ProxyHelper {

	public static Person newPersonProxy() {
		return newPersonProxy( null );
	}

	public static Person newPersonProxy(Serializable id) {
		return ( Person ) Proxy.newProxyInstance(
				Person.class.getClassLoader(),
		        new Class[] {Person.class},
		        new DataProxyHandler( Person.class.getName(), id )
		);
	}

	public static Customer newCustomerProxy() {
		return newCustomerProxy( null );
	}

	public static Customer newCustomerProxy(Serializable id) {
		return ( Customer ) Proxy.newProxyInstance(
				Customer.class.getClassLoader(),
		        new Class[] {Customer.class},
		        new DataProxyHandler( Customer.class.getName(), id )
		);
	}

	public static Company newCompanyProxy() {
		return newCompanyProxy( null );
	}

	public static Company newCompanyProxy(Serializable id) {
		return ( Company ) Proxy.newProxyInstance(
				Company.class.getClassLoader(),
		        new Class[] {Company.class},
		        new DataProxyHandler( Company.class.getName(), id )
		);
	}

	public static Address newAddressProxy() {
		return newAddressProxy( null );
	}

	public static Address newAddressProxy(Serializable id) {
		return ( Address ) Proxy.newProxyInstance(
				Address.class.getClassLoader(),
		        new Class[] {Address.class},
		        new DataProxyHandler( Address.class.getName(), id )
		);
	}

	public static String extractEntityName(Object object) {
		// Our custom java.lang.reflect.Proxy instances actually bundle
		// their appropriate entity name, so we simply extract it from there
		// if this represents one of our proxies; otherwise, we return null
		if ( Proxy.isProxyClass( object.getClass() ) ) {
			InvocationHandler handler = Proxy.getInvocationHandler( object );
			if ( DataProxyHandler.class.isAssignableFrom( handler.getClass() ) ) {
				DataProxyHandler myHandler = ( DataProxyHandler ) handler;
				return myHandler.getEntityName();
			}
		}
		return null;
	}
}
