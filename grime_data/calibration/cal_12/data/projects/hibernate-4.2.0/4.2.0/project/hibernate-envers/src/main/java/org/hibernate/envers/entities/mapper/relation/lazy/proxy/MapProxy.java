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
import java.util.Map;
import java.util.Set;

import org.hibernate.envers.entities.mapper.relation.lazy.initializor.Initializor;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class MapProxy<K, V> implements Map<K, V>, Serializable {
    private static final long serialVersionUID = 8418037541773074646L;

    private transient Initializor<Map<K, V>> initializor;
    protected Map<K, V> delegate;

    public MapProxy() {
    }

    public MapProxy(org.hibernate.envers.entities.mapper.relation.lazy.initializor.Initializor<Map<K, V>> initializor) {
        this.initializor = initializor;
    }

    private void checkInit() {
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

    public boolean containsKey(Object o) {
        checkInit();
        return delegate.containsKey(o);
    }

    public boolean containsValue(Object o) {
        checkInit();
        return delegate.containsValue(o);
    }

    public V get(Object o) {
        checkInit();
        return delegate.get(o);
    }

    public V put(K k, V v) {
        checkInit();
        return delegate.put(k, v);
    }

    public V remove(Object o) {
        checkInit();
        return delegate.remove(o);
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        checkInit();
        delegate.putAll(map);
    }

    public void clear() {
        checkInit();
        delegate.clear();
    }

    public Set<K> keySet() {
        checkInit();
        return delegate.keySet();
    }

    public Collection<V> values() {
        checkInit();
        return delegate.values();
    }

    public Set<Entry<K, V>> entrySet() {
        checkInit();
        return delegate.entrySet();
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
