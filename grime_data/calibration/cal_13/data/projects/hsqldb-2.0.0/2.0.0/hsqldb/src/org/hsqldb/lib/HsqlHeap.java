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

package org.hsqldb.lib;

/**
 * Provides the base HSQLDB interface for Heap ADT implementations. <p>
 *
 * In this context, a Heap is simply a collection-like ADT that allows addition
 * of elements and provides a way to remove the least element, given some
 * implementation-dependent strategy for imposing an order over its
 * elements. <p>
 *
 * Typically, an HsqlHeap will be implemented as a tree-like structure that
 * recursively guarantees a <i>Heap Invariant</i>, such that all nodes below
 * the root are greater than the root, given some comparison stragegy. <p>

 * This in turn provides the basis for an efficient implementation of ADTs such
 * PriorityQueue, since Heap operations using the typical implementation are,
 * in theory, guaranteed to be O(log n).
 *
 * @author boucherb@users
 * @version 1.7.2
 * @since 1.7.2
 */
public interface HsqlHeap {

    /**
     * Removes all of the elements from this Heap.
     */
    void clear();

    /**
     * Retrieves whether this Heap is empty.
     */
    boolean isEmpty();

    /**
     * Retrieves whether this Heap is full.
     */
    boolean isFull();

    /**
     * Adds the specified element to this Heap.
     *
     * @param o The element to add
     * @throws IllegalArgumentException if the implementation does
     *      not accept elements of the supplied type (optional)
     * throws RuntimeException if the implementation
     *      dictates that this Heap is not currently accepting additions
     *      or that this Heap is currently full (optional)
     */
    void add(Object o) throws IllegalArgumentException, RuntimeException;

    /**
     * Retrieves the least element from this Heap, without removing it.
     *
     * @return the least element from this Heap
     */
    Object peek();

    /**
     * Retrieves the least element from this Heap, removing it in the process.
     *
     * @return the least element from this Heap
     */
    Object remove();

    /**
     * Retrieves the number of elements currently in this Heap.
     *
     * @return the number of elements currently in this Heap
     */
    int size();
}
