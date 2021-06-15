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
package org.hibernate.internal.util.collections;

import java.util.Iterator;
import java.util.List;

/**
 * An JoinedIterator is an Iterator that wraps a number of Iterators.
 *
 * This class makes multiple iterators look like one to the caller.
 * When any method from the Iterator interface is called, the JoinedIterator
 * will delegate to a single underlying Iterator. The JoinedIterator will
 * invoke the Iterators in sequence until all Iterators are exhausted.
 *
 */
public class JoinedIterator implements Iterator {

	private static final Iterator[] ITERATORS = {};

	// wrapped iterators
	private Iterator[] iterators;

	// index of current iterator in the wrapped iterators array
	private int currentIteratorIndex;

	// the current iterator
	private Iterator currentIterator;

	// the last used iterator
	private Iterator lastUsedIterator;

	public JoinedIterator(List iterators) {
		this( (Iterator[]) iterators.toArray(ITERATORS) );
	}

	public JoinedIterator(Iterator[] iterators) {
		if( iterators==null )
			throw new NullPointerException("Unexpected NULL iterators argument");
		this.iterators = iterators;
	}

	public JoinedIterator(Iterator first, Iterator second) {
		this( new Iterator[] { first, second } );
	}

	public boolean hasNext() {
		updateCurrentIterator();
		return currentIterator.hasNext();
	}

	public Object next() {
		updateCurrentIterator();
		return currentIterator.next();
	}

	public void remove() {
		updateCurrentIterator();
		lastUsedIterator.remove();
	}


	// call this before any Iterator method to make sure that the current Iterator
	// is not exhausted
	protected void updateCurrentIterator() {

		if (currentIterator == null) {
			if( iterators.length==0  ) {
				currentIterator = EmptyIterator.INSTANCE;
			}
			else {
				currentIterator = iterators[0];
			}
			// set last used iterator here, in case the user calls remove
			// before calling hasNext() or next() (although they shouldn't)
			lastUsedIterator = currentIterator;
		}

		while (! currentIterator.hasNext() && currentIteratorIndex < iterators.length - 1) {
			currentIteratorIndex++;
			currentIterator = iterators[currentIteratorIndex];
		}
	}

}
