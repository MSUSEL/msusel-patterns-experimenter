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
package org.hibernate.cache.spi;

import java.util.Comparator;

/**
 * Describes attributes regarding the type of data to be cached.
 *
 * @author Steve Ebersole
 */
public interface CacheDataDescription {
	/**
	 * Is the data marked as being mutable?
	 *
	 * @return {@code true} if the data is mutable; {@code false} otherwise.
	 */
	public boolean isMutable();

	/**
	 * Is the data to be cached considered versioned?
	 *
	 * If {@code true}, it is illegal for {@link #getVersionComparator} to return {@code null}.
	 *
	 * @return {@code true} if the data is versioned; {@code false} otherwise.
	 */
	public boolean isVersioned();

	/**
	 * Get the comparator used to compare two different version values.  May return {@code null} <b>if</b>
	 * {@link #isVersioned()} returns false.
	 *
	 * @return The comparator for versions, or {@code null}
	 */
	public Comparator getVersionComparator();
}
