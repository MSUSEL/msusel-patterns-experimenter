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

import junit.framework.TestCase;

public class TestTaskStatus extends TestCase {

  public void testMapTaskStatusStartAndFinishTimes() {
    checkTaskStatues(true);
  }

  public void testReduceTaskStatusStartAndFinishTimes() {
    checkTaskStatues(false);
  }

  /**
   * Private utility method which ensures uniform testing of newly created
   * TaskStatus object.
   * 
   * @param isMap
   *          true to test map task status, false for reduce.
   */
  private void checkTaskStatues(boolean isMap) {

    TaskStatus status = null;
    if (isMap) {
      status = new MapTaskStatus();
    } else {
      status = new ReduceTaskStatus();
    }
    long currentTime = System.currentTimeMillis();
    // first try to set the finish time before
    // start time is set.
    status.setFinishTime(currentTime);
    assertEquals("Finish time of the task status set without start time", 0,
        status.getFinishTime());
    // Now set the start time to right time.
    status.setStartTime(currentTime);
    assertEquals("Start time of the task status not set correctly.",
        currentTime, status.getStartTime());
    // try setting wrong start time to task status.
    long wrongTime = -1;
    status.setStartTime(wrongTime);
    assertEquals(
        "Start time of the task status is set to wrong negative value",
        currentTime, status.getStartTime());
    // finally try setting wrong finish time i.e. negative value.
    status.setFinishTime(wrongTime);
    assertEquals("Finish time of task status is set to wrong negative value",
        0, status.getFinishTime());
    status.setFinishTime(currentTime);
    assertEquals("Finish time of the task status not set correctly.",
        currentTime, status.getFinishTime());
  }
}
