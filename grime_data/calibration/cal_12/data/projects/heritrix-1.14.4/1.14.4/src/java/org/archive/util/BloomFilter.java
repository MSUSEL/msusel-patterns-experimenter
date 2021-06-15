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
/* BloomFilter
*
* $Id: BloomFilter.java 3655 2005-07-05 19:20:03Z gojomo $
*
* Created on Jun 30, 2005
*
* Copyright (C) 2005 Internet Archive; an adaptation of
* LGPL work (C) Sebastiano Vigna
*
* This file is part of the Heritrix web crawler (crawler.archive.org).
*
* Heritrix is free software; you can redistribute it and/or modify
* it under the terms of the GNU Lesser Public License as published by
* the Free Software Foundation; either version 2.1 of the License, or
* any later version.
*
* Heritrix is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser Public License for more details.
*
* You should have received a copy of the GNU Lesser Public License
* along with Heritrix; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package org.archive.util;

/**
 * Common interface for different Bloom filter 
 * implementations
 * 
 * @author Gordon Mohr
 */
public interface BloomFilter {
	/** The number of character sequences in the filter.
	 *
	 * @return the number of character sequences in the filter (but see {@link #contains(CharSequence)}).
	 */
	public abstract int size();

	/** Checks whether the given character sequence is in this filter.
	 *
	 * <P>Note that this method may return true on a character sequence that is has
	 * not been added to the filter. This will happen with probability 2<sub>-<var>d</var></sub>,
	 * where <var>d</var> is the number of hash functions specified at creation time, if
	 * the number of the elements in the filter is less than <var>n</var>, the number
	 * of expected elements specified at creation time.
	 *
	 * @param s a character sequence.
	 * @return true if the sequence is in the filter (or if a sequence with the
	 * same hash sequence is in the filter).
	 */
	public abstract boolean contains(final CharSequence s);

	/** Adds a character sequence to the filter.
	 *
	 * @param s a character sequence.
	 * @return true if the character sequence was not in the filter (but see {@link #contains(CharSequence)}).
	 */
	public abstract boolean add(final CharSequence s);

	/**
     * The amount of memory in bytes consumed by the bloom 
     * bitfield.
     *
	 * @return memory used by bloom bitfield, in bytes
	 */
	public abstract long getSizeBytes();
}