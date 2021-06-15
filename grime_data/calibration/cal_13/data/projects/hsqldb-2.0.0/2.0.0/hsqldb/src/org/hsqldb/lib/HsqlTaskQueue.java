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
 * Provides very simple queued execution of Runnable objects in a background
 * thread. The underlying queue is an HsqlDeque instance, an array-based
 * circular queue implementation with automatic capacity expansion.
 *
 * @author boucherb@users
 * @version 1.7.2
 * @since 1.7.2
 */
public class HsqlTaskQueue {

    /** The thread used to process commands */
    protected Thread taskRunnerThread;

    /** Special queue element to signal termination */
    protected static final Runnable SHUTDOWNTASK = new Runnable() {
        public void run() {}
    };

    /**
     * true if thread should shut down after processing current task.
     *
     * Once set true, stays true forever
     */
    protected volatile boolean isShutdown;

    public synchronized Thread getTaskRunnerThread() {
        return taskRunnerThread;
    }

    protected synchronized void clearThread() {
        taskRunnerThread = null;
    }

    protected final HsqlDeque queue = new HsqlDeque();

    protected class TaskRunner implements Runnable {

        public void run() {

            Runnable task;

            try {
                while (!isShutdown) {
                    synchronized (queue) {
                        task = (Runnable) queue.getFirst();
                    }

                    if (task == SHUTDOWNTASK) {
                        isShutdown = true;

                        synchronized (queue) {
                            queue.clear();
                        }

                        break;
                    } else if (task != null) {
                        task.run();

                        task = null;
                    } else {
                        break;
                    }
                }
            } finally {
                clearThread();
            }
        }
    }

    protected final TaskRunner taskRunner = new TaskRunner();

    public HsqlTaskQueue() {}

    public boolean isShutdown() {
        return isShutdown;
    }

    public synchronized void restart() {

        if (taskRunnerThread == null &&!isShutdown) {
            taskRunnerThread = new Thread(taskRunner);

            taskRunnerThread.start();
        }
    }

    public void execute(Runnable command) throws RuntimeException {

        if (!isShutdown) {
            synchronized (queue) {
                queue.addLast(command);
            }

            restart();
        }
    }

    public synchronized void shutdownAfterQueued() {

        if (!isShutdown) {
            synchronized (queue) {
                queue.addLast(SHUTDOWNTASK);
            }
        }
    }

    public synchronized void shutdownAfterCurrent() {

        isShutdown = true;

        synchronized (queue) {
            queue.clear();
            queue.addLast(SHUTDOWNTASK);
        }
    }

    public synchronized void shutdownImmediately() {

        isShutdown = true;

        if (taskRunnerThread != null) {
            taskRunnerThread.interrupt();
        }

        synchronized (queue) {
            queue.clear();
            queue.addLast(SHUTDOWNTASK);
        }
    }
}
