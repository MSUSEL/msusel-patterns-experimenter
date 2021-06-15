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
package org.hibernate.cfg;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.mapping.SimpleValue;
import org.hibernate.mapping.Value;

/**
 * @author Emmanuel Bernard
 */
public abstract class FkSecondPass implements SecondPass {
	protected SimpleValue value;
	protected Ejb3JoinColumn[] columns;
	/**
	 * unique counter is needed to differentiate 2 instances of FKSecondPass
	 * as they are compared.
	 * Fairly hacky but IBM VM sometimes returns the same hashCode for 2 different objects
	 * TODO is it doable to rely on the Ejb3JoinColumn names? Not sure at they could be inferred
	 */
	private int uniqueCounter;
	private static AtomicInteger globalCounter = new AtomicInteger();

	public FkSecondPass(SimpleValue value, Ejb3JoinColumn[] columns) {
		this.value = value;
		this.columns = columns;
		this.uniqueCounter = globalCounter.getAndIncrement();
	}

	public int getUniqueCounter() {
		return uniqueCounter;
	}

	public Value getValue() {
		return value;
	}

	public boolean equals(Object o) {
		if ( this == o ) return true;
		if ( !( o instanceof FkSecondPass ) ) return false;

		FkSecondPass that = (FkSecondPass) o;

		if ( uniqueCounter != that.uniqueCounter ) return false;

		return true;
	}

	public int hashCode() {
		return uniqueCounter;
	}

	public abstract String getReferencedEntityName();

	public abstract boolean isInPrimaryKey();
}
