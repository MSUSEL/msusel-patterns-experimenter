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
package com.itextpdf.rups.model;

import javax.swing.SwingUtilities;

/**
 * Allows you to perform long lasting tasks in background.
 * If we ever move to Java 6, we should use the SwingWorker class
 * (included in the JDK) instead of this custom Event Dispatching
 * code.
 */

public abstract class BackgroundTask {

	/**
     * Inner class that holds the reference to the thread.
     */
    private static class ThreadWrapper {
        private Thread thread;
        ThreadWrapper(Thread t) { thread = t; }
        synchronized Thread get() { return thread; }
        synchronized void clear() { thread = null; }
    }

	/** A wrapper for the tread that executes a time-consuming task. */
    private ThreadWrapper thread;

    /**
     * Starts a thread.
     * Executes the time-consuming task in the construct method;
     * finally calls the finish().
     */
    public BackgroundTask() {
        final Runnable doFinished = new Runnable() {
           public void run() { finished(); }
        };

        Runnable doConstruct = new Runnable() {
            public void run() {
                try {
                	doTask();
                }
                finally {
                    thread.clear();
                }
                SwingUtilities.invokeLater(doFinished);
            }
        };
        Thread t = new Thread(doConstruct);
        thread = new ThreadWrapper(t);
    }

    /**
     * Implement this class; the time-consuming task will go here.
     */
    public abstract void doTask();

    /**
     * Starts the thread.
     */
    public void start() {
        Thread t = thread.get();
        if (t != null) {
            t.start();
        }
    }

    /**
     * Forces the thread to stop what it's doing.
     */
    public void interrupt() {
        Thread t = thread.get();
        if (t != null) {
            t.interrupt();
        }
        thread.clear();
    }

    /**
     * Called on the event dispatching thread once the
     * construct method has finished its task.
     */
    public void finished() {
    }
}
