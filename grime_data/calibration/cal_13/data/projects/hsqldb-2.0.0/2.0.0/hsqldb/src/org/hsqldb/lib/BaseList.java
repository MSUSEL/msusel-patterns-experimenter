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
 * Abstract base for Lists
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.7.0
 */
abstract class BaseList {

    protected int elementCount;

    abstract Object get(int index);

    abstract Object remove(int index);

    abstract boolean add(Object o);

    abstract int size();

    public boolean contains(Object o) {
        return indexOf(o) == -1 ? false
                             : true;
    }

    public boolean remove(Object o) {

        int i = indexOf(o);

        if (i == -1) {
            return false;
        }

        remove(i);

        return true;
    }

    public int indexOf(Object o) {

        for (int i = 0, size = size(); i < size; i++) {
            Object current = get(i);

            if (current == null) {
                if (o == null) {
                    return i;
                }
            } else if (current.equals(o)) {
                return i;
            }
        }

        return -1;
    }

    public boolean addAll(Collection other) {

        boolean  result = false;
        Iterator it     = other.iterator();

        while (it.hasNext()) {
            result = true;

            add(it.next());
        }

        return result;
    }

    public boolean addAll(Object[] array) {

        boolean  result = false;
        for (int i = 0; i < array.length; i++) {
            result = true;

            add(array[i]);
        }

        return result;
    }

    public boolean isEmpty() {
        return elementCount == 0;
    }

    /** Returns a string representation */
    public String toString() {

        StringBuffer sb = new StringBuffer(32 + elementCount * 3);

        sb.append("List : size=");
        sb.append(elementCount);
        sb.append(' ');
        sb.append('{');

        Iterator it = iterator();

        while (it.hasNext()) {
            sb.append(it.next());

            if (it.hasNext()) {
                sb.append(',');
                sb.append(' ');
            }
        }

        sb.append('}');

        return sb.toString();
    }

    public Iterator iterator() {
        return new BaseListIterator();
    }

    private class BaseListIterator implements Iterator {

        int     counter = 0;
        boolean removed;

        public boolean hasNext() {
            return counter < elementCount;
        }

        public Object next() {

            if (counter < elementCount) {
                removed = false;

                Object returnValue = get(counter);

                counter++;

                return returnValue;
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

            if (removed) {
                throw new NoSuchElementException("Iterator");
            }

            removed = true;

            if (counter != 0) {
                BaseList.this.remove(counter - 1);

                counter--;    // above can throw, so decrement if successful

                return;
            }

            throw new NoSuchElementException();
        }

        public void setValue(Object value) {
            throw new NoSuchElementException();
        }
    }
}
