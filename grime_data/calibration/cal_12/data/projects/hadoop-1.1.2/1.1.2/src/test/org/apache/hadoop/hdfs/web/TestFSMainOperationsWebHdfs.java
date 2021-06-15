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
package org.apache.hadoop.hdfs.web;


import static org.apache.hadoop.fs.FileSystemTestHelper.exists;
import static org.apache.hadoop.fs.FileSystemTestHelper.getTestRootPath;

import java.io.IOException;
import java.net.URI;
import java.security.PrivilegedExceptionAction;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSMainOperationsBaseTest;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.server.datanode.web.resources.DatanodeWebHdfsMethods;
import org.apache.hadoop.hdfs.web.resources.ExceptionHandler;
import org.apache.hadoop.security.AccessControlException;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.log4j.Level;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestFSMainOperationsWebHdfs extends FSMainOperationsBaseTest {
  {
    ((Log4JLogger)ExceptionHandler.LOG).getLogger().setLevel(Level.ALL);
    ((Log4JLogger)DatanodeWebHdfsMethods.LOG).getLogger().setLevel(Level.ALL);
  }

  private static MiniDFSCluster cluster = null;
  private static Path defaultWorkingDirectory;

  @BeforeClass
  public static void setupCluster() {
    final Configuration conf = new Configuration();
    conf.setBoolean(DFSConfigKeys.DFS_WEBHDFS_ENABLED_KEY, true);
    try {
      cluster = new MiniDFSCluster(conf, 2, true, null);
      cluster.waitActive();

      //change root permission to 777
      cluster.getFileSystem().setPermission(
          new Path("/"), new FsPermission((short)0777));

      final String uri = WebHdfsFileSystem.SCHEME  + "://"
          + conf.get("dfs.http.address");

      //get file system as a non-superuser
      final UserGroupInformation current = UserGroupInformation.getCurrentUser();
      final UserGroupInformation ugi = UserGroupInformation.createUserForTesting(
          current.getShortUserName() + "x", new String[]{"user"});
      fSys = ugi.doAs(new PrivilegedExceptionAction<FileSystem>() {
        @Override
        public FileSystem run() throws Exception {
          return FileSystem.get(new URI(uri), conf);
        }
      });

      defaultWorkingDirectory = fSys.getWorkingDirectory();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @AfterClass
  public static void shutdownCluster() {
    if (cluster != null) {
      cluster.shutdown();
      cluster = null;
    }
  }

  @Override
  protected Path getDefaultWorkingDirectory() {
    return defaultWorkingDirectory;
  }

  //copied from trunk.
  @Test
  public void testMkdirsFailsForSubdirectoryOfExistingFile() throws Exception {
    Path testDir = getTestRootPath(fSys, "test/hadoop");
    Assert.assertFalse(exists(fSys, testDir));
    fSys.mkdirs(testDir);
    Assert.assertTrue(exists(fSys, testDir));
    
    createFile(getTestRootPath(fSys, "test/hadoop/file"));
    
    Path testSubDir = getTestRootPath(fSys, "test/hadoop/file/subdir");
    try {
      fSys.mkdirs(testSubDir);
      Assert.fail("Should throw IOException.");
    } catch (IOException e) {
      // expected
    }
    try {
      Assert.assertFalse(exists(fSys, testSubDir));
    } catch(AccessControlException e) {
      // also okay for HDFS.
    }
    
    Path testDeepSubDir = getTestRootPath(fSys, "test/hadoop/file/deep/sub/dir");
    try {
      fSys.mkdirs(testDeepSubDir);
      Assert.fail("Should throw IOException.");
    } catch (IOException e) {
      // expected
    }
    try {
      Assert.assertFalse(exists(fSys, testDeepSubDir));
    } catch(AccessControlException e) {
      // also okay for HDFS.
    }
  }
}
