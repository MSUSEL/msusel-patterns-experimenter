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
package org.apache.hadoop.mapred;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.FileAppender;
import org.apache.log4j.spi.LoggingEvent;

/**
 * A simple log4j-appender for the task child's 
 * map-reduce system logs.
 * 
 */
public class TaskLogAppender extends FileAppender {
  private String taskId; //taskId should be managed as String rather than TaskID object
  //so that log4j can configure it from the configuration(log4j.properties). 
  private int maxEvents;
  private Queue<LoggingEvent> tail = null;
  private boolean isCleanup;

  @Override
  public void activateOptions() {
    synchronized (this) {
      if (maxEvents > 0) {
        tail = new LinkedList<LoggingEvent>();
      }
      setFile(TaskLog.getTaskLogFile(TaskAttemptID.forName(taskId),
          isCleanup, TaskLog.LogName.SYSLOG).toString());
      setAppend(true);
      super.activateOptions();
    }
  }
  
  @Override
  public void append(LoggingEvent event) {
    synchronized (this) {
      if (tail == null) {
        super.append(event);
      } else {
        if (tail.size() >= maxEvents) {
          tail.remove();
        }
        tail.add(event);
      }
    }
  }
  
  public void flush() {
    qw.flush();
  }

  @Override
  public synchronized void close() {
    if (tail != null) {
      for(LoggingEvent event: tail) {
        super.append(event);
      }
    }
    super.close();
  }

  /**
   * Getter/Setter methods for log4j.
   */
  
  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  private static final int EVENT_SIZE = 100;
  
  public long getTotalLogFileSize() {
    return maxEvents * EVENT_SIZE;
  }

  public void setTotalLogFileSize(long logSize) {
    maxEvents = (int) logSize / EVENT_SIZE;
  }

  /**
   * Set whether the task is a cleanup attempt or not.
   * 
   * @param isCleanup
   *          true if the task is cleanup attempt, false otherwise.
   */
  public void setIsCleanup(boolean isCleanup) {
    this.isCleanup = isCleanup;
  }

  /**
   * Get whether task is cleanup attempt or not.
   * 
   * @return true if the task is cleanup attempt, false otherwise.
   */
  public boolean getIsCleanup() {
    return isCleanup;
  }
}
