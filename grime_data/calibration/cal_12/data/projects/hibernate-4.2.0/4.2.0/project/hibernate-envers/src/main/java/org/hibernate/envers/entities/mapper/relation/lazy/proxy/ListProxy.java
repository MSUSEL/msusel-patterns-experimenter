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
package org.hibernate.envers.entities.mapper.relation.lazy.proxy;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class ListProxy<U> extends CollectionProxy<U, List<U>> implements List<U> {
    private static final long serialVersionUID = -5479232938279790987L;

    public ListProxy() {
    }

    public ListProxy(org.hibernate.envers.entities.mapper.relation.lazy.initializor.Initializor<List<U>> initializor) {
        super(initializor);
    }

    public boolean addAll(int index, Collection<? extends U> c) {
        checkInit();
        return delegate.addAll(index, c);
    }

    public U get(int index) {
        checkInit();
        return delegate.get(index);
    }

    public U set(int index, U element) {
        checkInit();
        return delegate.set(index, element);
    }

    public void add(int index, U element) {
        checkInit();
        delegate.add(index, element);
    }

    public U remove(int index) {
        checkInit();
        return delegate.remove(index);
    }

    public int indexOf(Object o) {
        checkInit();
        return delegate.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        checkInit();
        return delegate.lastIndexOf(o);
    }

    public ListIterator<U> listIterator() {
        checkInit();
        return delegate.listIterator();
    }

    public ListIterator<U> listIterator(int index) {
        checkInit();
        return delegate.listIterator(index);
    }

    public List<U> subList(int fromIndex, int toIndex) {
        checkInit();
        return delegate.subList(fromIndex, toIndex);
    }
}
