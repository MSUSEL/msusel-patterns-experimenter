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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import org.apache.hadoop.mapred.TaskTracker.TaskInProgress;
import org.apache.hadoop.mapred.TaskTracker.TaskLauncher;
import org.apache.hadoop.mapreduce.TaskType;
import org.junit.Test;

/**
 * Tests {@link TaskLauncher}
 */
public class TestTaskLauncher {
  private static int expectedLaunchAttemptId = 1;

  private static class MyTaskTracker extends TaskTracker {
    // override startNewTask just to set the runState,
    // not to launch the task really
    @Override
    void startNewTask(TaskInProgress tip) {
      assertEquals(expectedLaunchAttemptId, tip.getTask().getTaskID().getId());
      tip.getStatus().setRunState(TaskStatus.State.RUNNING);
    }
  }

  /**
   * Tests the case "task waiting to be launched is killed externally".
   *
   * Launches a task which will wait for ever to get slots. Kill the
   * task and see if launcher is able to come out of the wait and pickup a
   * another task.
   *
   * @throws IOException
   */
  @Test
  public void testExternalKillForLaunchTask() throws IOException {
    // setup a TaskTracker
    JobConf ttConf = new JobConf();
    ttConf.setInt("mapred.tasktracker.map.tasks.maximum", 4);
    TaskTracker tt = new MyTaskTracker();
    tt.runningJobs = new TreeMap<JobID, TaskTracker.RunningJob>();
    tt.runningTasks = new LinkedHashMap<TaskAttemptID, TaskInProgress>();
    tt.setIndexCache(new IndexCache(ttConf));
    tt.setTaskMemoryManagerEnabledFlag();

    // start map-task launcher with four slots
    TaskLauncher mapLauncher = tt.new TaskLauncher(TaskType.MAP, 4);
    mapLauncher.start();

    // launch a task which requires five slots
    String jtId = "test";
    TaskAttemptID attemptID = new TaskAttemptID(jtId, 1, true, 0, 0);
    Task task = new MapTask(null, attemptID, 0, null, 5);
    mapLauncher.addToTaskQueue(new LaunchTaskAction(task));
    // verify that task is added to runningTasks
    TaskInProgress killTip = tt.runningTasks.get(attemptID);
    assertNotNull(killTip);

    // wait for a while for launcher to pick up the task
    // this loop waits atmost for 30 seconds
    for (int i = 0; i < 300; i++) {
      if (mapLauncher.getNumWaitingTasksToLaunch() == 0) {
        break;
      }
      UtilsForTests.waitFor(100);
    }
    assertEquals("Launcher didnt pick up the task " + attemptID + "to launch",
        0, mapLauncher.getNumWaitingTasksToLaunch());

    // Now, that launcher has picked up the task, it waits until all five slots
    // are available. i.e. it waits for-ever
    // lets kill the task so that map launcher comes out
    tt.processKillTaskAction(new KillTaskAction(attemptID));
    assertEquals(TaskStatus.State.KILLED, killTip.getRunState());

    // launch another attempt which requires only one slot
    TaskAttemptID runningAttemptID = new TaskAttemptID(jtId, 1, true,
        0, expectedLaunchAttemptId);
    mapLauncher.addToTaskQueue(new LaunchTaskAction(new MapTask(null,
        runningAttemptID, 0, null, 1)));
    TaskInProgress runningTip = tt.runningTasks.get(runningAttemptID);
    assertNotNull(runningTip);

    // wait for a while for the task to be launched
    // this loop waits at most for 30 seconds
    for (int i = 0; i < 300; i++) {
      if (runningTip.getRunState().equals(TaskStatus.State.RUNNING)) {
        break;
      }
      UtilsForTests.waitFor(100);
    }

    // verify that the task went to running
    assertEquals(TaskStatus.State.RUNNING, runningTip.getRunState());
  }

}
