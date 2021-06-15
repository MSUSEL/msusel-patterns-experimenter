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
import java.util.LinkedList;

import org.apache.commons.collections.Predicate;

/** An in-memory implementation of a {@link Queue}.
 *
 * @author Gordon Mohr
 *
 */
public class MemQueue<T> extends LinkedList<T> implements Queue<T> {

    private static final long serialVersionUID = -9077824759011044247L;

    /** Create a new, empty MemQueue
     */
    public MemQueue() {
        super();
    }

    /**
     * @see org.archive.queue.Queue#enqueue(Object)
     */
    public void enqueue(T o) {
        add(o);
    }

    /**
     * @see org.archive.queue.Queue#dequeue()
     */
    public T dequeue() {
        return removeFirst();
    }

    /**
     * @see org.archive.queue.Queue#length()
     */
    public long length() {
        return size();
    }

    /**
     * @see org.archive.queue.Queue#release()
     */
    public void release() {
        // nothing to release
    }

    /**
     * @see org.archive.queue.Queue#peek()
     */
    public T peek() {
        return getFirst();
    }


    /**
     * @see org.archive.queue.Queue#getIterator(boolean)
     */
    public Iterator<T> getIterator(boolean inCacheOnly) {
        return listIterator();
    }

    /**
     * @see org.archive.queue.Queue#deleteMatchedItems(org.apache.commons.collections.Predicate)
     */
    public long deleteMatchedItems(Predicate matcher) {
        Iterator<T> it = listIterator();
        long numberOfDeletes = 0;
        while(it.hasNext()){
            if(matcher.evaluate(it.next())){
                it.remove();
                numberOfDeletes++;
            }
        }
        return numberOfDeletes;
    }

    /* (non-Javadoc)
     * @see org.archive.queue.Queue#unpeek()
     */
    public void unpeek() {
        // nothing necessary
    }



}
