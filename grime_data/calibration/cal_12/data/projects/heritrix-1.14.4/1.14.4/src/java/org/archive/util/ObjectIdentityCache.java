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
 *  This file is part of the Heritrix web crawler (crawler.archive.org).
 *
 *  Licensed to the Internet Archive (IA) by one or more individual 
 *  contributors. 
 *
 *  The IA licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.archive.util;

import java.io.Closeable;
import java.util.Set;

/**
 * An object cache for create-once-by-name-and-then-reuse objects. 
 * 
 * Objects are added, but never removed. Subsequent get()s using the 
 * same key will return the exact same object, UNLESS all such objects
 * have been forgotten, in which case a new object MAY be returned. 
 * 
 * This allows implementors (such as ObjectIdentityBdbCache or 
 * CachedBdbMap) to page out (aka 'expunge') instances to
 * persistent storage while they're not being used. However, as long as
 * they are used (referenced), all requests for the same-named object
 * will share a reference to the same object, and the object may be
 * mutated in place without concern for explicitly persisting its
 * state to disk.  
 * 
 * @param <V>
 */
public interface ObjectIdentityCache<K, V> extends Closeable {
    /** get the object under the given key/name */
    public abstract V get(final K key);
    
    /** get the object under the given key/name, using (and remembering)
     * the object supplied by the supplier if no prior mapping exists */
    public abstract V getOrUse(final K key, Supplier<V> supplierOrNull);

    /** force the persistent backend, if any, to be updated with all 
     * live object state */ 
    public abstract void sync();
    
    /** close/release any associated resources */ 
    public abstract void close();
    
    /** count of name-to-object contained */ 
    public abstract int size();

    /** set of all keys */ 
    public abstract Set<K> keySet();
}