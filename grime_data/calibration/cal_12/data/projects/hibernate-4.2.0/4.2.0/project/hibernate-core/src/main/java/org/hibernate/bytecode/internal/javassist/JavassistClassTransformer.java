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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.ProtectionDomain;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.bytecode.ClassFile;
import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.bytecode.buildtime.spi.ClassFilter;
import org.hibernate.bytecode.spi.AbstractClassTransformerImpl;
import org.hibernate.internal.CoreMessageLogger;

/**
 * Enhance the classes allowing them to implements InterceptFieldEnabled
 * This interface is then used by Hibernate for some optimizations.
 *
 * @author Emmanuel Bernard
 * @author Steve Ebersole
 * @author Dustin Schultz
 */
public class JavassistClassTransformer extends AbstractClassTransformerImpl {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class,
                                                                       JavassistClassTransformer.class.getName());

	public JavassistClassTransformer(ClassFilter classFilter, org.hibernate.bytecode.buildtime.spi.FieldFilter fieldFilter) {
		super( classFilter, fieldFilter );
	}

	@Override
	protected byte[] doTransform(
			ClassLoader loader,
			String className,
			Class classBeingRedefined,
			ProtectionDomain protectionDomain,
			byte[] classfileBuffer) {
		ClassFile classfile;
		try {
			// WARNING: classfile only
			classfile = new ClassFile( new DataInputStream( new ByteArrayInputStream( classfileBuffer ) ) );
		}
		catch (IOException e) {
			LOG.unableToBuildEnhancementMetamodel( className );
			return classfileBuffer;
		}
		// This is the same as ClassPool.getDefault() but ensures a new ClassPool per
		ClassPool cp = new ClassPool();
		cp.appendSystemPath();
		cp.appendClassPath(new ClassClassPath(this.getClass()));
		cp.appendClassPath(new ClassClassPath(classfile.getClass()));
		try {
			cp.makeClassIfNew(new ByteArrayInputStream(classfileBuffer));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		FieldTransformer transformer = getFieldTransformer( classfile, cp );
		if ( transformer != null ) {
			LOG.debugf( "Enhancing %s", className );
			DataOutputStream out = null;
			try {
				transformer.transform( classfile );
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				out = new DataOutputStream( byteStream );
				classfile.write( out );
				return byteStream.toByteArray();
			}
			catch (Exception e) {
				LOG.unableToTransformClass( e.getMessage() );
				throw new HibernateException( "Unable to transform class: " + e.getMessage() );
			}
			finally {
				try {
					if ( out != null ) out.close();
				}
				catch (IOException e) {
					//swallow
				}
			}
		}
		return classfileBuffer;
	}

	protected FieldTransformer getFieldTransformer(final ClassFile classfile, final ClassPool classPool) {
		if ( alreadyInstrumented( classfile ) ) {
			return null;
		}
		return new FieldTransformer(
				new FieldFilter() {
					public boolean handleRead(String desc, String name) {
						return fieldFilter.shouldInstrumentField( classfile.getName(), name );
					}

					public boolean handleWrite(String desc, String name) {
						return fieldFilter.shouldInstrumentField( classfile.getName(), name );
					}

					public boolean handleReadAccess(String fieldOwnerClassName, String fieldName) {
						return fieldFilter.shouldTransformFieldAccess( classfile.getName(), fieldOwnerClassName, fieldName );
					}

					public boolean handleWriteAccess(String fieldOwnerClassName, String fieldName) {
						return fieldFilter.shouldTransformFieldAccess( classfile.getName(), fieldOwnerClassName, fieldName );
					}
				}, classPool
		);
	}

	private boolean alreadyInstrumented(ClassFile classfile) {
		String[] intfs = classfile.getInterfaces();
		for ( int i = 0; i < intfs.length; i++ ) {
			if ( FieldHandled.class.getName().equals( intfs[i] ) ) {
				return true;
			}
		}
		return false;
	}
}
