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
package org.archive.queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.collections.Predicate;


/** 
 * An Abstract queue.  It should implement FIFO semantics.
 * 
 * @author gojomo
 *
 */
public interface Queue<T> {

    /** Add an entry to the end of queue
     * @param obj the entry to queue
     */
    void enqueue(T obj);

    /** is the queue empty?
     *
     * @return <code>true</code> if the queue has no elements
     */
    boolean isEmpty();

    /** remove an entry from the start of the  queue
     *
     * @return the object
     * @throws java.util.NoSuchElementException
     */
    T dequeue() throws NoSuchElementException;

    /** get the number of elements in the queue
     *
     * @return the number of elements in the queue
     */
    long length();

    /**
     * release any OS/IO resources associated with Queue
     */
    void release();

    /**
     * Give the top object in the queue, leaving it in place to be
     * returned by future peek() or dequeue() invocations.
     * 
     * @return top object, without removing it
     */
    T peek();

    /**
     * Releases queue from the obligation to return in the
     * next peek()/dequeue() the same object as returned by
     * any previous peek(). 
     */
    void unpeek();
    
    /**
     * Returns an iterator for the queue.
     * <p>
     * The returned iterator's <code>remove</code> method is considered
     * unsafe.
     * <p>
     * Editing the queue while using the iterator is not safe.
     * @param inCacheOnly
     * @return an iterator for the queue
     */
    Iterator<T> getIterator(boolean inCacheOnly);

    /**
     * All objects in the queue where <code>matcher.match(object)</code>
     * returns true will be deleted from the queue.
     * <p>
     * Making other changes to the queue while this method is being
     * processed is not safe.
     * @param matcher a predicate
     * @return the number of deleted items
     */
    long deleteMatchedItems(Predicate matcher);
}
