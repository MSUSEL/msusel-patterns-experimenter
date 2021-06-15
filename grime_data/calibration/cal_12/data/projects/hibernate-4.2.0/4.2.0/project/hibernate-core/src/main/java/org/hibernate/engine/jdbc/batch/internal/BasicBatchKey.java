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
package org.hibernate.engine.jdbc.batch.internal;

import org.hibernate.engine.jdbc.batch.spi.BatchKey;
import org.hibernate.jdbc.Expectation;

/**
 * @author Steve Ebersole
 */
public class BasicBatchKey implements BatchKey {
	private final String comparison;
	private final int statementCount;
	private final Expectation expectation;

//	public BasicBatchKey(String comparison, int statementCount, Expectation expectation) {
//		this.comparison = comparison;
//		this.statementCount = statementCount;
//		this.expectations = new Expectation[statementCount];
//		Arrays.fill( this.expectations, expectation );
//	}
//
//	public BasicBatchKey(String comparison, Expectation... expectations) {
//		this.comparison = comparison;
//		this.statementCount = expectations.length;
//		this.expectations = expectations;
//	}

	public BasicBatchKey(String comparison, Expectation expectation) {
		this.comparison = comparison;
		this.statementCount = 1;
		this.expectation = expectation;
	}

	@Override
	public Expectation getExpectation() {
		return expectation;
	}

	@Override
	public int getBatchedStatementCount() {
		return statementCount;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		BasicBatchKey that = (BasicBatchKey) o;

		if ( !comparison.equals( that.comparison ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return comparison.hashCode();
	}

}
