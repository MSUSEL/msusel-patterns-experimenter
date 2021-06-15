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
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.hibernate.envers.entities.mapper.relation.lazy.initializor.Initializor;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class SortedMapProxy<K, V> implements SortedMap<K, V>, Serializable {
    private static final long serialVersionUID = 2645817952901452375L;

    private transient Initializor<SortedMap<K, V>> initializor;
    protected SortedMap<K, V> delegate;

    public SortedMapProxy() {
    }

    public SortedMapProxy(org.hibernate.envers.entities.mapper.relation.lazy.initializor.Initializor<SortedMap<K, V>> initializor) {
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

    public Comparator<? super K> comparator() {
        checkInit();
        return delegate.comparator();
    }

    public SortedMap<K, V> subMap(K k, K k1) {
        checkInit();
        return delegate.subMap(k, k1);
    }

    public SortedMap<K, V> headMap(K k) {
        checkInit();
        return delegate.headMap(k);
    }

    public SortedMap<K, V> tailMap(K k) {
        checkInit();
        return delegate.tailMap(k);
    }

    public K firstKey() {
        checkInit();
        return delegate.firstKey();
    }

    public K lastKey() {
        checkInit();
        return delegate.lastKey();
    }

    @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
    public boolean equals(Object o) {
        checkInit();
        return delegate.equals(o);
    }

    public int hashCode() {
        checkInit();
        return delegate.hashCode();
    }
}
