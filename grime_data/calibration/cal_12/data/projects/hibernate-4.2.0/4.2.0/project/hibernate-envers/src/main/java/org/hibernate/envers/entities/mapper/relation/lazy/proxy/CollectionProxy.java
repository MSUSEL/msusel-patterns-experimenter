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
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import org.hibernate.envers.entities.mapper.relation.lazy.initializor.Initializor;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public abstract class CollectionProxy<U, T extends Collection<U>> implements Collection<U>, Serializable {
	private static final long serialVersionUID = 8698249863871832402L;

	private transient org.hibernate.envers.entities.mapper.relation.lazy.initializor.Initializor<T> initializor;
    protected T delegate;

    protected CollectionProxy() {
    }

    public CollectionProxy(Initializor<T> initializor) {
        this.initializor = initializor;
    }

    protected void checkInit() {
        if (delegate == null) {
            delegate = initializor.initialize();
        }
    }

    public int size() {
        checkInit();
        return delegate.size();
    }

    public boolean isEmpty() {
        checkInit();
        return delegate.isEmpty();
    }

    public boolean contains(Object o) {
        checkInit();
        return delegate.contains(o);
    }

    public Iterator<U> iterator() {
        checkInit();
        return delegate.iterator();
    }

    public Object[] toArray() {
        checkInit();
        return delegate.toArray();
    }

    public <V> V[] toArray(V[] a) {
        checkInit();
        return delegate.toArray(a);
    }

    public boolean add(U o) {
        checkInit();
        return delegate.add(o);
    }

    public boolean remove(Object o) {
        checkInit();
        return delegate.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        checkInit();
        return delegate.containsAll(c);
    }

    public boolean addAll(Collection<? extends U> c) {
        checkInit();
        return delegate.addAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        checkInit();
        return delegate.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        checkInit();
        return delegate.retainAll(c);
    }

    public void clear() {
        checkInit();
        delegate.clear();
    }

    @Override
    public String toString() {
        checkInit();
        return delegate.toString();
    }

    @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
    @Override
    public boolean equals(Object obj) {
        checkInit();
        return delegate.equals(obj);
    }

    @Override
    public int hashCode() {
        checkInit();
        return delegate.hashCode();
    }
}
