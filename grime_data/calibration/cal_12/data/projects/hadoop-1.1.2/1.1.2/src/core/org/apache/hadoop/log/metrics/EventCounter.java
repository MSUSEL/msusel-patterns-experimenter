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
package org.apache.hadoop.log.metrics;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

/**
 * A log4J Appender that simply counts logging events in three levels:
 * fatal, error and warn. The class name is used in log4j.properties
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public class EventCounter extends AppenderSkeleton {

  private static final int FATAL = 0;
  private static final int ERROR = 1;
  private static final int WARN = 2;
  private static final int INFO = 3;

  private static class EventCounts {

    private final long[] counts = {0, 0, 0, 0};

    private synchronized void incr(int i) {
      ++counts[i];
    }

    private synchronized long get(int i) {
      return counts[i];
    }
  }

  private static EventCounts counts = new EventCounts();

  @InterfaceAudience.Private
  public static long getFatal() {
    return counts.get(FATAL);
  }

  @InterfaceAudience.Private
  public static long getError() {
    return counts.get(ERROR);
  }

  @InterfaceAudience.Private
  public static long getWarn() {
    return counts.get(WARN);
  }

  @InterfaceAudience.Private
  public static long getInfo() {
    return counts.get(INFO);
  }

  @Override
  public void append(LoggingEvent event) {
    Level level = event.getLevel();
    if (level == Level.INFO) {
      counts.incr(INFO);
    }
    else if (level == Level.WARN) {
      counts.incr(WARN);
    }
    else if (level == Level.ERROR) {
      counts.incr(ERROR);
    }
    else if (level == Level.FATAL) {
      counts.incr(FATAL);
    }

  }

  @Override
  public void close() {
  }

  @Override
  public boolean requiresLayout() {
    return false;
  }
}
