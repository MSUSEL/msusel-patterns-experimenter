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
package org.hibernate.ejb.event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.internal.util.ReflectHelper;

/**
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 */
public abstract class Callback implements Serializable {
	transient protected Method callbackMethod;

	public Callback(Method callbackMethod) {
		this.callbackMethod = callbackMethod;
	}

	public Method getCallbackMethod() {
		return callbackMethod;
	}

	public abstract void invoke(Object bean);

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		oos.writeObject( callbackMethod.toGenericString() );
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		String signature = (String) ois.readObject();
		StringTokenizer st = new StringTokenizer( signature, " ", false );
		String usefulSignature = null;
		while ( st.hasMoreElements() ) usefulSignature = (String) st.nextElement();
		int parenthesis = usefulSignature.indexOf( "(" );
		String methodAndClass = usefulSignature.substring( 0, parenthesis );
		int lastDot = methodAndClass.lastIndexOf( "." );
		String clazzName = methodAndClass.substring( 0, lastDot );
		Class callbackClass = ReflectHelper.classForName( clazzName, this.getClass() );
		String parametersString = usefulSignature.substring( parenthesis + 1, usefulSignature.length() - 1 );
		st = new StringTokenizer( parametersString, ", ", false );
		List<Class> parameters = new ArrayList<Class>();
		while ( st.hasMoreElements() ) {
			String parameter = (String) st.nextElement();
			parameters.add( ReflectHelper.classForName( parameter, this.getClass() ) );
		}
		String methodName = methodAndClass.substring( lastDot + 1, methodAndClass.length() );
		try {
			callbackMethod = callbackClass.getDeclaredMethod(
					methodName,
					parameters.toArray( new Class[ parameters.size() ] )
			);
			if ( ! callbackMethod.isAccessible() ) {
				callbackMethod.setAccessible( true );
			}
		}
		catch (NoSuchMethodException e) {
			throw new IOException( "Unable to get EJB3 callback method: " + signature + ", cause: " + e );
		}
	}
}
