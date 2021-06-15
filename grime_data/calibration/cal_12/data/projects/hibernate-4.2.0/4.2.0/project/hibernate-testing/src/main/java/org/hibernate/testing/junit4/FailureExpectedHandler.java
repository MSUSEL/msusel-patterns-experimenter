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
package org.hibernate.testing.junit4;

import org.jboss.logging.Logger;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import org.hibernate.testing.FailureExpected;

/**
* @author Steve Ebersole
*/
class FailureExpectedHandler extends Statement {
	private static final Logger log = Logger.getLogger( FailureExpectedHandler.class );

	private final TestClassMetadata testClassMetadata;
	private final ExtendedFrameworkMethod extendedFrameworkMethod;
	private final Statement realInvoker;
	private final Object testInstance;

	public FailureExpectedHandler(
			Statement realInvoker,
			TestClassMetadata testClassMetadata,
			ExtendedFrameworkMethod extendedFrameworkMethod,
			Object testInstance) {
		this.realInvoker = realInvoker;
		this.testClassMetadata = testClassMetadata;
		this.extendedFrameworkMethod = extendedFrameworkMethod;
		this.testInstance = testInstance;
	}

	@Override
	public void evaluate() throws Throwable {
		final FailureExpected failureExpected = extendedFrameworkMethod.getFailureExpectedAnnotation();
		try {
			realInvoker.evaluate();
			// reaching here is expected, unless the test is marked as an expected failure
			if ( failureExpected != null ) {
				throw new FailureExpectedTestPassedException( extendedFrameworkMethod );
			}
		}
		catch (FailureExpectedTestPassedException e) {
			// just pass this along
			throw e;
		}
		catch (Throwable e) {
			// on error handling is very different based on whether the test was marked as an expected failure
			if ( failureExpected != null ) {

				// handle the expected failure case
				log.infof(
						"Ignoring expected failure [{}] : {}",
						Helper.extractTestName( extendedFrameworkMethod ),
						Helper.extractMessage( failureExpected )
				);
				testClassMetadata.performOnExpectedFailureCallback( testInstance );
				// most importantly, do not propagate exception...
			}
			else {
				// handle the non-expected failure case
				testClassMetadata.performOnFailureCallback( testInstance );
				throw e;
			}
		}
	}

	public static class FailureExpectedTestPassedException extends Exception {
		public FailureExpectedTestPassedException(FrameworkMethod frameworkMethod) {
			super( "Test marked as FailureExpected, but did not fail : " + Helper.extractTestName( frameworkMethod ) );
		}
	}
}
