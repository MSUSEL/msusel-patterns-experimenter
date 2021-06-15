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
package org.hibernate.test.instrument.buildtime;

import org.junit.Test;

import org.hibernate.bytecode.instrumentation.internal.FieldInterceptionHelper;
import org.hibernate.test.instrument.cases.Executable;
import org.hibernate.test.instrument.cases.TestCustomColumnReadAndWrite;
import org.hibernate.test.instrument.cases.TestDirtyCheckExecutable;
import org.hibernate.test.instrument.cases.TestFetchAllExecutable;
import org.hibernate.test.instrument.cases.TestInjectFieldInterceptorExecutable;
import org.hibernate.test.instrument.cases.TestIsPropertyInitializedExecutable;
import org.hibernate.test.instrument.cases.TestLazyExecutable;
import org.hibernate.test.instrument.cases.TestLazyManyToOneExecutable;
import org.hibernate.test.instrument.cases.TestLazyPropertyCustomTypeExecutable;
import org.hibernate.test.instrument.cases.TestManyToOneProxyExecutable;
import org.hibernate.test.instrument.cases.TestSharedPKOneToOneExecutable;
import org.hibernate.test.instrument.domain.Document;
import org.hibernate.testing.Skip;
import org.hibernate.testing.junit4.BaseUnitTestCase;

/**
 * @author Gavin King
 */
@Skip(
		message = "domain classes not instrumented for build-time instrumentation testing",
		condition = InstrumentTest.SkipCheck.class
)
public class InstrumentTest extends BaseUnitTestCase {
	@Test
	public void testDirtyCheck() throws Exception {
		execute( new TestDirtyCheckExecutable() );
	}

	@Test
	public void testFetchAll() throws Exception {
		execute( new TestFetchAllExecutable() );
	}

	@Test
	public void testLazy() throws Exception {
		execute( new TestLazyExecutable() );
	}

	@Test
	public void testLazyManyToOne() throws Exception {
		execute( new TestLazyManyToOneExecutable() );
	}

	@Test
	public void testSetFieldInterceptor() throws Exception {
		execute( new TestInjectFieldInterceptorExecutable() );
	}

	@Test
	public void testPropertyInitialized() throws Exception {
		execute( new TestIsPropertyInitializedExecutable() );
	}

	@Test
	public void testManyToOneProxy() throws Exception {
		execute( new TestManyToOneProxyExecutable() );
	}

	@Test
	public void testLazyPropertyCustomTypeExecutable() throws Exception {
		execute( new TestLazyPropertyCustomTypeExecutable() );
	}

	@Test
	public void testSharedPKOneToOne() throws Exception {
		execute( new TestSharedPKOneToOneExecutable() );
	}

	@Test
	public void testCustomColumnReadAndWrite() throws Exception {
		execute( new TestCustomColumnReadAndWrite() );
	}	
	
	private void execute(Executable executable) throws Exception {
		executable.prepare();
		try {
			executable.execute();
		}
		finally {
			executable.complete();
		}
	}

	public static class SkipCheck implements Skip.Matcher {
		@Override
		public boolean isMatch() {
			return ! FieldInterceptionHelper.isInstrumented( new Document() );
		}
	}
}

