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
package com.gargoylesoftware.htmlunit.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Map implementation delegating all calls to the wrapped <tt>Map</tt> instance. This
 * class is <tt>Serializable</tt>, but serialization will fail if the wrapped map is not
 * itself <tt>Serializable</tt>.
 *
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 * @param <K> the type of the map key
 * @param <V> the type of the value
 */
public class MapWrapper<K, V> implements Map<K, V>, Serializable {

    private static final long serialVersionUID = 2761869317828831102L;

    private Map<K, V> wrappedMap_;

    /**
     * Simple constructor. Needed to allow subclasses to be serializable.
     */
    protected MapWrapper() {
        // Empty.
    }

    /**
     * Initializes the wrapper with its wrapped map.
     * @param map the map to wrap.
     */
    public MapWrapper(final Map<K, V> map) {
        wrappedMap_ = map;
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        wrappedMap_.clear();
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsKey(final Object key) {
        return wrappedMap_.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsValue(final Object value) {
        return wrappedMap_.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    public Set<Entry<K, V>> entrySet() {
        return wrappedMap_.entrySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        return wrappedMap_.equals(o);
    }

    /**
     * {@inheritDoc}
     */
    public V get(final Object key) {
        return wrappedMap_.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return wrappedMap_.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return wrappedMap_.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public Set<K> keySet() {
        return wrappedMap_.keySet();
    }

    /**
     * {@inheritDoc}
     */
    public V put(final K key, final V value) {
        return wrappedMap_.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    public void putAll(final Map< ? extends K, ? extends V> t) {
        wrappedMap_.putAll(t);
    }

    /**
     * {@inheritDoc}
     */
    public V remove(final Object key) {
        return wrappedMap_.remove(key);
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        return wrappedMap_.size();
    }

    /**
     * {@inheritDoc}
     */
    public Collection<V> values() {
        return wrappedMap_.values();
    }
}
