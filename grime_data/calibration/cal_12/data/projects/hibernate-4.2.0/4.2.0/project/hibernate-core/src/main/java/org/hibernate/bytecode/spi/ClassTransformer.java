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

import java.security.ProtectionDomain;

/**
 * A persistence provider provides an instance of this interface
 * to the PersistenceUnitInfo.addTransformer method.
 * The supplied transformer instance will get called to transform
 * entity class files when they are loaded and redefined.  The transformation
 * occurs before the class is defined by the JVM
 *
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @author Emmanuel Bernard
 */
public interface ClassTransformer
{
   /**
	* Invoked when a class is being loaded or redefined to add hooks for persistence bytecode manipulation
	*
	* @param loader the defining class loaderof the class being transformed.  It may be null if using bootstrap loader
	* @param classname The name of the class being transformed
	* @param classBeingRedefined If an already loaded class is being redefined, then pass this as a parameter
	* @param protectionDomain ProtectionDomain of the class being (re)-defined
	* @param classfileBuffer The input byte buffer in class file format
	* @return A well-formed class file that can be loaded
	*/
   public byte[] transform(ClassLoader loader,
					String classname,
					Class classBeingRedefined,
					ProtectionDomain protectionDomain,
					byte[] classfileBuffer);
}
