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
package org.geotools.factory;

import java.util.HashSet;
import java.util.Set;

class RecursionCheckingHelper {

    private final ThreadLocal<Set> threadLocalSet = new ThreadLocal<Set>();
    
    boolean addAndCheck(Object item) {
        Set set = threadLocalSet.get();
        if(set == null) {
            set = new HashSet<Class<?>>();
            threadLocalSet.set(set);
        }
        return set.add(item);
    }
    
    boolean contains(Object item) {
        Set<Class<?>> set = threadLocalSet.get();
        if(set == null) {
            return false;
        }
        return set.contains(item);
    }
    
    void removeAndCheck(Object item) {
        Set<Class<?>> set = threadLocalSet.get();
        if(set == null) {
            throw new AssertionError(null); // Should never happen.
        } else if(!set.remove(item)) {
            throw new AssertionError(item); // Should never happen.
        } 
        if(set.isEmpty()) {
            threadLocalSet.remove();
        }
    }

}
