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
 * An JoinedIterable is an Iterable that wraps a number of Iterables.
 *
 * This class makes multiple iterables look like one to the caller.
 * When any method from the Iterator interface is called on the
 * Iterator object returned by {@link #iterator()}, the JoinedIterable
 * will delegate to a single underlying Iterator. The JoinedIterable will
 * invoke the iterator on each Iterable, in sequence, until all Iterators
 * are exhausted.
 *
 * @author Gail Badner (adapted from JoinedIterator)
 */
public class JoinedIterable<T> implements Iterable<T> {
	private final TypeSafeJoinedIterator<T> iterator;

	public JoinedIterable(List<Iterable<T>> iterables) {
		if ( iterables == null ) {
			throw new NullPointerException( "Unexpected null iterables argument" );
		}
		iterator = new TypeSafeJoinedIterator<T>( iterables );
	}

	public Iterator<T> iterator() {
		return iterator;
	}

	private class TypeSafeJoinedIterator<T> implements Iterator<T> {

		// wrapped iterators
		private List<Iterable<T>> iterables;

		// index of current iterator in the wrapped iterators array
		private int currentIterableIndex;

		// the current iterator
		private Iterator<T> currentIterator;

		// the last used iterator
		private Iterator<T> lastUsedIterator;

		public TypeSafeJoinedIterator(List<Iterable<T>> iterables) {
			this.iterables = iterables;
		}

		public boolean hasNext() {
			updateCurrentIterator();
			return currentIterator.hasNext();
		}

		public T next() {
			updateCurrentIterator();
			return currentIterator.next();
		}

		public void remove() {
			updateCurrentIterator();
			lastUsedIterator.remove();
		}

		// call this before any Iterator method to make sure that the current Iterator
		// is not exhausted
		@SuppressWarnings( {"unchecked"})
		protected void updateCurrentIterator() {

			if ( currentIterator == null) {
				if( iterables.size() == 0  ) {
					currentIterator = EmptyIterator.INSTANCE;
				}
				else {
					currentIterator = iterables.get( 0 ).iterator();
				}
				// set last used iterator here, in case the user calls remove
				// before calling hasNext() or next() (although they shouldn't)
				lastUsedIterator = currentIterator;
			}

			while (! currentIterator.hasNext() && currentIterableIndex < iterables.size() - 1) {
				currentIterableIndex++;
				currentIterator = iterables.get( currentIterableIndex ).iterator();
			}
		}
	}
}
