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

import java.io.IOException;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.*;

/**
 * Test if JobTracker is resilient to garbage in mapred.system.dir.
 */
public class TestMapredSystemDir extends TestCase {
  private static final Log LOG = LogFactory.getLog(TestMapredSystemDir.class);
  
  // mapred ugi
  private static final UserGroupInformation MR_UGI = 
    TestMiniMRWithDFSWithDistinctUsers.createUGI("mr", false);
  private static final FsPermission SYSTEM_DIR_PERMISSION =
    FsPermission.createImmutable((short) 0733); // rwx-wx-wx
  
  public void testGarbledMapredSystemDir() throws Exception {
    final Configuration conf = new Configuration();
    MiniDFSCluster dfs = null;
    MiniMRCluster mr = null;
    try {
      // start dfs
      conf.set("dfs.permissions.supergroup", "supergroup");
      conf.set("mapred.system.dir", "/mapred");
      dfs = new MiniDFSCluster(conf, 1, true, null);
      FileSystem fs = dfs.getFileSystem();
      // create Configs.SYSTEM_DIR's parent (the parent has to be given
      // permissions since the JT internally tries to delete the leaf of
      // the directory structure
      Path mapredSysDir =  new Path(conf.get("mapred.system.dir")).getParent();
      fs.mkdirs(mapredSysDir);
      fs.setPermission(mapredSysDir, new FsPermission(SYSTEM_DIR_PERMISSION));
      fs.setOwner(mapredSysDir, "mr", "mrgroup");

      final MiniDFSCluster finalDFS = dfs;
      
      // Become MR_UGI to do start the job tracker...
      mr = MR_UGI.doAs(new PrivilegedExceptionAction<MiniMRCluster>() {
        @Override
        public MiniMRCluster run() throws Exception {
          // start mr (i.e jobtracker)
          Configuration mrConf = new Configuration();
          
          FileSystem fs = finalDFS.getFileSystem();
          MiniMRCluster mr2 = new MiniMRCluster(0, 0, 0, fs.getUri().toString(),
              1, null, null, MR_UGI, new JobConf(mrConf));
          JobTracker jobtracker = mr2.getJobTrackerRunner().getJobTracker();
          // add garbage to mapred.system.dir
          Path garbage = new Path(jobtracker.getSystemDir(), "garbage");
          fs.mkdirs(garbage);
          fs.setPermission(garbage, new FsPermission(SYSTEM_DIR_PERMISSION));
          return mr2;
        }
      });
      
      // Drop back to regular user (superuser) to change owner of garbage dir
      final Path garbage = new Path(
          mr.getJobTrackerRunner().getJobTracker().getSystemDir(), "garbage");
      fs.setOwner(garbage, "test", "test-group");
      
      // Again become MR_UGI to start/stop the MR cluster
      final MiniMRCluster mr2 = mr;
      MR_UGI.doAs(new PrivilegedExceptionAction<Object>() {
        @Override
        public Object run() throws Exception {
          // stop the jobtracker
          mr2.stopJobTracker();
          mr2.getJobTrackerConf().setBoolean(
              "mapred.jobtracker.restart.recover", false);
          // start jobtracker but dont wait for it to be up
          mr2.startJobTracker(false);

          // check 5 times .. each time wait for 2 secs to check if the
          // jobtracker
          // has crashed or not.
          for (int i = 0; i < 5; ++i) {
            LOG.info("Check #" + i);
            if (!mr2.getJobTrackerRunner().isActive()) {
              return null;
            }
            UtilsForTests.waitFor(2000);
          }
          return null;
        }
      });

      assertFalse("JobTracker did not bail out (waited for 10 secs)", 
                  mr.getJobTrackerRunner().isActive());
    } finally {
      if (dfs != null) { dfs.shutdown(); }
      if (mr != null) { mr.shutdown();}
    }
  }
}