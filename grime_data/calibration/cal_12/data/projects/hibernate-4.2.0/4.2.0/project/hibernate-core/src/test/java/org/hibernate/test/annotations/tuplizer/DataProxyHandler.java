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
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * A simple {@link java.lang.reflect.InvocationHandler} to act as the handler for our generated
 * {@link java.lang.reflect.Proxy}-based entity instances.
 * <p/>
 * This is a trivial impl which simply keeps the property values into
 * a Map.
 *
 * @author <a href="mailto:steve@hibernate.org">Steve Ebersole </a>
 */
public final class DataProxyHandler implements InvocationHandler {
	private String entityName;
	private HashMap data = new HashMap();

	public DataProxyHandler(String entityName, Serializable id) {
		this.entityName = entityName;
		data.put( "Id", id );
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		if ( methodName.startsWith( "set" ) ) {
			String propertyName = methodName.substring( 3 );
			data.put( propertyName, args[0] );
		}
		else if ( methodName.startsWith( "get" ) ) {
			String propertyName = methodName.substring( 3 );
			return data.get( propertyName );
		}
		else if ( "toString".equals( methodName ) ) {
			return entityName + "#" + data.get( "Id" );
		}
		else if ( "hashCode".equals( methodName ) ) {
			return new Integer( this.hashCode() );
		}
		return null;
	}

	public String getEntityName() {
		return entityName;
	}

	public HashMap getData() {
		return data;
	}
}
