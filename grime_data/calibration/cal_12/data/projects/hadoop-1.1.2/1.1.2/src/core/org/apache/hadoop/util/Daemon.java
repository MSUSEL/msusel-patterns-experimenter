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
package org.apache.hadoop.util;

/** A thread that has called {@link Thread#setDaemon(boolean) } with true.*/
public class Daemon extends Thread {

  {
    setDaemon(true);                              // always a daemon
  }

  Runnable runnable = null;
  /** Construct a daemon thread. */
  public Daemon() {
    super();
  }

  /** Construct a daemon thread. */
  public Daemon(Runnable runnable) {
    super(runnable);
    this.runnable = runnable;
    this.setName(((Object)runnable).toString());
  }

  /** Construct a daemon thread to be part of a specified thread group. */
  public Daemon(ThreadGroup group, Runnable runnable) {
    super(group, runnable);
    this.runnable = runnable;
    this.setName(((Object)runnable).toString());
  }

  public Runnable getRunnable() {
    return runnable;
  }
}
