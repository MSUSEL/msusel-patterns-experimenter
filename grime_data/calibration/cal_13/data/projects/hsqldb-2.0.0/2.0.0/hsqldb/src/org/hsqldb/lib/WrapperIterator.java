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

import java.util.NoSuchElementException;

/**
 *  An Iterator that returns the elements of a specified array, or other
 *  iterators etc. The collection of objects returned depends on the
 *  constructor used.<p>
 *
 *  Based on similar Enumerator code by boucherb@users
 *
 * @author fred@users
 * @version 1.9.0
 * @since HSQLDB 1.7.2
 */
public class WrapperIterator implements Iterator {

    private static final Object[] emptyelements = new Object[0];
    private Object[]              elements;
    private int                   i;

    // chained iterators
    private boolean  chained;
    private Iterator it1;
    private Iterator it2;

    /** return only not null elements */
    private boolean notNull;

    /**
     * Constructor for an empty iterator. <p>
     */
    public WrapperIterator() {
        this.elements = emptyelements;
    }

    /**
     * Constructor for all elements of the specified array. <p>
     *
     * @param elements the array of objects to enumerate
     */
    public WrapperIterator(Object[] elements) {
        this.elements = elements;
    }

    /**
     * Constructor for not-null elements of specified array. <p>
     *
     * @param elements the array of objects to iterate
     */
    public WrapperIterator(Object[] elements, boolean notNull) {
        this.elements = elements;
        this.notNull  = notNull;
    }

    /**
     * Constructor for a singleton object iterator
     *
     * @param element the single object to iterate
     */
    public WrapperIterator(Object element) {
        this.elements = new Object[]{ element };
    }

    /**
     * Constructor for a chained iterator that returns the elements of the two
     * specified iterators.
     */
    public WrapperIterator(Iterator it1, Iterator it2) {

        this.it1 = it1;
        this.it2 = it2;
        chained  = true;
    }

    /**
     * Tests if this iterator contains more elements. <p>
     *
     * @return  <code>true</code> if this iterator contains more elements;
     *          <code>false</code> otherwise.
     */
    public boolean hasNext() {

        // for chained iterators
        if (chained) {
            if (it1 == null) {
                if (it2 == null) {
                    return false;
                }

                if (it2.hasNext()) {
                    return true;
                }

                it2 = null;

                return false;
            } else {
                if (it1.hasNext()) {
                    return true;
                }

                it1 = null;

                return hasNext();
            }
        }

        // for other interators
        if (elements == null) {
            return false;
        }

        for (; notNull && i < elements.length && elements[i] == null; i++) {}

        if (i < elements.length) {
            return true;
        } else {

            // release elements for garbage collection
            elements = null;

            return false;
        }
    }

    /**
     * Returns the next element.
     *
     * @return the next element
     * @throws NoSuchElementException if there is no next element
     */
    public Object next() {

        // for chained iterators
        if (chained) {
            if (it1 == null) {
                if (it2 == null) {
                    throw new NoSuchElementException();
                }

                if (it2.hasNext()) {
                    return it2.next();
                }

                it2 = null;

                next();
            } else {
                if (it1.hasNext()) {
                    return it1.next();
                }

                it1 = null;

                next();
            }
        }

        // for other itertors
        if (hasNext()) {
            return elements[i++];
        }

        throw new NoSuchElementException();
    }

    public int nextInt() {
        throw new NoSuchElementException();
    }

    public long nextLong() {
        throw new NoSuchElementException();
    }

    public void remove() {
        throw new NoSuchElementException();
    }

    public void setValue(Object value) {
        throw new NoSuchElementException();
    }
}
