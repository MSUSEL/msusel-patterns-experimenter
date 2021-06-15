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
package org.hibernate.bytecode.spi;

import java.io.InputStream;

/**
 * A specialized classloader which performs bytecode enhancement on class
 * definitions as they are loaded into the classloader scope.
 *
 * @author Emmanuel Bernard
 * @author Steve Ebersole
 */
public class InstrumentedClassLoader extends ClassLoader {

	private ClassTransformer classTransformer;

	public InstrumentedClassLoader(ClassLoader parent, ClassTransformer classTransformer) {
		super( parent );
		this.classTransformer = classTransformer;
	}

	public Class loadClass(String name) throws ClassNotFoundException {
		if ( name.startsWith( "java." ) || classTransformer == null ) {
			return getParent().loadClass( name );
		}

		Class c = findLoadedClass( name );
		if ( c != null ) {
			return c;
		}

		InputStream is = this.getResourceAsStream( name.replace( '.', '/' ) + ".class" );
		if ( is == null ) {
			throw new ClassNotFoundException( name + " not found" );
		}

		try {
			byte[] originalBytecode = ByteCodeHelper.readByteCode( is );
			byte[] transformedBytecode = classTransformer.transform( getParent(), name, null, null, originalBytecode );
			if ( originalBytecode == transformedBytecode ) {
				// no transformations took place, so handle it as we would a
				// non-instrumented class
				return getParent().loadClass( name );
			}
			else {
				return defineClass( name, transformedBytecode, 0, transformedBytecode.length );
			}
		}
		catch( Throwable t ) {
			throw new ClassNotFoundException( name + " not found", t );
		}
	}
}
