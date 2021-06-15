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
package org.hibernate.bytecode.buildtime.internal;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

import javassist.bytecode.ClassFile;

import org.hibernate.bytecode.buildtime.spi.AbstractInstrumenter;
import org.hibernate.bytecode.buildtime.spi.BasicClassFilter;
import org.hibernate.bytecode.buildtime.spi.ClassDescriptor;
import org.hibernate.bytecode.buildtime.spi.Logger;
import org.hibernate.bytecode.internal.javassist.BytecodeProviderImpl;
import org.hibernate.bytecode.internal.javassist.FieldHandled;
import org.hibernate.bytecode.spi.ClassTransformer;

/**
 * Strategy for performing build-time instrumentation of persistent classes in order to enable
 * field-level interception using Javassist.
 *
 * @author Steve Ebersole
 * @author Muga Nishizawa
 */
public class JavassistInstrumenter extends AbstractInstrumenter {

	private static final BasicClassFilter CLASS_FILTER = new BasicClassFilter();

	private final BytecodeProviderImpl provider = new BytecodeProviderImpl();

	public JavassistInstrumenter(Logger logger, Options options) {
		super( logger, options );
	}

	@Override
    protected ClassDescriptor getClassDescriptor(byte[] bytecode) throws IOException {
		return new CustomClassDescriptor( bytecode );
	}

	@Override
    protected ClassTransformer getClassTransformer(ClassDescriptor descriptor, Set classNames) {
		if ( descriptor.isInstrumented() ) {
			logger.debug( "class [" + descriptor.getName() + "] already instrumented" );
			return null;
		}
		else {
			return provider.getTransformer( CLASS_FILTER, new CustomFieldFilter( descriptor, classNames ) );
		}
	}

	private static class CustomClassDescriptor implements ClassDescriptor {
		private final byte[] bytes;
		private final ClassFile classFile;

		public CustomClassDescriptor(byte[] bytes) throws IOException {
			this.bytes = bytes;
			this.classFile = new ClassFile( new DataInputStream( new ByteArrayInputStream( bytes ) ) );
		}

		public String getName() {
			return classFile.getName();
		}

		public boolean isInstrumented() {
			String[] interfaceNames = classFile.getInterfaces();
			for ( String interfaceName : interfaceNames ) {
				if ( FieldHandled.class.getName().equals( interfaceName ) ) {
					return true;
				}
			}
			return false;
		}

		public byte[] getBytes() {
			return bytes;
		}
	}

}
