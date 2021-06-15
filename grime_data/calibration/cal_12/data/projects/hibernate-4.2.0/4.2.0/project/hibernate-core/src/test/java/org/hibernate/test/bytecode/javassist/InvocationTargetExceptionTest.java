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
package org.hibernate.test.bytecode.javassist;
import java.text.ParseException;

import org.junit.Test;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.bytecode.internal.javassist.BytecodeProviderImpl;
import org.hibernate.cfg.Environment;
import org.hibernate.test.bytecode.Bean;
import org.hibernate.testing.Skip;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Test that the Javassist-based lazy initializer properly handles InvocationTargetExceptions
 *
 * @author Steve Ebersole
 */
@Skip(
		condition = InvocationTargetExceptionTest.LocalSkipMatcher.class,
		message = "environment not configured for javassist bytecode provider"
)
public class InvocationTargetExceptionTest extends BaseCoreFunctionalTestCase {
	public static class LocalSkipMatcher implements Skip.Matcher {
		@Override
		public boolean isMatch() {
			return ! BytecodeProviderImpl.class.isInstance( Environment.getBytecodeProvider() );
		}
	}

	@Override
	public String[] getMappings() {
		return new String[] { "bytecode/Bean.hbm.xml" };
	}

	@Test
	public void testProxiedInvocationException() {
		Session s = openSession();
		s.beginTransaction();
		Bean bean = new Bean();
		bean.setSomeString( "my-bean" );
		s.save( bean );
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		bean = ( Bean ) s.load( Bean.class, bean.getSomeString() );
		assertFalse( Hibernate.isInitialized( bean ) );
		try {
			bean.throwException();
			fail( "exception not thrown" );
		}
		catch ( ParseException e ) {
			// expected behavior
		}
		catch ( Throwable t ) {
			fail( "unexpected exception type : " + t );
		}

		s.delete( bean );
		s.getTransaction().commit();
		s.close();
	}
}
