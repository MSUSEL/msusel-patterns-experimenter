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
package org.hibernate.ejb.test.instrument;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.util.List;

import org.hibernate.ejb.instrument.InterceptFieldClassFileTransformer;

/**
 * @author Emmanuel Bernard
 * @author Dustin Schultz
 */
public class InstrumentedClassLoader extends ClassLoader {
	private List<String> entities;

	public InstrumentedClassLoader(ClassLoader parent) {
		super( parent );
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		// Do not instrument the following packages
		if (name != null
				&& (name.startsWith("java.lang.") || 
					name.startsWith("java.util.")))
			return getParent().loadClass(name);
		Class c = findLoadedClass( name );
		if ( c != null ) return c;

		byte[] transformed = loadClassBytes(name);
		
		return defineClass( name, transformed, 0, transformed.length );
	}
	
	/**
	 * Specialized {@link ClassLoader#loadClass(String)} that returns the class
	 * as a byte array.
	 * 
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	public byte[] loadClassBytes(String name) throws ClassNotFoundException {
		InputStream is = this.getResourceAsStream( name.replace( ".", "/" ) + ".class" );
		if ( is == null ) throw new ClassNotFoundException( name );
		byte[] buffer = new byte[409600];
		byte[] originalClass = new byte[0];
		int r = 0;
		try {
			r = is.read( buffer );
		}
		catch (IOException e) {
			throw new ClassNotFoundException( name + " not found", e );
		}
		while ( r >= buffer.length ) {
			byte[] temp = new byte[ originalClass.length + buffer.length ];
			System.arraycopy( originalClass, 0, temp, 0, originalClass.length );
			System.arraycopy( buffer, 0, temp, originalClass.length, buffer.length );
			originalClass = temp;
		}
		if ( r != -1 ) {
			byte[] temp = new byte[ originalClass.length + r ];
			System.arraycopy( originalClass, 0, temp, 0, originalClass.length );
			System.arraycopy( buffer, 0, temp, originalClass.length, r );
			originalClass = temp;
		}
		try {
			is.close();
		}
		catch (IOException e) {
			throw new ClassNotFoundException( name + " not found", e );
		}
		InterceptFieldClassFileTransformer t = new InterceptFieldClassFileTransformer( entities );
		byte[] transformed = new byte[0];
		try {
			transformed = t.transform(
					getParent(),
					name,
					null,
					null,
					originalClass
			);
		}
		catch (IllegalClassFormatException e) {
			throw new ClassNotFoundException( name + " not found", e );
		}
		
		return transformed;
	}

	public void setEntities(List<String> entities) {
		this.entities = entities;
	}
}
