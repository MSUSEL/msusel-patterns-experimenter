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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.examples.SleepJob;
import org.junit.Test;

public class TestTaskTrackerInstrumentation {

  @Test
  public void testStartup() throws IOException {
    MiniMRCluster mr = null;
    try {
      JobConf jtConf = new JobConf();
      // Set a bad class.
      jtConf.set("mapred.tasktracker.instrumentation",
          "org.nowhere.FUBAR");
      mr = new MiniMRCluster(1, "file:///", 1, null, null, jtConf);
      // Assert that the TT fell back to default class.
      TaskTracker tt = mr.getTaskTrackerRunner(0).getTaskTracker();
      assertEquals(TaskTrackerInstrumentation.create(tt).getClass(),
          mr.getTaskTrackerRunner(0).getTaskTracker()
          .getTaskTrackerInstrumentation().getClass());
    } finally {
      mr.shutdown();
    }
  }

  @Test
  public void testSlots() throws IOException {
    MiniMRCluster mr = null;
    try {
      JobConf jtConf = new JobConf();
      jtConf.set("mapred.tasktracker.instrumentation",
          MyTaskTrackerMetricsInst.class.getName());
      mr = new MiniMRCluster(1, "file:///", 1, null, null, jtConf);
      MyTaskTrackerMetricsInst instr = (MyTaskTrackerMetricsInst)
        mr.getTaskTrackerRunner(0).getTaskTracker()
        .getTaskTrackerInstrumentation();

      JobConf conf = mr.createJobConf();
      SleepJob job = new SleepJob();
      job.setConf(conf);
      int numMapTasks = 3;
      int numReduceTasks = 2;
      job.run(numMapTasks, numReduceTasks, 1, 1, 1, 1);

      synchronized (instr) {
        // 5 regular tasks + 2 setup/cleanup tasks.
        assertEquals(7, instr.complete);
        assertEquals(7, instr.end);
        assertEquals(7, instr.launch);
      }
    } finally {
      if (mr != null) {
        mr.shutdown();
      }
    }
  }

  static class MyTaskTrackerMetricsInst extends TaskTrackerInstrumentation  {
    public int complete = 0;
    public int launch = 0;
    public int end = 0;

    public MyTaskTrackerMetricsInst(TaskTracker tracker) {
      super(tracker);
    }

    @Override
    public void completeTask(TaskAttemptID t) {
      this.complete++;
    }

    @Override
    public void reportTaskLaunch(TaskAttemptID t, File stdout, File stderr) {
      this.launch++;
    }

    @Override
    public void reportTaskEnd(TaskAttemptID t) {
      this.end++;
    }
  }
}
