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
package org.hibernate.ejb.instrument;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.bytecode.buildtime.spi.ClassFilter;
import org.hibernate.bytecode.buildtime.spi.FieldFilter;
import org.hibernate.bytecode.spi.ClassTransformer;
import org.hibernate.cfg.Environment;

/**
 * Enhance the classes allowing them to implements InterceptFieldEnabled
 * This interface is then used by Hibernate for some optimizations.
 *
 * @author Emmanuel Bernard
 */
public class InterceptFieldClassFileTransformer implements javax.persistence.spi.ClassTransformer {
	private ClassTransformer classTransformer;

	public InterceptFieldClassFileTransformer(List<String> entities) {
		final List<String> copyEntities = new ArrayList<String>( entities.size() );
		copyEntities.addAll( entities );
		classTransformer = Environment.getBytecodeProvider().getTransformer(
				//TODO change it to a static class to make it faster?
				new ClassFilter() {
					public boolean shouldInstrumentClass(String className) {
						return copyEntities.contains( className );
					}
				},
				//TODO change it to a static class to make it faster?
				new FieldFilter() {

					public boolean shouldInstrumentField(String className, String fieldName) {
						return true;
					}

					public boolean shouldTransformFieldAccess(
							String transformingClassName, String fieldOwnerClassName, String fieldName
					) {
						return true;
					}
				}
		);
	}

	public byte[] transform(
			ClassLoader loader,
			String className,
			Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain,
			byte[] classfileBuffer ) throws IllegalClassFormatException {
		try {
			return classTransformer.transform( loader, className, classBeingRedefined,
					protectionDomain, classfileBuffer );
		}
		catch (Exception e) {
			throw new IllegalClassFormatException( e.getMessage() );
		}
	}
}
