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
//$Id$
package org.hibernate.test.annotations.tuplizer;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author Emmanuel Bernard
 */
public class ProxyHelper {

	public static Country newPersonProxy() {
		return newCountryProxy( null );
	}

	public static Country newCountryProxy(Serializable id) {
		return ( Country ) Proxy.newProxyInstance(
				Country.class.getClassLoader(),
		        new Class[] {Country.class},
		        new DataProxyHandler( Country.class.getName(), id )
		);
	}

	public static Cuisine newCustomerProxy() {
		return newCuisineProxy( null );
	}

	public static Cuisine newCuisineProxy(Serializable id) {
		return ( Cuisine ) Proxy.newProxyInstance(
				Cuisine.class.getClassLoader(),
		        new Class[] {Cuisine.class},
		        new DataProxyHandler( Cuisine.class.getName(), id )
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
