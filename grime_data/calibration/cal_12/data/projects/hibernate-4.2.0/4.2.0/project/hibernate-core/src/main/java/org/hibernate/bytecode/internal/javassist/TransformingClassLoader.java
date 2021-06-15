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

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import org.hibernate.HibernateException;

/**
 * @author Steve Ebersole
 */
public class TransformingClassLoader extends ClassLoader {
	private ClassLoader parent;
	private ClassPool classPool;

	/*package*/ TransformingClassLoader(ClassLoader parent, String[] classpath) {
		this.parent = parent;
		classPool = new ClassPool( true );
		for ( int i = 0; i < classpath.length; i++ ) {
			try {
				classPool.appendClassPath( classpath[i] );
			}
			catch ( NotFoundException e ) {
				throw new HibernateException(
						"Unable to resolve requested classpath for transformation [" +
						classpath[i] + "] : " + e.getMessage()
				);
			}
		}
	}

	protected Class findClass(String name) throws ClassNotFoundException {
        try {
            CtClass cc = classPool.get( name );
	        // todo : modify the class definition if not already transformed...
            byte[] b = cc.toBytecode();
            return defineClass( name, b, 0, b.length );
        }
        catch ( NotFoundException e ) {
            throw new ClassNotFoundException();
        }
        catch ( IOException e ) {
            throw new ClassNotFoundException();
        }
        catch ( CannotCompileException e ) {
            throw new ClassNotFoundException();
        }
    }

	public void release() {
		classPool = null;
		parent = null;
	}
}
