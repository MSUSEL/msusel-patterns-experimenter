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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.resources;

import java.util.AbstractSet;
import java.util.Iterator;


/**
 * An immutable set built from an iterator, which will be filled only when needed. This
 * implementation do <strong>not</strong> check if all elements in the iterator are really
 * unique; we assume that it was already verified by {@link javax.imageio.spi.ServiceRegistry}.
 * This set is constructed by {@link org.geotools.referencing.FactoryFinder}.
 *
 * @since 2.0
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class LazySet<E> extends AbstractSet<E> {
    /**
     * The iterator to use for filling this set.
     */
    private final Iterator<? extends E> iterator;

    /**
     * The elements in this set. This array will grown as needed.
     */
    private E[] elements;

    /**
     * The current size of this set. This size will increases as long as there is some elements
     * remaining in the iterator. This is <strong>not</strong> the size returned by {@link #size()}.
     */
    private int size;

    /**
     * Construct a set to be filled using the specified iterator.
     * Iteration in the given iterator will occurs only when needed.
     */
    @SuppressWarnings("unchecked")
    public LazySet(final Iterator<? extends E> iterator) {
        this.iterator = iterator;
        elements = (E[]) new Object[4];
    }

    /**
     * Add the next element from the iterator to this set. This method doesn't check
     * if more element were available; the check must have been done before to invoke
     * this method.
     */
    private void addNext() {
        if (size >= elements.length) {
            elements = XArray.resize(elements, size*2);
        }
        elements[size++] = iterator.next();
    }

    /**
     * Returns an iterator over the elements contained in this set.
     * This is not the same iterator than the one given to the constructor.
     */
    public Iterator<E> iterator() {
        return new Iter();
    }

    /**
     * Returns the number of elements in this set. Invoking this method
     * force the set to immediately iterates through all remaining elements.
     */
    public int size() {
        while (iterator.hasNext()) {
            addNext();
        }
        return size;
    }

    /**
     * Tests if this set has no elements.
     */
    @Override
    public boolean isEmpty() {
        return size==0 && !iterator.hasNext();
    }

    /**
     * Returns {@code true} if an element exists at the given index.
     * The element is not loaded immediately.
     *
     * <strong>NOTE: This method is for use by iterators only.</strong>
     * It is not suited for more general usage since it doesn't check
     * for negative index and for skipped elements.
     */
    final boolean exists(final int index) {
        return index<size || iterator.hasNext();
    }

    /**
     * Returns the element at the specified position in this set.
     */
    public E get(final int index) {
        while (index >= size) {
            if (!iterator.hasNext()) {
                throw new IndexOutOfBoundsException(String.valueOf(index));
            }
            addNext();
        }
        return elements[index];
    }

    /**
     * The iterator implementation for the {@linkplain LazySet lazy set}.
     */
    private final class Iter implements Iterator<E> {
        /** Index of the next element to be returned. */
        private int cursor;

        /** Check if there is more elements. */
        public boolean hasNext() {
            return exists(cursor);
        }

        /** Returns the next element. */
        public E next() {
            return get(cursor++);
        }

        /** Always throws an exception, since {@link LazySet} are immutable. */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
