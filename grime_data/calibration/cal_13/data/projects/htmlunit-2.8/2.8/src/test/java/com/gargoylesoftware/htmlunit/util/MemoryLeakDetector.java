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

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>A memory leak detector which can be used during testing.</p>
 *
 * <p>Based on <a href="http://blogs.ilog.com/jviews/2008/05/05/unit-testing-memory-leaks/">this</a>.</p>
 *
 * @version $Revision: 5301 $
 * @author Daniel Gredler
 */
public class MemoryLeakDetector {

    /** Weak references to the objects being tracked by the detector. */
    private Map<String , WeakReference<Object>> map_ = new HashMap<String , WeakReference<Object>>();

    /**
     * Registers the specified object with the memory leak detector. Once an object has been registered
     * with the detector via this method, {@link #canBeGCed(String)} may be called with the specified
     * ID in order to find out whether or not the registered object can be garbage collected.
     *
     * @param id the unique ID under which to register the specified object
     * @param o the object to be tracked
     */
    public void register(final String id, final Object o) {
        if (map_.containsKey(id)) {
            throw new IllegalArgumentException("There is already an object registered with ID '" + id + "': " + o);
        }
        map_.put(id, new WeakReference<Object>(o));
    }

    /**
     * Returns <tt>true</tt> if the object registered with the specified ID can be garbage collected.
     *
     * @param id the ID corresponding to the object to check
     * @return <tt>true</tt> if the object registered with the specified ID can be garbage collected
     */
    public boolean canBeGCed(final String id) {
        final WeakReference<Object> ref = map_.get(id);
        if (ref == null) {
            throw new IllegalArgumentException("No object registered with ID '" + id + "'.");
        }
        gc();
        return ref.get() == null;
    }

    /**
     * Does a best effort at getting unreferenced objects garbage collected.
     */
    private void gc() {
        final Runtime rt = Runtime.getRuntime();
        for (int i = 0; i < 3; i++) {
            allocateMemory((int) (2e6));
            for (int j = 0; j < 3; j++) {
                rt.gc();
            }
        }
        rt.runFinalization();
        try {
            Thread.sleep(50);
        }
        catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allocates a byte array of the specified size and messes around with it just to make sure
     * that the allocation doesn't get optimized away.
     *
     * @param bytes the size of the byte array to create
     */
    private void allocateMemory(final int bytes) {
        final byte[] big = new byte[bytes];
        // Fight against clever compilers/JVMs that may not allocate
        // unless we actually use the elements of the array.
        int total = 0;
        for (int i = 0; i < 10; i++) {
            // We don't touch all the elements, would take too long.
            if (i % 2 == 0) {
                total += big[i];
            }
            else {
                total -= big[i];
            }
        }
    }

}
