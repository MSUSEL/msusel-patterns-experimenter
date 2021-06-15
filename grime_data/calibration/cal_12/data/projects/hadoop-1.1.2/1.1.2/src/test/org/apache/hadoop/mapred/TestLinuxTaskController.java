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

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.LocalDirAllocator;
import org.apache.hadoop.mapred.TaskTracker.LocalStorage;

import junit.framework.TestCase;

public class TestLinuxTaskController extends TestCase {
  private static int INVALID_TASKCONTROLLER_PERMISSIONS = 22;
  private static File testDir = new File(System.getProperty("test.build.data",
      "/tmp"), TestLinuxTaskController.class.getName());
  private static String taskControllerPath = System
      .getProperty(ClusterWithLinuxTaskController.TASKCONTROLLER_PATH);

  protected void setUp() throws Exception {
    testDir.mkdirs();
  }

  protected void tearDown() throws Exception {
    FileUtil.fullyDelete(testDir);
  }

  public static class MyLinuxTaskController extends LinuxTaskController {
    String taskControllerExePath = taskControllerPath + "/task-controller";
  }

  private void validateTaskControllerSetup(TaskController controller,
      boolean shouldFail) throws IOException {
    if (shouldFail) {
      // task controller setup should fail validating permissions.
      Throwable th = null;
      try {
        controller.setup(
            new LocalDirAllocator(JobConf.MAPRED_LOCAL_DIR_PROPERTY),
            new LocalStorage(controller.getConf().getStrings(
                JobConf.MAPRED_LOCAL_DIR_PROPERTY)));
      } catch (IOException ie) {
        th = ie;
      }
      assertNotNull("No exception during setup", th);
      assertTrue("Exception message does not contain exit code"
          + INVALID_TASKCONTROLLER_PERMISSIONS, th.getMessage().contains(
          "with exit code " + INVALID_TASKCONTROLLER_PERMISSIONS));
    } else {
      controller.setup(new LocalDirAllocator(JobConf.MAPRED_LOCAL_DIR_PROPERTY),
          new LocalStorage(controller.getConf().getStrings(
              JobConf.MAPRED_LOCAL_DIR_PROPERTY)));
    }

  }

  public void testTaskControllerGroup() throws Exception {
    if (!ClusterWithLinuxTaskController.isTaskExecPathPassed()) {
      return;
    }
    // cleanup configuration file.
    ClusterWithLinuxTaskController
        .getTaskControllerConfFile(taskControllerPath).delete();
    Configuration conf = new Configuration();
    // create local dirs and set in the conf.
    File mapredLocal = new File(testDir, "mapred/local");
    mapredLocal.mkdirs();
    conf.set(JobConf.MAPRED_LOCAL_DIR_PROPERTY, mapredLocal.toString());

    // setup task-controller without setting any group name
    TaskController controller = new MyLinuxTaskController();
    controller.setConf(conf);
    validateTaskControllerSetup(controller, true);

    // set an invalid group name for the task controller group
    conf.set(ClusterWithLinuxTaskController.TT_GROUP, "invalid");
    // write the task-controller's conf file
    ClusterWithLinuxTaskController.createTaskControllerConf(taskControllerPath,
        conf);
    validateTaskControllerSetup(controller, true);

    conf.set(ClusterWithLinuxTaskController.TT_GROUP,
        ClusterWithLinuxTaskController.taskTrackerSpecialGroup);
    // write the task-controller's conf file
    ClusterWithLinuxTaskController.createTaskControllerConf(taskControllerPath,
        conf);
    validateTaskControllerSetup(controller, false);
  }
}
