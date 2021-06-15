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
package org.hibernate.test.instrument.runtime;

import java.lang.reflect.InvocationTargetException;

import org.junit.Rule;
import org.junit.Test;

import org.hibernate.HibernateException;
import org.hibernate.bytecode.buildtime.spi.BasicClassFilter;
import org.hibernate.bytecode.buildtime.spi.FieldFilter;
import org.hibernate.bytecode.spi.BytecodeProvider;
import org.hibernate.bytecode.spi.InstrumentedClassLoader;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.testing.SkipForDialect;
import org.hibernate.testing.junit4.BaseUnitTestCase;
import org.hibernate.testing.junit4.ClassLoadingIsolater;

/**
 * @author Steve Ebersole
 */
public abstract class AbstractTransformingClassLoaderInstrumentTestCase extends BaseUnitTestCase {

	@Rule
	public ClassLoadingIsolater isolater = new ClassLoadingIsolater(
			new ClassLoadingIsolater.IsolatedClassLoaderProvider() {
				final BytecodeProvider provider = buildBytecodeProvider();

				@Override
				public ClassLoader buildIsolatedClassLoader() {
					return new InstrumentedClassLoader(
							Thread.currentThread().getContextClassLoader(),
							provider.getTransformer(
									new BasicClassFilter( new String[] { "org.hibernate.test.instrument" }, null ),
									new FieldFilter() {
										public boolean shouldInstrumentField(String className, String fieldName) {
											return className.startsWith( "org.hibernate.test.instrument.domain" );
										}
										public boolean shouldTransformFieldAccess(String transformingClassName, String fieldOwnerClassName, String fieldName) {
											return fieldOwnerClassName.startsWith( "org.hibernate.test.instrument.domain" )
													&& transformingClassName.equals( fieldOwnerClassName );
										}
									}
							)
					);
				}

				@Override
				public void releaseIsolatedClassLoader(ClassLoader isolatedClassLoader) {
					// nothing to do
				}
			}
	);

	protected abstract BytecodeProvider buildBytecodeProvider();


	// the tests ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Test
	public void testSetFieldInterceptor() {
		executeExecutable( "org.hibernate.test.instrument.cases.TestInjectFieldInterceptorExecutable" );
	}

	@Test
	public void testDirtyCheck() {
		executeExecutable( "org.hibernate.test.instrument.cases.TestDirtyCheckExecutable" );
	}

	@Test
    @SkipForDialect( value = MySQLDialect.class, comment = "wrong sql in mapping, mysql needs double type, but it is float type in mapping")
	public void testFetchAll() throws Exception {
		executeExecutable( "org.hibernate.test.instrument.cases.TestFetchAllExecutable" );
	}

	@Test
    @SkipForDialect( value = MySQLDialect.class, comment = "wrong sql in mapping, mysql needs double type, but it is float type in mapping")
	public void testLazy() {
		executeExecutable( "org.hibernate.test.instrument.cases.TestLazyExecutable" );
	}

	@Test
    @SkipForDialect( value = MySQLDialect.class, comment = "wrong sql in mapping, mysql needs double type, but it is float type in mapping")
	public void testLazyManyToOne() {
		executeExecutable( "org.hibernate.test.instrument.cases.TestLazyManyToOneExecutable" );
	}

	@Test
    @SkipForDialect( value = MySQLDialect.class, comment = "wrong sql in mapping, mysql needs double type, but it is float type in mapping")
	public void testPropertyInitialized() {
		executeExecutable( "org.hibernate.test.instrument.cases.TestIsPropertyInitializedExecutable" );
	}

	@Test
	public void testManyToOneProxy() {
		executeExecutable( "org.hibernate.test.instrument.cases.TestManyToOneProxyExecutable" );
	}

	@Test
	public void testLazyPropertyCustomType() {
		executeExecutable( "org.hibernate.test.instrument.cases.TestLazyPropertyCustomTypeExecutable" );
	}

	@Test
	public void testSharedPKOneToOne() {
		executeExecutable( "org.hibernate.test.instrument.cases.TestSharedPKOneToOneExecutable" );
	}

	@Test
    @SkipForDialect( value = MySQLDialect.class, comment = "wrong sql in mapping, mysql needs double type, but it is float type in mapping")
	public void testCustomColumnReadAndWrite() {
		executeExecutable( "org.hibernate.test.instrument.cases.TestCustomColumnReadAndWrite" );
	}	

	// reflection code to ensure isolation into the created classloader ~~~~~~~

	private static final Class[] SIG = new Class[] {};
	private static final Object[] ARGS = new Object[] {};

	public void executeExecutable(String name) {
		Class execClass = null;
		Object executable = null;
		try {
			execClass = Thread.currentThread().getContextClassLoader().loadClass( name );
			executable = execClass.newInstance();
		}
		catch( Throwable t ) {
			throw new HibernateException( "could not load executable", t );
		}
		try {
			execClass.getMethod( "prepare", SIG ).invoke( executable, ARGS );
			execClass.getMethod( "execute", SIG ).invoke( executable, ARGS );
		}
		catch ( NoSuchMethodException e ) {
			throw new HibernateException( "could not exeucte executable", e );
		}
		catch ( IllegalAccessException e ) {
			throw new HibernateException( "could not exeucte executable", e );
		}
		catch ( InvocationTargetException e ) {
			throw new HibernateException( "could not exeucte executable", e.getTargetException() );
		}
		finally {
			try {
				execClass.getMethod( "complete", SIG ).invoke( executable, ARGS );
			}
			catch ( Throwable ignore ) {
			}
		}
	}
}
