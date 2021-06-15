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
package org.archive.util.fingerprint;

/**
 * Set for holding primitive long fingerprints.
 *
 * @author Gordon Mohr
 */
public interface LongFPSet {
    /**
     * Add a fingerprint to the set.  Note that subclasses can implement
     * different policies on how to add - some might grow the available space,
     * others might implement some type of LRU caching.
     *
     * In particular, you cannot on the {@link #count()} method returning
     * 1 greater than before the addition.
     *
     * @param l the fingerprint to add
     * @return <code>true</code> if set has changed with this addition
     */
    boolean add(long l);

    /**
     *  Does this set contain a given fingerprint.
     * @param l the fingerprint to check for
     * @return <code>true</code> if the fingerprint is in the set
     */
    boolean contains(long l);

    /**
     *  Remove a fingerprint from the set, if it is there
     * @param l the fingerprint to remove
     * @return <code>true</code> if we removed the fingerprint
     */
    boolean remove(long l);

    /** get the number of elements in the Set
     * @return the number of elements in the Set
     */
    long count();

    /**
     * Do a contains() check that doesn't require laggy
     * activity (eg disk IO). If this returns true,
     * fp is definitely contained; if this returns
     * false, fp  *MAY* still be contained -- must use
     * full-cost contains() to be sure.
     *
     * @param fp the fingerprint to check for
     * @return <code>true</code> if contains the fingerprint
     */
    boolean quickContains(long fp);
}
