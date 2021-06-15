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
package org.hibernate.event.service.spi;

/**
 * Defines listener duplication checking strategy, both in terms of when a duplication is detected (see
 * {@link #areMatch}) as well as how to handle a duplication (see {@link #getAction}).
 *
 * @author Steve Ebersole
 */
public interface DuplicationStrategy {
	/**
	 * The enumerated list of actions available on duplication match
	 */
	public static enum Action {
		ERROR,
		KEEP_ORIGINAL,
		REPLACE_ORIGINAL
	}

	/**
	 * Are the two listener instances considered a duplication?
	 *
	 * @param listener The listener we are currently trying to register
	 * @param original An already registered listener
	 *
	 * @return {@literal true} if the two instances are considered a duplication; {@literal false} otherwise
	 */
	public boolean areMatch(Object listener, Object original);

	/**
	 * How should a duplication be handled?
	 *
	 * @return The strategy for handling duplication
	 */
	public Action getAction();
}
