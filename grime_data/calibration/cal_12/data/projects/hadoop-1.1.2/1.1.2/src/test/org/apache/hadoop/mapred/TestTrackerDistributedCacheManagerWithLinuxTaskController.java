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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.LocalDirAllocator;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.ClusterWithLinuxTaskController.MyLinuxTaskController;
import org.apache.hadoop.mapred.TaskTracker.LocalStorage;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.filecache.TestTrackerDistributedCacheManager;

/**
 * Test the DistributedCacheManager when LinuxTaskController is used.
 * 
 */
public class TestTrackerDistributedCacheManagerWithLinuxTaskController extends
    TestTrackerDistributedCacheManager {

  private File configFile;

  private static final Log LOG =
      LogFactory
          .getLog(TestTrackerDistributedCacheManagerWithLinuxTaskController.class);

  @Override
  protected void setUp()
      throws IOException, InterruptedException {

    if (!ClusterWithLinuxTaskController.shouldRun()) {
      return;
    }

    TEST_ROOT_DIR =
        new File(System.getProperty("test.build.data", "/tmp"),
            TestTrackerDistributedCacheManagerWithLinuxTaskController.class
                .getSimpleName()).getAbsolutePath();

    super.setUp();

    taskController = new MyLinuxTaskController();
    String path =
        System.getProperty(ClusterWithLinuxTaskController.TASKCONTROLLER_PATH);
    String execPath = path + "/task-controller";
    ((MyLinuxTaskController)taskController).setTaskControllerExe(execPath);
    taskController.setConf(conf);
    UtilsForTests.setupTC(taskController, localDirAllocator,
        conf.getStrings(JobConf.MAPRED_LOCAL_DIR_PROPERTY));
  }

  @Override
  protected void refreshConf(Configuration conf) throws IOException {
    super.refreshConf(conf);
    String path =
      System.getProperty(ClusterWithLinuxTaskController.TASKCONTROLLER_PATH);
    configFile =
      ClusterWithLinuxTaskController.createTaskControllerConf(path, conf);
   
  }

  @Override
  protected void tearDown()
      throws IOException {
    if (!ClusterWithLinuxTaskController.shouldRun()) {
      return;
    }
    if (configFile != null) {
      configFile.delete();
    }
    super.tearDown();
  }

  @Override
  protected boolean canRun() {
    return ClusterWithLinuxTaskController.shouldRun();
  }

  @Override
  protected String getJobOwnerName() {
    String ugi =
        System.getProperty(ClusterWithLinuxTaskController.TASKCONTROLLER_UGI);
    String userName = ugi.split(",")[0];
    return userName;
  }

  @Override
  protected void checkFilePermissions(Path[] localCacheFiles)
      throws IOException {
    String userName = getJobOwnerName();
    String filePermissions = UserGroupInformation.getLoginUser()
        .getShortUserName().equals(userName) ? "-rwxrwx---" : "-r-xrwx---";

    for (Path p : localCacheFiles) {
      // First make sure that the cache file has proper permissions.
      TestTaskTrackerLocalization.checkFilePermissions(p.toUri().getPath(),
          filePermissions, userName,
          ClusterWithLinuxTaskController.taskTrackerSpecialGroup);
      // Now. make sure that all the path components also have proper
      // permissions.
      checkPermissionOnPathComponents(p.toUri().getPath(), userName);
    }

  }

  /**
   * @param cachedFilePath
   * @param userName
   * @throws IOException
   */
  private void checkPermissionOnPathComponents(String cachedFilePath,
      String userName)
      throws IOException {
    // The trailing distcache/file/... string
    String trailingStringForFirstFile =
        cachedFilePath.replaceFirst(ROOT_MAPRED_LOCAL_DIR.getAbsolutePath()
            + Path.SEPARATOR + "0_[0-" + (numLocalDirs - 1) + "]"
            + Path.SEPARATOR + TaskTracker.getPrivateDistributedCacheDir(userName),
            "");
    LOG.info("Trailing path for cacheFirstFile is : "
        + trailingStringForFirstFile);
    // The leading mapred.local.dir/0_[0-n]/taskTracker/$user string.
    String leadingStringForFirstFile =
        cachedFilePath.substring(0, cachedFilePath
            .lastIndexOf(trailingStringForFirstFile));
    LOG.info("Leading path for cacheFirstFile is : "
        + leadingStringForFirstFile);

    String dirPermissions = UserGroupInformation.getLoginUser()
        .getShortUserName().equals(userName) ? "drwxrws---" : "dr-xrws---";

    // Now check path permissions, starting with cache file's parent dir.
    File path = new File(cachedFilePath).getParentFile();
    while (!path.getAbsolutePath().equals(leadingStringForFirstFile)) {
      TestTaskTrackerLocalization.checkFilePermissions(path.getAbsolutePath(),
          dirPermissions, userName, 
          ClusterWithLinuxTaskController.taskTrackerSpecialGroup);
      path = path.getParentFile();
    }
  }
}
