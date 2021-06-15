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
package org.apache.hadoop.io.file.tfile;

/**
 * A nano-second timer.
 */
public class NanoTimer {
  private long last = -1;
  private boolean started = false;
  private long cumulate = 0;

  /**
   * Constructor
   * 
   * @param start
   *          Start the timer upon construction.
   */
  public NanoTimer(boolean start) {
    if (start) this.start();
  }

  /**
   * Start the timer.
   * 
   * Note: No effect if timer is already started.
   */
  public void start() {
    if (!this.started) {
      this.last = System.nanoTime();
      this.started = true;
    }
  }

  /**
   * Stop the timer.
   * 
   * Note: No effect if timer is already stopped.
   */
  public void stop() {
    if (this.started) {
      this.started = false;
      this.cumulate += System.nanoTime() - this.last;
    }
  }

  /**
   * Read the timer.
   * 
   * @return the elapsed time in nano-seconds. Note: If the timer is never
   *         started before, -1 is returned.
   */
  public long read() {
    if (!readable()) return -1;

    return this.cumulate;
  }

  /**
   * Reset the timer.
   */
  public void reset() {
    this.last = -1;
    this.started = false;
    this.cumulate = 0;
  }

  /**
   * Checking whether the timer is started
   * 
   * @return true if timer is started.
   */
  public boolean isStarted() {
    return this.started;
  }

  /**
   * Format the elapsed time to a human understandable string.
   * 
   * Note: If timer is never started, "ERR" will be returned.
   */
  public String toString() {
    if (!readable()) {
      return "ERR";
    }

    return NanoTimer.nanoTimeToString(this.cumulate);
  }

  /**
   * A utility method to format a time duration in nano seconds into a human
   * understandable stirng.
   * 
   * @param t
   *          Time duration in nano seconds.
   * @return String representation.
   */
  public static String nanoTimeToString(long t) {
    if (t < 0) return "ERR";

    if (t == 0) return "0";

    if (t < 1000) {
      return t + "ns";
    }

    double us = (double) t / 1000;
    if (us < 1000) {
      return String.format("%.2fus", us);
    }

    double ms = us / 1000;
    if (ms < 1000) {
      return String.format("%.2fms", ms);
    }

    double ss = ms / 1000;
    if (ss < 1000) {
      return String.format("%.2fs", ss);
    }

    long mm = (long) ss / 60;
    ss -= mm * 60;
    long hh = mm / 60;
    mm -= hh * 60;
    long dd = hh / 24;
    hh -= dd * 24;

    if (dd > 0) {
      return String.format("%dd%dh", dd, hh);
    }

    if (hh > 0) {
      return String.format("%dh%dm", hh, mm);
    }

    if (mm > 0) {
      return String.format("%dm%.1fs", mm, ss);
    }

    return String.format("%.2fs", ss);

    /**
     * StringBuilder sb = new StringBuilder(); String sep = "";
     * 
     * if (dd > 0) { String unit = (dd > 1) ? "days" : "day";
     * sb.append(String.format("%s%d%s", sep, dd, unit)); sep = " "; }
     * 
     * if (hh > 0) { String unit = (hh > 1) ? "hrs" : "hr";
     * sb.append(String.format("%s%d%s", sep, hh, unit)); sep = " "; }
     * 
     * if (mm > 0) { String unit = (mm > 1) ? "mins" : "min";
     * sb.append(String.format("%s%d%s", sep, mm, unit)); sep = " "; }
     * 
     * if (ss > 0) { String unit = (ss > 1) ? "secs" : "sec";
     * sb.append(String.format("%s%.3f%s", sep, ss, unit)); sep = " "; }
     * 
     * return sb.toString();
     */
  }

  private boolean readable() {
    return this.last != -1;
  }

  /**
   * Simple tester.
   * 
   * @param args
   */
  public static void main(String[] args) {
    long i = 7;

    for (int x = 0; x < 20; ++x, i *= 7) {
      System.out.println(NanoTimer.nanoTimeToString(i));
    }
  }
}

