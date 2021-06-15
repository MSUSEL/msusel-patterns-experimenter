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

package org.hsqldb.lib;

/**
 * Provides the programatic analog of a physical stop watch. <p>
 *
 * The watch can be started, stopped and zeroed and can be queried for
 * elapsed running time.  The watch accumulates elapsed time over starts
 * and stops such that only the time actually spent running is recorded.
 * If the watch is zeroed, then the accumulated time is discarded and
 * the watch starts again with zero acumulated time. <p>
 *
 * @author boucherb@users
 * @version 1.7.2
 * @since 1.7.2
 */
public class StopWatch {

    /**
     * The last time this object made the transition
     * from stopped to running state, as reported
     * by System.currentTimeMillis().
     */
    private long startTime;
    private long lastStart;

    /**
     * The accumulated running time of this object since
     * it was last zeroed.
     */
    private long total;

    /** Flags if this object is started or stopped. */
    boolean running = false;

    /** Creates, zeros, and starts a new StopWatch */
    public StopWatch() {
        this(true);
    }

    /** Creates, zeros, and starts a new StopWatch */
    public StopWatch(boolean start) {

        if (start) {
            start();
        }
    }

    /**
     * Retrieves the accumulated time this object has spent running since
     * it was last zeroed.
     * @return the accumulated time this object has spent running since
     * it was last zeroed.
     */
    public long elapsedTime() {

        if (running) {
            return total + System.currentTimeMillis() - startTime;
        } else {
            return total;
        }
    }

    /**
     * Retrieves the accumulated time this object has spent running since
     * it was last started.
     * @return the accumulated time this object has spent running since
     * it was last started.
     */
    public long currentElapsedTime() {

        if (running) {
            return System.currentTimeMillis() - startTime;
        } else {
            return 0;
        }
    }

    /** Zeros accumulated running time and restarts this object. */
    public void zero() {

        total = 0;

        start();
    }

    /**
     * Ensures that this object is in the running state.  If this object is not
     * running, then the call has the effect of setting the <code>startTime</code>
     * attribute to the current value of System.currentTimeMillis() and setting
     * the <code>running</code> attribute to <code>true</code>.
     */
    public void start() {
        startTime = System.currentTimeMillis();
        running   = true;
    }

    /**
     * Ensures that this object is in the stopped state.  If this object is
     * in the running state, then this has the effect of adding to the
     * <code>total</code> attribute the elapsed time since the last transition
     * from stopped to running state and sets the <code>running</code> attribute
     * to false. If this object is not in the running state, this call has no
     * effect.
     */
    public void stop() {

        if (running) {
            total   += System.currentTimeMillis() - startTime;
            running = false;
        }
    }

    public void mark() {
        stop();
        start();
    }

    /**
     * Retrieves prefix + " in " + elapsedTime() + " ms."
     * @param prefix The string to use as a prefix
     * @return prefix + " in " + elapsedTime() + " ms."
     */
    public String elapsedTimeToMessage(String prefix) {
        return prefix + " in " + elapsedTime() + " ms.";
    }

    /**
     * Retrieves prefix + " in " + elapsedTime() + " ms."
     * @param prefix The string to use as a prefix
     * @return prefix + " in " + elapsedTime() + " ms."
     */
    public String currentElapsedTimeToMessage(String prefix) {
        return prefix + " in " + currentElapsedTime() + " ms.";
    }

    /**
     * Retrieves the internal state of this object, as a String.
     *
     * The retreived value is:
     *
     * <pre>
     *    super.toString() +
     *    "[running=" +
     *    running +
     *    ", startTime=" +
     *    startTime +
     *    ", total=" +
     *    total + "]";
     * </pre>
     * @return the state of this object, as a String
     */
    public String toString() {
        return super.toString() + "[running=" + running + ", startTime="
               + startTime + ", total=" + total + "]";
    }
}
