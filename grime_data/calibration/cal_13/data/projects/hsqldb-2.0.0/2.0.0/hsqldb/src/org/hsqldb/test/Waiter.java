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

package org.hsqldb.test;

import java.util.Map;
import java.util.HashMap;

/**
 * Single-use synchronization object.
 *
 * Design would be more scalable if there were a separate collection class,
 * instead of the static Waiter.map.  The limitation is acceptable since
 * there's no use case for running multiple Test Runners simultaneously
 * from a single JVM (one Test runner can handle multiple test scripts
 * simultaneously or synchronously).
 *
 * (It would be much work to make the collection non-static, because that
 * would require a refactor of TestUtil with proper OOD).
 */
public class Waiter {
    /*
    static private HashMap<String, Waiter> map =
            new HashMap<String, Waiter>();
    Java 5 */
    static private Map map = new HashMap();
    private String key;
    private boolean notified = false; // resume() method has been called
    private boolean waiting = false;  // a client is waiting (in waitFor()).
    private boolean abort = false;  // Make fail if partner failed

    public boolean isNotified() { return notified; }
    public boolean isWaiting() { return waiting; }

    private Waiter(String key) {
        this.key = key;
        map.put(key, this);
    }

    /**
     * @param enforceSequence  Fail if waitFor() called before resume()
     */
    public synchronized void waitFor(boolean enforceSequence) {
        if (abort)
            throw new RuntimeException("Notifier side failed previously");
        if (notified) {
            if (enforceSequence)
                throw new RuntimeException(
                        "Request to wait on '" + key
                        + "', but this object has already been notified");
            return;
        }
        waiting = true;
        try {
            wait();
        } catch (InterruptedException ie) {
            throw new RuntimeException(
                    "Unexpected interrupted while waiting for '"
                    + key + "'", ie);
        } finally {
            waiting = false;
        }
        map.remove(this);
        if (!notified)
            throw new RuntimeException(
                    "Exiting waitFor() on '" + key
                    + "' even though not 'notified'");
    }

    /**
     * @param enforceSequence  Fail if waitFor() called before resume()
     */
    public synchronized void resume(boolean enforceSequence) {
        if (enforceSequence && !waiting) {
            abort = true;
            throw new RuntimeException("Requested to resume on '"
                    + key + " ', but nothing is waiting for it");
        }
        notified = true;
        notify();
    }

    /**
     * It doesn't matter if the waiter or the waitee runs getWaiter()
     * first.  Whoever requests it first will instantiate it.
     *
     * @return A Waiter instance.  Never returns nul.
     */
    synchronized static public Waiter getWaiter(String key) {
        Waiter waiter = (Waiter) map.get(key);
        if (waiter == null) waiter = new Waiter(key);
        return waiter;
    }
}
