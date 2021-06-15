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

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Trivial all-in-memory object cache, using a single internal
 * ConcurrentHashMap.
 * 
 * @contributor gojomo
 * @param <V>
 */
public class ObjectIdentityMemCache<V> 
implements ObjectIdentityCache<String,V> {
    ConcurrentHashMap<String, V> map; 
    
    public ObjectIdentityMemCache() {
        map = new ConcurrentHashMap<String, V>();
    }
    
    public ObjectIdentityMemCache(int cap, float load, int conc) {
        map = new ConcurrentHashMap<String, V>(cap, load, conc);
    }

    public void close() {
        // do nothing
    }

    public V get(String key) {
        return map.get(key); 
    }

    public V getOrUse(String key, Supplier<V> supplierOrNull) {
        V val = map.get(key); 
        if (val==null && supplierOrNull!=null) {
            val = supplierOrNull.get(); 
            V prevVal = map.putIfAbsent(key, val);
            if(prevVal!=null) {
                val = prevVal; 
            }
        }
        return val; 
    }

    public int size() {
        return map.size();
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public void sync() {
        // do nothing
    }
}
