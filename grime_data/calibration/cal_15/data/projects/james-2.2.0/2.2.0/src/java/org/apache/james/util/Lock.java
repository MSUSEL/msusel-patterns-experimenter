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
package org.apache.james.util;

import java.util.Hashtable;

/**
 * Provides Lock functionality
 *
 */
public class Lock {
    /**
     * An internal hash table of keys to locks
     */
    private Hashtable locks = new Hashtable();

    /**
     * Check to see if the object is locked
     *
     * @param key the Object on which to check the lock
     * @return true if the object is locked, false otherwise
     */
    public boolean isLocked(final Object key) {
        return (locks.get(key) != null);
    }

    /**
     * Check to see if we can lock on a given object.
     *
     * @param key the Object on which to lock
     * @return true if the calling thread can lock, false otherwise
     */
    public boolean canI(final Object key) {
        Object o = locks.get( key );

        if (null == o || o == this.getCallerId()) {
            return true;
        }

        return false;
    }

    /**
     * Lock on a given object.
     *
     * @param key the Object on which to lock
     * @return true if the locking was successful, false otherwise
     */
    public boolean lock(final Object key) {
        Object theLock;

        synchronized(this) {
            theLock = locks.get(key);

            if (null == theLock) {
                locks.put(key, getCallerId());
                return true;
            } else if (getCallerId() == theLock) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Release the lock on a given object.
     *
     * @param key the Object on which the lock is held
     * @return true if the unlocking was successful, false otherwise
     */
    public boolean unlock(final Object key) {
        Object theLock;
        synchronized (this) {
            theLock = locks.get(key);

            if (null == theLock) {
                return true;
            } else if (getCallerId() == theLock) {
                locks.remove(key);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Private helper method to abstract away caller ID.
     *
     * @return the id of the caller (i.e. the Thread reference)
     */
    private Object getCallerId() {
        return Thread.currentThread();
    }
}
